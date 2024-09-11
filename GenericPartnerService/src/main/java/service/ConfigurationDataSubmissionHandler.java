package service;


import lombok.extern.slf4j.Slf4j;
import model.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import javax.inject.Inject;
import javax.inject.Named;

import java.net.http.HttpHeaders;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static utils.GenericConstants.ROUTE_ENABLED;

@Slf4j
@Named
public class ConfigurationDataSubmissionHandler {

    FormInterceptor formInterceptor;
    FormSubmissionService formSubmissionService;

    PartnerService partnerService;
    FormSubmissionRepository formSubmissionRepository;

    @Inject
    public ConfigurationDataSubmissionHandler(FormInterceptor formInterceptor, FormSubmissionService formSubmissionService,
                                              PartnerService partnerService, Form) {
        this.formInterceptor = formInterceptor;
        this.formSubmissionService = formSubmissionService;
        this.partnerService = partnerService;
    }

    public Mono<ServerResponse> processConfigurationDataSubmission(ServerRequest request) {
        Mono<HashMap<String, Object>> requestDetails = formInterceptor.loadFormSubmissionAndConfiguration(request);

        return requestDetails.flatMap(processRequest -> {
                    Mono<Tuple2<PartnerApiResponse, APIDetails>> partnerAPICall = Mono.just(Tuples.of(null, null)); // Initialize with empty values

                    if (TRUE.equals(processRequest.get(ROUTE_ENABLED))) {
                        log.warn("processFormSubmission :: end ", request.headers().asHttpHeaders().getFirst("X-Apple-Request-ID"));
                    } else if (FALSE.equals(processRequest.get(ROUTE_ENABLED))) {
                        log.warn("processFormSubmission :: end ", request.headers().asHttpHeaders().getFirst("X-Apple-Request-ID"));
                    } else {
                        throw new IBPMServiceException("EFORMS_GENERIC_ERROR");
                    }

                    // Call the partner service asynchronously for both TRUE and FALSE cases
                    partnerAPICall = partnerService.invokePartnerAPI(partnerServiceRequest, headers, payLoad, reqData);

                    return partnerAPICall.flatMap(partnerResponse -> {
                        // Handle partner service response and continue processing processRequest
                        if (TRUE.equals(processRequest.get(ROUTE_ENABLED))) {
                            return formSubmissionService.handleSaveSubmissionForRouteForm(processRequest)
                                    .flatMap(response -> handleUploadFiles(processRequest, response))
                                    .flatMap(response -> handleDeleteFiles(processRequest, response))
                                    .doOnSuccess(formSubmissionResponse -> formSubmissionPostProcess(processRequest, formSubmissionResponse));
                        } else if (FALSE.equals(processRequest.get(ROUTE_ENABLED))) {
                            return formSubmissionService.handlerSubmissionForNonRouteForm(processRequest)
                                    .flatMap(response -> handleUploadFiles(processRequest, response))
                                    .flatMap(response -> handleDeleteFiles(processRequest, response))
                                    .doOnSuccess(formSubmissionResponse -> formSubmissionPostProcess(processRequest, formSubmissionResponse));
                        } else {
                            throw new IBPMServiceException("EFORMS_GENERIC_ERROR");
                        }
                    });
                }).flatMap(response -> ServerResponse.ok().bodyValue(response))
                .onErrorResume(Mono::error);
    }


    public Mono<Long> executePartnerService(final PartnerServiceRequest partnerServiceRequest, HttpHeaders headers, String payLoad, Map<String, Object> reqData) {
        Mono<Long> partnerAPICall = updateFormSubmissionPartnerAPIStatusToProcessing(partnerServiceRequest.getFormSubmissionData())
                .then(partnerService.invokePartnerAPI(partnerServiceRequest, headers, payLoad, reqData)
                        .onErrorResume(e -> {
                            updateFormSubmissionPartnerAPIStatusToFailed(partnerServiceRequest.getFormSubmissionData());
                            return Mono.error(e);
                        })
                        .doOnSuccess(response -> updateFormSubmissionPartnerAPIStatusToProcessed(partnerServiceRequest.getFormSubmissionData()))
                )
                .subscribeOn(Schedulers.elastic()); // Run on a separate thread

        return partnerAPICall;
    }



    private Mono<Long> updateFormSubmissionPartnerAPIStatusToFailed(FormSubmission formSubmissionData) {
        String submissionId = StringUtils.defaultIfEmpty((String) formSubmissionData.getData().get("_formio_submissionId"), "");
        return formSubmissionRepository.updateFormSubmissionPartnerAPIStatus(submissionId,"Failed");
    }

    private Mono<Long> updateFormSubmissionPartnerAPIStatusToProcessed(FormSubmission formSubmissionData) {
        String submissionId = StringUtils.defaultIfEmpty((String) formSubmissionData.getData().get("_formio_submissionId"), "");
        return formSubmissionRepository.updateFormSubmissionPartnerAPIStatus(submissionId,"Processed");
    }

    private Mono<Long> updateFormSubmissionPartnerAPIStatusToProcessing(FormSubmission formSubmissionData) {
        String submissionId = StringUtils.defaultIfEmpty((String) formSubmissionData.getData().get("_formio_submissionId"), "");
        return formSubmissionRepository.updateFormSubmissionPartnerAPIStatus(submissionId,"Processing");
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
}
