package Respository;

import com.mongodb.reactivestreams.client.MongoCollection;
import model.Notification;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.data.mongodb.core.aggregation.BooleanOperators;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

public class NotificationRepository {

    private final MongoCollection<Notification> notificationCollection;

    public NotificationRepository(MongoCollection<Notification> notificationCollection) {
        this.notificationCollection = notificationCollection;
    }

    public Mono<Boolean> isNotAlreadyProcessing(String notificationId) {
        Bson filter = eq("_id", notificationId);
        Bson update = set("processing", true);
        return Mono.from(notificationCollection.findOneAndUpdate(filter, update))
                .map(document -> document != null);
    }

    private Mono<Void> releaseLock(String notificationId) {
        Bson filter = eq("_id", notificationId);
        Bson update = set("processing", false);
        return Mono.from(notificationCollection.updateOne(filter, update)).then();
    }

    public Mono<Notification> save(Notification notification) {
        return Mono.from(notificationCollection.insertOne(notification)).thenReturn(notification);
    }

    public Flux<Notification> findByRouteIdOrderByCreationTime(String routeId) {
        Document query = new Document("routeId", routeId);
        Document sort = new Document("creationTime", 1); // 1 for ascending, -1 for descending
        return Flux.from(notificationCollection.find(query).sort(sort));
    }




    //++++++++++++++++++++++++s
    public Mono<Void> saveAllNotifications(List<Notification> notifications) {
        if (notifications.isEmpty()) {
            return Mono.empty(); // No notifications to insert
        }

        List<Document> notificationDocuments = notifications.stream()
                .map(notification -> new Document("key", notification.getEventId())
                        .append("message", notification.getMessage()))
                .collect(Collectors.toList());

        return Flux.from(notificationCollection.find(new Document("key", new Document("$in", getKeysFromNotifications(notifications))))
                .collectList()
                .flatMap(existingNotifications -> {
                    List<String> existingKeys = existingNotifications.stream()
                            .map(doc -> doc.getString("key"))
                            .collect(Collectors.toList());

                    List<Document> nonDuplicateDocuments = notificationDocuments.stream()
                            .filter(doc -> !existingKeys.contains(doc.getString("key")))
                            .collect(Collectors.toList());

                    if (nonDuplicateDocuments.isEmpty()) {
                        return Mono.empty(); // No new notifications to insert
                    } else {
                        return Flux.from(notificationCollection.insertMany(nonDuplicateDocuments))
                                .then(); // Notifications inserted successfully
                    }
                });
    }

    private List<String> getKeysFromNotifications(List<Notification> notifications) {
        return notifications.stream()
                .map(Notification::getKey)
                .collect(Collectors.toList());
    }


    //+++++++++++++++

    public void processWithRetry(NotificationDTO notification) {
        notificationMono.flatMap(notification -> processNotification(notification)
                        .doOnSuccess(notificationDTO -> updateStatusInDB(notificationDTO, "completed")
                                .subscribe(updatedRows -> System.out.println("Status updated in DB: " + updatedRows)))
                        .onErrorResume(error -> {
                            System.out.println("Error processing notification: " + error.getMessage());
                            return updateStatusInDB(notification, "failed")
                                    .flatMap(updatedRows -> Mono.error(error)); // Propagate the error after status update
                        })
                        .retryWhen(errors -> errors
                                .zipWith(Flux.range(1, 4), (error, retryCount) -> {
                                    if (retryCount < 4) {
                                        System.out.println("Retrying... (Retry count: " + retryCount);
                                        return retryCount;
                                    } else {
                                        throw new NotificationMonitorBaseException("Exhausted retries"); // Throw exception after 3 retries
                                    }
                                })
                                .flatMap(retryCount -> {
                                    long delaySeconds = (long) Math.pow(2, retryCount - 1) * 3; // Exponential backoff with 3 seconds base
                                    System.out.println("Delaying retry by " + delaySeconds + " seconds");
                                    return Mono.delay(Duration.ofSeconds(delaySeconds)); // Exponential backoff delay
                                }))
                )
                .subscribe(
                        result -> System.out.println("Processing and status update completed"),
                        error -> System.out.println("Error after retries: " + error.getMessage())
                );
    }

    public Mono<List<Notification>> processNotifications(PriorityBlockingQueue<Notification> notificationQueue, String routeID) {
    return Flux.fromIterable(notificationQueue)
            .flatMap(notification -> processWithRetry(notification)
                .onErrorResume(error -> {
        System.out.println("Error processing notification: " + error.getMessage());
        return Mono.empty(); // Continue processing next notification
    })
            .thenReturn(notification) // return the processed notification
        )
                .collectList()
        .doOnSuccess(processedNotifications -> {
        if (processedNotifications.size() == notificationQueue.size()) {
            System.out.println("All notifications for route " + routeID + " processed successfully");
        }
    });

    public Mono<Long> processWithRetry(Notification notification) {
        return Mono.just(notification)
                .flatMap(n -> {
                    return processNotification(n)
                            .then(updateStatusInDB(n, "completed")
                                    .doOnSuccess(updatedRows -> System.out.println("Status updated in DB: " + updatedRows))
                            )
                })
                .onErrorResume(error -> {
                    System.out.println("Error processing notification: " + error.getMessage());
                    return updateStatusInDB(notification, "failed")
                            .flatMap(updatedRows -> Mono.error(new Exception("Failed to process notification", error))); // Return NotificationBaseException with error
                })
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(3))
                        .doBeforeRetry(signal -> System.out.println("Retrying... (Retry count: " + signal.totalRetriesInARow()))
                        .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> new Exception("Exhausted retries"))
                )
                .onErrorReturn(new NotificationBaseException("Failed to process notification", error))
                .doOnError(error -> System.out.println("Error after retries: " + error.getMessage())) // Log error after returning
                .doOnSuccess(updatedRows -> updateStatusInDB(notification, "success"))
                .onErrorResume(NotificationBaseException.class, ex -> updateStatusInDB(notification, "failed")
                        .then(Mono.error(ex))
                );

    <T> T processNotification(Notification notification) {
        return (T) notification;
    }
    Mono<Long> updateStatusInDB(Notification notification, String status) {
        return Mono.just(1l)

    }

    Mono<List<Notification>> processEnqueueNotification(PriorityBlockingQueue<Notification> queue , String routeId) {

        return Flux.fromIterable(queue)
                .flatMap(notification -> updateStatusInDB(notification , routeId)
                .thenReturn(notification)
        ).collectList();
    }


    Mono<List<Notification>> processNotification(String routeId, List<Notification> notifications) {
        PriorityBlockingQueue<Notification> queue = new PriorityBlockingQueue<>(500, Comparator.comparing(Notification::getCreatedTime));

        return enqueueNotifications(queue, notifications)
                .flatMap(enqueueSuccess -> processEnqueueNotification(queue, routeId));
    }

    Mono<Boolean> enqueueNotifications(PriorityBlockingQueue<Notification> queue, List<Notification> notifications) {
        return Flux.fromIterable(notifications)
                .flatMap(notification -> enqueueNotification(queue, notification))
                .collectList()
                .map(enqueueResults -> enqueueResults.stream().allMatch(result -> result));
    }

    Mono<Boolean> enqueueNotification(PriorityBlockingQueue<Notification> queue, Notification notification) {
        return Mono.fromCallable(() -> queue.offer(notification));
    }

    Mono<Boolean> enqueueNotifications(PriorityBlockingQueue<Notification> queue, Flux<Notification> notifications) {
        return notifications
                .collectList()
                .map(queue::addAll) // Assuming addAll returns a boolean indicating whether the queue changed as a result of the call
                .onErrorResume(error -> Mono.just(false)); // Handling any error in enqueuing notifications
    }

    private static class NotificationBaseException extends  Exception{
        public NotificationBaseException(String failedToProcessNotification, Throwable p1) {
        }
    }
}


}