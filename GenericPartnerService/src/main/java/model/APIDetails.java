package model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.inject.Named;
import java.io.Serializable;

@Setter
@Getter
@Named
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
public class APIDetails implements Serializable {
    String serviceName;
    String baseUrl;
    String method;
    String uri;
    String authType;
    @ToString.Exclude
    String a3Context;
    String receivedAppId;
    @ToString.Exclude
    String hmacSecret;
    @ToString.Exclude
    long hmacSecretVersion;
    String responseType;
    String requestJoltSpec;
    String responseJoltSpec;
    String queryParams;
    String pathParams;
    String uriVariables;
    String a3ReceiverAppId;
    String apiName;
    String respurceName;
    String apiversion;

    APIExceptionConfig exceptionConfig;
    APIExceptionConfig serviceExceptionConfig;
    PartnerAPISuccessConfig successResponseConfig;
    PartnerAPISuccessConfig serviceSuccessResponseConfig;
}
