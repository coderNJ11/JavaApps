package service;



import com.sun.mail.iap.ResponseHandler;
import model.APIDetails;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.function.Consumer;

public class ServiceCaller {

    private final String serviceName;
    private final WebClient client;

    private ServiceCallerEnricher enricher = null;


    private BaseUrlProvider baseUrlProvider = null;



    public ServiceCaller(String serviceName, WebClient client) {
        this.serviceName = serviceName;
        this.client = client;
    }

    public ServiceCaller(String serviceName, WebClient client, ServiceCallerEnricher enricher) {
        this.serviceName = serviceName;
        this.client = client;
        this.enricher = enricher;
    }


    public ServiceCaller(String serviceName, WebClient client, BaseUrlProvider baseUrlProvider, ServiceCallerEnricher enricher) {
        this.serviceName = serviceName;
        this.client = client;
        this.enricher = enricher;
        this.baseUrlProvider = baseUrlProvider;
    }

    public String getServiceName() {
        return this.serviceName;
    }

    public WebClient getWebClient() {
        return this.client.mutate().baseUrl(this.baseUrlProvider.getBaseUrl()).build();
    }



    public static class ServiceCallBuilder<B> {
        private WebClient client = null;

        private HttpMethod method = null;

        private final Class<B> requestBodyType;

        private final String uriString;

        private final Object[] uriVariables;

        private URI uri;

        private String serviceName;

        private ServiceCallerEnricher enricher;

        private MediaType mediaType;

        private Consumer<HttpHeaders> injector;

        private MultiValueMap<String, String> params;

        private B body;

        private Publisher<B> bodyPublisher;

        private MediaType contentType;
        private BaseUrlProvider baseUrlProvider;


        public ServiceCallBuilder(WebClient client, HttpMethod method, Class<B> requestBodyType, String uriString, Object[] uriVariables, URI uri, String serviceName, ServiceCallerEnricher enricher, MediaType mediaType, Consumer<HttpHeaders> injector, MultiValueMap<String, String> params, B body, Publisher<B> bodyPublisher, MediaType contentType) {

            this.client = client;
            this.method = method;
            this.requestBodyType = requestBodyType;
            this.uriString = uriString;
            this.uriVariables = uriVariables;
            this.uri = uri;
            this.serviceName = serviceName;
            this.enricher = enricher;
            this.mediaType = mediaType;
            this.injector = injector;
            this.params = params;
            this.body = body;
            this.bodyPublisher = bodyPublisher;
            this.contentType = contentType;
        }


        public ServiceCallBuilder<B> withQueryParams(MultiValueMap<String, String> queryParams) {
            this.params = queryParams;
            return this;
        }

        public ServiceCallBuilder<B> withUri(URI uri) {
            this.uri = uri;
            return this;
        }

        public ServiceCallBuilder<B> withBody(B body) {
            this.body = body;
            return this;
        }

        public ServiceCallBuilder<B> withBodyPublisher(Publisher<B> bodyPublisher) {
            this.bodyPublisher = bodyPublisher;
            return this;
        }

        public ServiceCallBuilder<B> withMediaType(MediaType mediaType) {
            this.mediaType = mediaType;
            return this;
        }

        public ServiceCallBuilder<B> withHeaders(Consumer<HttpHeaders> injector) {
            this.injector = injector;
            return this;
        }

        public ServiceCallBuilder<B> withParams(MultiValueMap<String, String> params) {
            this.params = params;
            return this;
        }

        public ServiceCallBuilder<B> withEnricher(ServiceCallerEnricher enricher) {
            this.enricher = enricher;
            return this;
        }

        public ServiceCallBuilder<B> withBaseUrlProvider(BaseUrlProvider baseUrlProvider) {
            this.baseUrlProvider = baseUrlProvider;
            return this;
        }

        public ServiceCallBuilder<B> withServiceName(String serviceName) {
            this.serviceName = serviceName;
            return this;
        }

        public ServiceCallBuilder<B> contentType(MediaType contentType) {
            this.contentType = contentType;
            return this;
        }

        public ServiceCallBuilder<B> withHeaderInjector(Consumer<HttpHeaders> headerInjector) {
            this.injector = headerInjector;
            return this;
        }

        public <R> Mono<R> build(ResponseHandler<R> responseHandler) {
            WebClient.RequestHeadersSpec var1 = this.buildRequest();
            return this.applySettings(var1.exchangeToMono(responseHandler::handle));
        }

        @FunctionalInterface
        public interface ResponseHandler<R> {
            R handle(ClientResponse response);
        }

        private <R> Mono<R> applySettings(Mono mono) {

            return mono;
        }

        private WebClient.RequestHeadersSpec buildRequest() {

            WebClient.RequestHeadersSpec request = this.client.method(this.method).uri(this.uriString, this.uriVariables);
            return request;
        }


    }
}
