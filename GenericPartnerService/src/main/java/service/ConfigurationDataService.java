package service;

import model.Configuration;
import reactor.core.publisher.Mono;

public class ConfigurationDataService {

    public Configuration getConfigurationByTennantName(String tennantName) {
        return null;
    }

    public Mono<Configuration> getConfigurationResourceSpec(final String serviceName, final String resourceName,final String version, final String apiName) {

        return Mono.just(new Configuration());
    }


    public Mono<Configuration> getConfigurationDetails(String serviceName, String resourceName, String versionNumber, String apiName) {
        return Mono.just(new Configuration());
    }
}
