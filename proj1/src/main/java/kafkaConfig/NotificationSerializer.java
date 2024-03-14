package kafkaConfig;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;
import model.Notification;

import java.util.Map;

public class NotificationSerializer implements Serializer<Notification> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // No additional configuration needed
    }

    @Override
    public byte[] serialize(String topic, Notification data) {
        byte[] serializedData = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            serializedData = mapper.writeValueAsBytes(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serializedData;
    }

    @Override
    public void close() {
        // No resources to close
    }
}