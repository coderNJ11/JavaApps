package KafkaConsumer;


import Respository.NotificationRepository;
import model.Notification;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import reactor.core.publisher.Mono;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class NotificationConsumerService {
    private final NotificationRepository notificationRepository;
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    private final AtomicInteger retryCount = new AtomicInteger(0);

    private Notification currentNotification;

    public NotificationConsumerService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @KafkaListener(topics = "inBoundNotifq")
    @Retryable(maxAttempts = 3, value = {Exception.class}) // Retry up to 3 times with a 5-second delay
    public void processKafkaNotification(Notification notification) {
        currentNotification = notification;
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

    @Recover
    public void recoverFromException(Exception exn) {
        if (retryCount.get() < 3) {
            executor.schedule(() -> {
                processKafkaNotification(currentNotification);
            }, 5, TimeUnit.SECONDS);

            retryCount.incrementAndGet();
        } else {
            // Handle the exception after maximum retries
            // ...
        }
    }

    // Existing code...
}