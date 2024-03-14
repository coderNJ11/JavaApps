package processor;

import Respository.NotificationRepository;
import model.Notification;
import org.redisson.api.RedissonClient;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

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


}