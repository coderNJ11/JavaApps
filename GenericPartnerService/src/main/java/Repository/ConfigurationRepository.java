package Repository;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import model.Configuration;
import org.bson.Document;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.stereotype.Repository;

@Repository
public class ConfigurationRepository {

    private final MongoCollection<Configuration> configurationMongoCollection;

    public ConfigurationRepository(MongoClient mongoClient) {
        MongoDatabase database = mongoClient.getDatabase("yourDatabaseName");
        this.configurationMongoCollection = database.getCollection("yourCollectionName", Configuration.class);
    }

    public Mono<Configuration> saveConfiguration(Configuration configuration) {
        return Mono.from(configurationMongoCollection
                        .insertOne(configuration))
                .then(Mono.just(configuration));
    }

    public Flux<Configuration> findByServiceName(String serviceName) {
        return Flux.from(configurationMongoCollection
                .find(new Document("serviceName", serviceName), Configuration.class));
    }

    public Flux<Configuration> findByBaseUrl(String baseUrl) {
        return Flux.from(configurationMongoCollection
                .find(new Document("baseUrl", baseUrl), Configuration.class));
    }
}