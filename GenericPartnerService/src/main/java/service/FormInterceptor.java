package service;

import Exception.EFormBusinessServiceException;
import Exception.EFormsErrorCode;
import lombok.extern.slf4j.Slf4j;
import model.*;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.json.JSONObject;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;
import utils.GenericConstants;

import javax.inject.Inject;
import javax.inject.Named;
import java.net.http.HttpHeaders;
import java.util.HashMap;
import java.util.Optional;

import static org.springframework.util.StringUtils.isEmpty;
import static utils.GenericConstants.*;


@Named
@Slf4j
public class FormInterceptor {

    private final EFormsServiceHelper eFormsServiceHelper;
    private final FormIORequestHandler formIORequestHandler;

    private ConfigurationDataService configurationDataService;


    @Inject
    public FormInterceptor(EFormsServiceHelper eFormsServiceHelper, FormIORequestHandler formIORequestHandler, ConfigurationDataService configurationDataService) {
        this.eFormsServiceHelper = eFormsServiceHelper;
        this.formIORequestHandler = formIORequestHandler;
        this.configurationDataService = configurationDataService;
    }


    public FormSubmission loadFormFromMongo(String formSubmissionId) {
        // Implement form loading logic from Mongo
        return null;
    }

    public Mono<HashMap<String, Object>> loadFormSubmissionAndConfiguration(final ServerRequest request) {
        return request.bodyToMono(String.class)
                .flatMap(submissionRequest -> {
                    JSONObject inputJsonObject = eFormsServiceHelper.getInputJsonObject(submissionRequest);
                    performValidation(inputJsonObject);
                    final String serviceName = "ibpm";
                    final Optional<String> resourceName = Optional.of("route");
                    FormSubmission formSubmissionData = eFormsServiceHelper.getConvertedFormSubmission(submissionRequest);
                    String formId = (String) formSubmissionData.getData().get("formId");
                    String requestSubmissionId = StringUtils.defaultIfEmpty((String) formSubmissionData.getData().get("_formio_submissionId"), "");

                    return formIORequestHandler.getFormDetailsById(formId).
                            switchIfEmpty(Mono.error(() ->
                                    new EFormBusinessServiceException(EFormsErrorCode.EFORMS_GENERIC_ERROR, "Form not found for formId : " + formId)))
                            .flatMap(formDetails -> {
                                String tennantName = (String) getTennantName(formDetails).get("name");
                                HttpHeaders headersByToken = getHeadersByTokenType(request.headers().asHttpHeaders(), tennantName);
                                HttpHeaders headers = getPopulatedHeaders(headersByToken);

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
                                    String versionNumber = StringUtils.defaultIfEmpty((String) formSubmissionData.getData().get("version"), "");
                                    final String apiName = getIBPMApiNameByAction(formSubmissionAction, formSubmissionData);

                                    Mono<Configuration> configurationMono = configurationDataService.getConfigurationResourceSpec(serviceName, resourceName.get(), versionNumber.toLowerCase(),
                                                    apiName)
                                            .switchIfEmpty(Mono.error(() ->
                                                    new EFormBusinessServiceException(EFormsErrorCode.EFORMS_GENERIC_ERROR, "Configuration not found for apiName : " + apiName)));

                                    return configurationMono.map(configuration -> {
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
                                }
                            });

                });
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