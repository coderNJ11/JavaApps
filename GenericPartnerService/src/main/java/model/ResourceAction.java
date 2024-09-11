package model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpMethod;

import javax.inject.Named;

@Getter
@Setter
@Named
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
public class ResourceAction {
    private HttpMethod method;
    private String requestJoltSpec;
    String subUri;
    String apiName;
    String responseJoltPSec;
    String queryParams;
    String pathParams;
    String uriVariable;
    String responseType;
    APIExceptionConfig exceptionConfig;
    PartnerAPISuccessConfig partnerAPISuccessConfig;
    APIStatusCodeMapping statusCodeMapping;



}
