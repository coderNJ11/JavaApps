package KafkaPartition;
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

import java.util.Map;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CustomPartitioner implements Partitioner {
    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        // Custom logic to determine the partition based on the key (routeId)
        String routeId = (String) key;

        // Hash the routeId using SHA-256
        int partition = hashRouteId(routeId, cluster.partitionCountForTopic(topic));

        return partition;
    }

    private int hashRouteId(String routeId, int partitionCount) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(routeId.getBytes(StandardCharsets.UTF_8));
            int hashValue = Math.abs(bytesToInt(hash));
            return hashValue % partitionCount;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }

    private int bytesToInt(byte[] bytes) {
        int result = 0;
        for (int i = 0; i < 4; i++) {
            result <<= 8;
            result |= (bytes[i] & 0xFF);
        }
        return result;
    }

    @Override
    public void close() {
        // Perform any cleanup
    }

    @Override
    public void configure(Map<String, ?> configs) {
        // Perform any configuration
    }
}