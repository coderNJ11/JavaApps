package service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import model.APIDetails;
import model.Configuration;
import model.PartnerApiResponse;
import model.PartnerServiceRequest;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.json.JsonParseException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.List;
import java.util.Map;
import Exception.*;

import javax.inject.Named;

import static Exception.EFormsErrorCode.EFORMS_GENERIC_ERROR;
import static utils.GenericConstants.REQ_TRANSFORM_FIELD;

@Named
@Slf4j
public class PartnerService {

    private final ConfigurationDataService configurationDataService;

    private final WebClientService webClientService;
    private final EFormsServiceHelper eFormsServiceHelper;

    private final JoltTranformService joltTranformService;
    private final ObjectMapper partnerServiceObjectMapper ;


    public PartnerService(EFormsServiceHelper eFormsServiceHelper,
                          ConfigurationDataService configurationDataService,
                          WebClientService webClientService,
                          JoltTranformService joltTranformService) {
        this.eFormsServiceHelper = eFormsServiceHelper;
        this.partnerServiceObjectMapper = new ObjectMapper();
        this.configurationDataService = configurationDataService;
        this.webClientService = webClientService;
        this.joltTranformService = joltTranformService;
    }


    public Mono<ServerResponse> callParnterService(final ServerRequest request) {
        return request.bodyToMono(String.class)
                .flatMap(inputData -> {
                    Tuple2<Map, Map> reqKeyValueDataTypeMap = eFormsServiceHelper.getFalttenedJsonData(inputData);
                    Map<String, Object> reqData = (Map<String, Object>) reqKeyValueDataTypeMap;
                    PartnerServiceRequest partnerServiceRequest = convertJsonToObject(inputData, PartnerServiceRequest.class);
                    performValidation(partnerServiceRequest);
                    final org.springframework.http.HttpHeaders headers = request.headers().asHttpHeaders();
                    Mono<Tuple2<PartnerApiResponse, APIDetails>> responseDetails = invokePartnerAPI(partnerServiceRequest , headers, inputData, reqData);
                    return getTransformedResponse(responseDetails , reqData);
                });
    }

    private Mono<? extends ServerResponse> getTransformedResponse(Mono<Tuple2<PartnerApiResponse, APIDetails>> responseDetails, Map<String, Object> reqData) {

        return null;
    }

    public Mono<Tuple2<PartnerApiResponse, APIDetails>> invokePartnerAPI(PartnerServiceRequest partnerServiceRequest, HttpHeaders headers, String payLoad, Map<String, Object> reqData) {

        //validate input paramters
        if (partnerServiceRequest == null || headers == null || payLoad == null || reqData == null) {
            return Mono.error(new IllegalArgumentException("Partner Service request is null"));
        }

        String serviceName = (String) reqData.get("serviceName");
        String resourceName = (String) reqData.get("resourceName");
        String versionNumber = (String) reqData.get("versionNumber");
        String apiName = (String) reqData.get("apiName");
        Mono<APIDetails> apiConfigDetails = getConfigurationDetails(serviceName, resourceName, versionNumber, apiName)
                .switchIfEmpty(Mono.error(new EFormBusinessServiceException(EFormsErrorCode.EFORM_GENERIC_ERROR, "Service not found")));

        Mono<ServiceCaller> serviceCaller = apiConfigDetails.flatMap(apiDetails -> Mono.just(webClientService.getServiceCaller(apiDetails)));

        return Mono.zip(apiConfigDetails, serviceCaller).flatMap(data -> {
            APIDetails apiDetails = data.getT1();
            ServiceCaller serviceObj = data.getT2();
            String trasnFormRequest = payLoad;
            String requestJoltSpec = getJoltTrasnformationSpec(REQ_TRANSFORM_FIELD, apiDetails, reqData);
            checkFormMandatoryApiConfig(apiDetails);

            if (StringUtils.isNoneBlank(requestJoltSpec)) {
                try {
                    trasnFormRequest = joltTranformService.transform(payLoad, requestJoltSpec);
                } catch (Exception ex) {
                    throw new EFormBusinessServiceException(EFORMS_GENERIC_ERROR, ex.getMessage());
                }
            }

            MultiValueMap<String, String> queryParams = getQueryParams(apiDetails, partnerServiceRequest);

            ServiceCaller.ServiceCallBuilder<String> serviceCallerBuilder = getServiceCallerByRequest(serviceObj, apiDetails,
                    partnerServiceRequest, trasnFormRequest);

            Mono<PartnerApiResponse> response = serviceCallerBuilder
                    .withQueryParams(queryParams)
                    .contentType(MediaType.APPLICATION_JSON)
                    .withHeaderInjector(httpHeaders -> httpHeaders.addAll(eFormsServiceHelper.getHeaders(headers)))
                    .build(clientResponse -> handleResponse(PartnerApiResponse.class, clientResponse, apiDetails))
                    .onErrorResume(Mono::error).block();

            return Mono.zip(response, Mono.just(apiDetails));

        });
    }

    private <T> Mono<T> handleResponse(Class<T> type, ClientResponse clientResponse, APIDetails apiDetails) {
        return (Mono<T>) Mono.just(clientResponse);
    }


    private ServiceCaller.ServiceCallBuilder<String> getServiceCallerByRequest(ServiceCaller serviceObj, APIDetails apiDetails, PartnerServiceRequest partnerServiceRequest, String trasnFormRequest) {
    }

    private MultiValueMap<String, String> getQueryParams(APIDetails apiDetails, PartnerServiceRequest partnerServiceRequest) {
        Map<String, Object> requestData = partnerServiceRequest.getData();
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        if(StringUtils.isNoneBlank(apiDetails.getQueryParams())) {
            List<String> lst = List.of(apiDetails.getQueryParams().split(","));
            lst.forEach(param -> {
                if(ObjectUtils.isNotEmpty(requestData.get(param))) {
                    queryParams.put(param, List.of(requestData.get(param).toString()));
                }
            });
        }
        return queryParams;
    }

    private void checkFormMandatoryApiConfig(APIDetails apiDetails) {
        if(StringUtils.isBlank(apiDetails.getUri())) {
            throw new RuntimeException("Missing Mandatory parameter uri");
        }

        if (StringUtils.isBlank(apiDetails.getMethod())) {
            throw new RuntimeException("Missing Mandatory parameter method");
        }
        if (StringUtils.isBlank(apiDetails.getAuthType())) {
            throw new RuntimeException("Missing Mandatory parameter authType");
        }

        if (StringUtils.isBlank(apiDetails.getBaseUrl())) {
            throw new RuntimeException("Missing Mandatory parameter baseUrl");
        }

    }

    private String getJoltTrasnformationSpec(String reqTransformField, APIDetails apiDetails, Map<String, Object> reqData) {
        return null;
    }

    private Mono<APIDetails> getConfigurationDetails(String serviceName, String resourceName, String versionNumber, String apiName) {
        Mono<Configuration> configurationMono = configurationDataService.getConfigurationResourceSpec(serviceName, resourceName, versionNumber, apiName);

        return configurationMono.flatMap(emailConfig -> {
            APIDetails apiDetails = eFormsServiceHelper.getApiDetails(emailConfig, serviceName,
                resourceName, versionNumber.toLowerCase(), apiName);
            return Mono.just(apiDetails);
        });
    }

    private void performValidation(PartnerServiceRequest partnerServiceRequest) {
        if(partnerServiceRequest == null) {
            throw new IllegalArgumentException("Partner Service request is null");
        }

        Map<String, Object> requestData = partnerServiceRequest.getData();

        validateMandatoryFeild(requestData , "serviceName");
    }

    private void validateMandatoryFeild(Map<String, Object> requestData, String feildName) {
        if(! (requestData.get(feildName) instanceof String)) {
            throw new IllegalArgumentException("Invalid Feild value");
        }
        String feildValue = (String) requestData.get(feildName);
        if(StringUtils.isEmpty(feildValue)) {
            throw new IllegalArgumentException("Mandataotry feild not available");
        }
    }


    public <T> T convertJsonToObject(String value, Class<T> valueClass) {
        try {
            T t = (T) partnerServiceObjectMapper.readValue(value, valueClass);
            return t;
        }
        catch(JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return  null;
    }
 }
