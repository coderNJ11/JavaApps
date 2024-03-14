package kafkaConfig;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;
import model.Notification;

import java.util.Map;

public class NotificationDeserializer implements Deserializer<Notification> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // No additional configuration needed
    }

    @Override
    public Notification deserialize(String topic, byte[] data) {
        ObjectMapper mapper = new ObjectMapper();
        Notification notification = null;
        try {
            notification = mapper.readValue(data, Notification.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return notification;
    }

    @Override
    public void close() {
        // No resources to close
    }
}