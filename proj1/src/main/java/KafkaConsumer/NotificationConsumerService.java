package KafkaConsumer;


import Respository.NotificationRepository;
import model.Notification;
import org.springframework.kafka.annotation.KafkaListener;
import reactor.core.publisher.Mono;

public class NotificationConsumerService {
    private final NotificationRepository notificationRepository;

    public NotificationConsumerService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @KafkaListener(topics = "inBoundNotifq")
    public void processKafkaNotification(Notification notification) {
        saveAndProcessNotification(notification).subscribe();
    }

    private Mono<Void> saveAndProcessNotification(Notification notification) {
        // Process the single notification synchronously, then save to MongoDB
        return processSingleNotification(notification);
    }

    private Mono<Void> processSingleNotification(Notification notification) {
        // Process the single notification synchronously
        // ...

        // Update the notification as processed
        notification.setProcessed(true);
        return notificationRepository.save(notification).then();
    }
}