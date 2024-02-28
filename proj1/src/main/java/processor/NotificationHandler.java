package processor;

import Service.NotificationService;
import model.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public class NotificationHandler {

    @Autowired
    private NotificationService notificationService;

    public Mono<ServerResponse> addNotification(ServerRequest request) {
        Mono<Notification> notificationMono = request.bodyToMono(Notification.class);
        return notificationMono.flatMap(notification -> {
            notificationService.saveNotification(notification);
            return ServerResponse.ok().build();
        });
    }

    Mono<ServerResponse> saveNotification(final Notification notification) {
        return yourReactiveMongoCollection.insertOne(document)
                .then(ServerResponse.ok().bodyValue("Document inserted successfully"))
                .onErrorResume(MongoWriteException.class, ex -> {
                    if (ex.getCode() == 11000) {
                        return ServerResponse.ok().bodyValue("Document already inserted");
                    }
                    return Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to insert document"));
                });
    }
    }
}