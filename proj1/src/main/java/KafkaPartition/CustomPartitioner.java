import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

import java.util.Map;

public class RouteIdPartitioner implements Partitioner {
    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        if (key instanceof String) {
            int numPartitions = cluster.partitionCountForTopic(topic);
            String routeId = (String) key;
            // Use the hash code of the SHA256 string to determine the partition
            int partition = Math.abs(routeId.hashCode()) % numPartitions;
            return partition;
        } else {
            return 0; // Default partition if key format is not as expected
        }
    }

    @Override
    public void close() {
        // Optionally close any resources used by the partitioner
    }

    @Override
    public void configure(Map<String, ?> configs) {
        // Optionally configure the partitioner
    }
}