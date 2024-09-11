package model;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@Named
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Configuration implements Serializable {

    String serviceName;
    String baseUrl;
    private Authentication auth;
    private List<ServiceResource> resource;
    APIExceptionConfig exceptionConfig;
    PartnerAPISuccessConfig successResponseConfig;


}
