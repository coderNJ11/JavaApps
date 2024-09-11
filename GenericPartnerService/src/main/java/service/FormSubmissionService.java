package service;

import lombok.extern.slf4j.Slf4j;
import model.FormSubmission;
import model.FormSubmissionRequest;
import model.FormSubmissionResponse;
import model.IBPMServiceException;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import Exception.*;

import javax.inject.Inject;
import javax.inject.Named;

import static Exception.EFormsErrorCode.EFORMS_GENERIC_ERROR;
import static utils.GenericConstants.*;

@Named
@Slf4j
public class FormSubmissionService {

    private EFormsServiceHelper eFormServiceHelper;
    private FormIORequestHandler formIORequestHandler;

    @Inject
    public FormSubmissionService(EFormsServiceHelper eFormServiceHelper, FormIORequestHandler formIORequestHandler) {
        this.eFormServiceHelper = eFormServiceHelper;
        this.formIORequestHandler = formIORequestHandler;
    }



    public Mono<FormSubmissionResponse> handleSaveSubmissionForRouteForm(HashMap<String, Object> processRequest) {
        Mono<FormSubmissionResponse> responseMono;
        switch ((String) processRequest.get(SUBMISSION_ACTION)) {
            case SUBMISSION_DELETE_ACTION:
                responseMono = processDeleteFormSubmissionForRoute(processRequest);
                break;
            case SUBMISSION_UPDATE_ACTION:
                responseMono = processUpdateFormSubmissionForRoute(processRequest);
                break;
            case SUBMISSION_CREATE_ACTION:
                responseMono = processCreateFormSubmissionForRoute(processRequest);
                break;
            case SUBMISSION_DRAFT_ACTION:
                responseMono = processFormsSubmissionForDraftRouteForm(processRequest);
                break;
            default:
                throw new EFormBusinessServiceException(EFORMS_GENERIC_ERROR, "Invalid Submission Action");
        }
        return responseMono;
    }

    public Mono<FormSubmissionResponse> handlerSubmissionForNonRouteForm(HashMap<String, Object> processRequest) {
        Mono<FormSubmissionResponse> responseMono;
        switch ((String) processRequest.get(SUBMISSION_ACTION)) {
            case SUBMISSION_UPDATE_ACTION:
                responseMono = updateFormSubmission(processRequest, (String) processRequest.get(FORMIO_SUBMISSION_ID),
                        (HttpHeaders) processRequest.get(HEADERS))
                        .flatMap(processSubmissionRequest -> Mono.just(getFormSubmissionResponse(processSubmissionRequest)));
                break;
            case SUBMISSION_CREATE_ACTION:
                responseMono = submitFormData(processRequest, (HttpHeaders) processRequest.get(HEADERS))
                        .flatMap(processupdateRequest -> formIORequestHandler.getAdminFormIoToken(null)
                                .flatMap(adminToken -> {
                                    HttpHeaders customHeaders = new HttpHeaders();
                                    customHeaders.put(JWT_TOKEN, List.of(adminToken));
                                    return updateFormSubmission(processRequest, (String) processRequest.get(FORMIO_SUBMISSION_ID),
                                            customHeaders)
                                            .flatMap(upddateResponse -> Mono.just(getFormSubmissionResponse(upddateResponse)));
                                }));
                break;
            case SUBMISSION_DELETE_ACTION:
                responseMono = submitFormData(processRequest, (HttpHeaders) processRequest.get(HEADERS))
                        .flatMap(processupdateRequest -> formIORequestHandler.getAdminFormIoToken(null)
                                .flatMap(adminToken -> {
                                    HttpHeaders customHeaders = new HttpHeaders();
                                    customHeaders.put(JWT_TOKEN, List.of(adminToken));
                                    return processupdateSubmission(processRequest, (String) processRequest.get(FORMIO_SUBMISSION_ID),
                                            customHeaders)
                                            .flatMap(deleteResponse -> Mono.just(getFormSubmissionResponse(deleteResponse)));
                                }));
                break;

            default:
                throw new EFormBusinessServiceException(EFORMS_GENERIC_ERROR, "Invalid Submission Action");
        }
        return responseMono;
    }

    private Mono<FormSubmission> processupdateSubmission(HashMap<String, Object> apiDetails, String requestSubmissionId, HttpHeaders headers) {
        FormSubmission formSubmissionData = eFormServiceHelper.getConvertedFormSubmission((String) apiDetails.get(INPUT));
        formSubmissionData.getData().put("_formio_submissionId", requestSubmissionId);
        Mono<FormSubmission> updateFormSubmission = formIORequestHandler.updateFormSubmission((String) apiDetails.get(TENNANT_NAME),
                (String) apiDetails.get(EFORM_FORMNAME),requestSubmissionId, formSubmissionData, headers);
        return updateFormSubmission.flatMap(formSubmission -> Mono.just(formSubmission));
    }

    private FormSubmissionResponse getFormSubmissionResponse(final FormSubmission submission) {
        FormSubmissionResponse response = new FormSubmissionResponse();
        response.setData(submission.getData());
        response.setMetaData(submission.getMetaData());
        response.set_id(submission.getId());
        response.set_formio_submissionId(submission.getId());
        response.set_fvid(submission.get_fvid());
        response.setState(submission.getState());
        return response;
    }

    private Mono<HashMap<String, Object>> submitFormData(final HashMap<String, Object> apiDetails, final HttpHeaders httpHeaders) {
        FormSubmission formSubmission = eFormServiceHelper.getConvertedFormSubmission((String) apiDetails.get(INPUT));
        Mono<HashMap<String, Object>> inputForGenerateRoute = formIORequestHandler.createFormSubmission((String) apiDetails.get(TENNANT_NAME),
                (String) apiDetails.get(EFORM_FORMNAME), formSubmission, httpHeaders)
                .flatMap(submissionData -> {
                    HashMap<String, Object> apiDetailsToUpdate = (HashMap<String, Object>) apiDetails.clone();
                    apiDetailsToUpdate.put(FORMIO_SUBMISSION_ID, submissionData.getId());
                    return Mono.just(apiDetailsToUpdate);
                });
        return inputForGenerateRoute;
    }

    private Mono<FormSubmission> updateFormSubmission(final HashMap<String, Object> apiDetails, final String requestSubmissionId,
                                                      final HttpHeaders httpHeaders) {
        FormSubmission formSubmission = eFormServiceHelper.getConvertedFormSubmission((String) apiDetails.get(INPUT));
        return formIORequestHandler.updateFormSubmission((String) apiDetails.get(TENNANT_NAME),
                (String) apiDetails.get(EFORM_FORMNAME),requestSubmissionId, formSubmission, httpHeaders);
    }

    private Mono<FormSubmissionResponse> processFormsSubmissionForDraftRouteForm(HashMap<String, Object> processRequest) {
        return null;
    }

    private Mono<FormSubmissionResponse> processCreateFormSubmissionForRoute(HashMap<String, Object> processRequest) {
        return null;
    }

    private Mono<FormSubmissionResponse> processUpdateFormSubmissionForRoute(HashMap<String, Object> processRequest) {
        return null;
    }

    private Mono<FormSubmissionResponse> processDeleteFormSubmissionForRoute(HashMap<String, Object> processRequest) {
        return null;
    }


}
