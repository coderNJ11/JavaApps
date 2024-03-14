package Controller;


import model.Notification;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public class NotificationProducerService {
    private final KafkaTemplate<String, Notification> kafkaTemplate;

    public NotificationProducerService(KafkaTemplate<String, Notification> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    // REST endpoint to save notifications to Kafka
    public Mono<ServerResponse> saveNotificationToKafka(ServerRequest request) {
        return request.bodyToMono(Notification.class)
                .doOnNext(notification -> kafkaTemplate.send("inBoundNotifq", notification.getRouteId(), notification))
                .then(ServerResponse.ok().build());
    }
}
