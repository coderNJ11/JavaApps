package Controller;


import lombok.extern.slf4j.Slf4j;
import model.Notification;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
public class NotificationProducerService {
    private final KafkaTemplate<String, Notification> kafkaTemplate;

    public NotificationProducerService(KafkaTemplate<String, Notification> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    // REST endpoint to save notifications to Kafka
    public Mono<ServerResponse> saveNotificationToKafka(ServerRequest request) {
        return request.headers().header("X-Apple-Client-App-ID").stream().findFirst()
                .map(clientAppId -> request.bodyToMono(Notification.class)
                        .flatMap(notification -> Mono.fromFuture(() -> kafkaTemplate.send("inBoundNotifq", notification.getRouteId(), notification))
                                .handle((recordMetadata, sink) -> {
                                    if (recordMetadata != null) {
                                        sink.next(recordMetadata);
                                    } else {
                                        sink.error(new RuntimeException("Error while sending notification to Kafka"));
                                    }
                                })
                                .flatMap(result -> ServerResponse.ok().build())
                                .doOnError(error -> log.error("Error while saving notification to Kafka {}", error))
                                .onErrorMap(error -> new RuntimeException("Error while saving notification to Kafka"))
                        )
                )
                .orElse(Mono.error(new IllegalArgumentException("X-Apple-Client-App-ID header not found")));
    }
}
