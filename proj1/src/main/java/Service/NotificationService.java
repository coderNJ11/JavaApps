package Service;
import model.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Service
public class NotificationService {

    @Autowired
    private ReactiveMongoTemplate reactiveMongoTemplate;

    public Mono<Notification> saveNotification(Notification notification) {
        return reactiveMongoTemplate.save(notification);
    }
}