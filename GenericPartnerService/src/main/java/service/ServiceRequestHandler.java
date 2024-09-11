package service;

import lombok.extern.slf4j.Slf4j;
import model.*;
import org.bson.Document;
import org.json.JSONObject;
import org.apache.commons.lang3.*;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import Exception.*;


import javax.inject.Inject;
import javax.inject.Named;
import java.net.http.HttpHeaders;
import java.util.HashMap;
import java.util.Optional;

import utils.GenericConstants;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.springframework.util.StringUtils.*;
import static utils.GenericConstants.*;

@Slf4j
@Named
public class ServiceRequestHandler {

    private final EFormsServiceHelper eFormsServiceHelper;
    private final ConfigurationDataService configurationDataService;
    private final FormIORequestHandler formIORequestHandler;
    private final EFormsService eFormsService;
    private final PartnerService partnerService;
    private final FormSubmissionService formSubmissionService;

    @Inject
    public ServiceRequestHandler(EFormsServiceHelper eFormsServiceHelper, ConfigurationDataService configurationDataService,
                                 FormIORequestHandler formIORequestHandler, EFormsService eFormsService, PartnerService partnerService,
                                 FormSubmissionService formSubmissionService) {
        this.eFormsServiceHelper = eFormsServiceHelper;
        this.configurationDataService = configurationDataService;
        this.formIORequestHandler = formIORequestHandler;
        this.eFormsService = eFormsService;
        this.partnerService = partnerService;
        this.formSubmissionService = formSubmissionService;
    }


    public Mono<ServerResponse> processFormSubmission(final ServerRequest request) {

        log.warn("processFormSubmission :: start ", request.headers().asHttpHeaders().getFirst("X-Apple-Request-ID"));

        Mono<HashMap<String, Object>> requestDetails = request.bodyToMono(String.class)
                .flatMap(submissionRequest -> {
                    JSONObject inputJsonObject = eFormsServiceHelper.getInputJsonObject(submissionRequest);
                    performValidation(inputJsonObject);
                    final String serviceName = "ibpm";
                    final Optional<String> resourceName = Optional.of("route");
                    FormSubmission formSubmissionData = eFormsServiceHelper.getConvertedFormSubmission(submissionRequest);
                    String formId = (String) formSubmissionData.getData().get("formId");
                    String requestSubmissionId = StringUtils.defaultIfEmpty((String) formSubmissionData.getData().get("_formio_submissionId"), "");

                    return formIORequestHandler.getFormDetailsById(formId)
                            .switchIfEmpty(Mono.error(() ->
                                    new EFormBusinessServiceException(EFormsErrorCode.EFORMS_GENERIC_ERROR, "Form not found for formId : " + formId)))
                            .flatMap(formDetails -> {
                                String tennantName = (String) getTennantName(formDetails).get("name");
                                HttpHeaders headersByToken = getHeadersByTokenType(request.headers().asHttpHeaders(), tennantName);
                                HttpHeaders headers = getPopulatedHeaders(headersByToken);

                                log.warn("processFormSubmission :: end ", request.headers().asHttpHeaders().getFirst("X-Apple-Request-ID"));
                                String formSubmissionAction = eFormsServiceHelper.getFormSubmissionAction(requestSubmissionId, inputJsonObject);
                                String state = inputJsonObject.has("state") ? inputJsonObject.getString("state") : "";

                                if (isFormRouteEnabled(formDetails)) {
                                    HashMap<String, Object> apiDetailsMap = new HashMap<>();
                                    apiDetailsMap.put(ROUTE_ENABLED, false);
                                    apiDetailsMap.put(INPUT, submissionRequest);
                                    apiDetailsMap.put(TENNANT_NAME, tennantName);
                                    apiDetailsMap.put(FORM_SUBMISSION_ACTION, formSubmissionAction);
                                    apiDetailsMap.put(HEADERS, headers);
                                    apiDetailsMap.put(FORMIO_SUBMISSION_ID, requestSubmissionId);
                                    apiDetailsMap.put(FORM_SUBMISSION_STATE, state);
                                    apiDetailsMap.put(FORM_DETAILS, formDetails);
                                    apiDetailsMap.put(EFROM_NAME, formDetails.getName());
                                    return Mono.just(apiDetailsMap);
                                } else {
                                    Mono<Configuration> configurationMono;
                                    String versionNumber = StringUtils.defaultIfEmpty((String) formSubmissionData.getData().get("version"), "");
                                    final String apiName = getIBPMApiNameByAction(formSubmissionAction, formSubmissionData);

                                    configurationMono = configurationDataService.getConfigurationResourceSpec(serviceName, resourceName.get(), versionNumber.toLowerCase(),
                                                    apiName)
                                            .switchIfEmpty(Mono.error(() ->
                                                    new EFormBusinessServiceException(EFormsErrorCode.EFORMS_GENERIC_ERROR, "Configuration not found for apiName : " + apiName)));

                                    Mono<HashMap<String, Object>> apiDetailsMono = configurationMono.map(configuration -> {

                                        HashMap<String, Object> apiDetails = getApiDetailsForRouteEnabledFormSubmission(configuration,
                                                resourceName.get(), versionNumber, apiName, submissionRequest, formDetails);

                                        apiDetails.put("formDetails", formDetails);
                                        apiDetails.put(ROUTE_ENABLED, "true");
                                        apiDetails.put(FORM_SUBMISSION_ACTION, formSubmissionAction);
                                        apiDetails.put(HEADERS, headers);
                                        apiDetails.put(FORMIO_SUBMISSION_ID, requestSubmissionId);
                                        apiDetails.put(FORM_SUBMISSION_STATE, state);
                                        ProcessInsatnceInfo processInsatnceInfo = getRouteInfrmation(apiDetails, headers);
                                        apiDetails.put("roueIds", processInsatnceInfo);

                                        if (isEmpty((String) apiDetails.get(TENNANT_NAME))) {
                                            throw new EFormBusinessServiceException(EFormsErrorCode.EFORM_GENERIC_ERROR, "tenant name not found");
                                        }
                                        if (isEmpty((String) apiDetails.get(GenericConstants.EFROM_FORMNAME))) {
                                            throw new EFormBusinessServiceException(EFormsErrorCode.EFORMS_FORMNAME_NOT_FOUND, "form name not found");
                                        }
                                        if (ObjectUtils.isNotEmpty((String) apiDetails.get(SUBMISSION_ACTION)) &&
                                                !SUBMISSION_DELETE_ACTION.equalsIgnoreCase((String) apiDetails.get(SUBMISSION_ACTION)) &&
                                                isEmpty(processInsatnceInfo.getTemplateId())) {
                                            throw new EFormBusinessServiceException(EFormsErrorCode.EFORMS_TEMPLATE_NOT_FOUND, "template not found");
                                        }
                                        return apiDetails;
                                    });

                                    // Call executePartnerService here
                                    return apiDetailsMono.flatMap(apiDetails -> {
                                        PartnerServiceRequest partnerServiceRequest = createPartnerServiceRequest(apiDetails); // Create the PartnerServiceRequest object based on apiDetails
                                        return configurationDataService.executePartnerService(partnerServiceRequest, headers, payLoad, reqData);
                                    });
                                }
                            });
                });

        return requestDetails.flatMap(processRequest -> {
                    if (TRUE.equals(processRequest.get(ROUTE_ENABLED))) {
                        log.warn("processFormSubmission :: end ", request.headers().asHttpHeaders().getFirst("X-Apple-Request-ID"));
                        return formSubmissionService.handleSaveSubmissionForRouteForm(processRequest)
                                .flatMap(response -> handleUploadFiles(processRequest, response))
                                .flatMap(response -> handleDeleteFiles(processRequest, response))
                                .doOnSuccess(formSubmissionResponse -> formSubmissionPostProcess(processRequest, formSubmissionResponse));
                    } else if (FALSE.equals(processRequest.get(ROUTE_ENABLED))) {
                        log.warn("processFormSubmission :: end ", request.headers().asHttpHeaders().getFirst("X-Apple-Request-ID"));
                        return formSubmissionService.handlerSubmissionForNonRouteForm(processRequest)
                                .flatMap(response -> handleUploadFiles(processRequest, response))
                                .flatMap(response -> handleDeleteFiles(processRequest, response))
                                .doOnSuccess(formSubmissionResponse -> formSubmissionPostProcess(processRequest, formSubmissionResponse));

                    } else {
                        throw new IBPMServiceException("EFORMS_GENERIC_ERROR");
                    }
                }).flatMap(response -> ServerResponse.ok().bodyValue(response))
                .onErrorResume(Mono::error);

    }

    private PartnerServiceRequest createPartnerServiceRequest(HashMap<String, Object> apiDetails) {
        PartnerServiceRequest partnerServiceRequest = new PartnerServiceRequest();
        partnerServiceRequest.setData(apiDetails);
        return partnerServiceRequest;
    }

    private Object formSubmissionPostProcess(HashMap<String, Object> processRequest, Object formSubmissionResponse) {
        return null;
    }

    private Mono<FormSubmissionResponse> handleDeleteFiles(HashMap<String, Object> processRequest, FormSubmissionResponse response) {
        return null;
    }

    private Mono<FormSubmissionResponse> handleUploadFiles(HashMap<String, Object> processRequest, FormSubmissionResponse response) {
        return null;
    }

    private ProcessInsatnceInfo getRouteInfrmation(HashMap<String, Object> apiDetails, HttpHeaders headers) {
        return null;
    }

    private HashMap<String, Object> getApiDetailsForRouteEnabledFormSubmission(Configuration configuration, String s, String versionNumber,
                                                                               String apiName, String submissionRequest, FormDetails formDetails) {

        return null;
    }

    private String getIBPMApiNameByAction(String formSubmissionAction, FormSubmission formSubmissionData) {

        return null;
    }

    private void performValidation(JSONObject inputJsonObject) {
    }

    private boolean isFormRouteEnabled(Object formDetails) {

        return false;
    }

    private HttpHeaders getPopulatedHeaders(HttpHeaders headersByToken) {

        return null;
    }

    private HttpHeaders getHeadersByTokenType(org.springframework.http.HttpHeaders httpHeaders, String tennantName) {

        return null;
    }

    private Document getTennantName(FormDetails formDetails) {
        return  null;
    }

}
