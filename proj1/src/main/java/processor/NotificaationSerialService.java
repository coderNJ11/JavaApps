package processor;

import Respository.NotificationRepository;
import model.Notification;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

public class NotificaationSerialService {
    private final NotificationRepository notificationRepository;
    private final RedissonClient redisson;

    public NotificaationSerialService(NotificationRepository notificationRepository, RedissonClient redisson) {
        this.notificationRepository = notificationRepository;
        this.redisson = redisson;
    }
    public Mono<ServerResponse> saveAndProcessNotification(Notification notification) {
        return notificationRepository.save(notification)
                .flatMap(savedNotification ->
                        notificationRepository.isNotAlreadyProcessing(notification.getRouteId())
                                .filter(notProcessing -> notProcessing)
                                .flatMap(notUsed -> processNotification(savedNotification))  // Assuming processNotification returns a Mono<Void>
                                .then(ServerResponse.ok().build())
                );
    }

    private Mono<Notification> processNotification(Notification notification) {
        return notificationRepository.findByRouteIdOrderByCreationTime(notification.getRouteId())
                .filter(notif -> !notif.isProcessed())
                .collectList()
                .flatMap(notifications -> {
                    List<Mono<Notification>> processedNotifications = notifications.stream()
                            .map(this::processSingleNotification)
                            .collect(Collectors.toList());
                    return Mono.when(processedNotifications).then(Mono.just(notification));
                });
    }

    private Mono<Notification> processSingleNotification(Notification notification) {
        // Process the single notification synchronously
        // ...

        // Update the notification as processed
        notification.setProcessed(true);
        return notificationRepository.save(notification);
    }







//    private RLock getNotificationLock(String notificationId) {
//        return redisson.getLock("notificationId:" + notificationId);
//    }
//
//    private boolean isNotAlreadyProcessing(String notificationId) {
//        RLock lock = getNotificationLock(notificationId);
//        try {
//            // Try to acquire the lock with a timeout
//            boolean isLockAcquired = lock.tryLock(100, 10, TimeUnit.SECONDS);
//            return isLockAcquired;
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//            return false;
//        }
//    }
//
//    private void releaseLock(String notificationId) {
//        RLock lock = getNotificationLock(notificationId);
//        if (lock.isLocked()) {
//            lock.unlock();
//        }
//    }
//
//    // Add a method to close the RedissonClient when the application shuts down
//    public void closeRedissonClient() {
//        redisson.shutdown();
//    }
}