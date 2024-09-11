package service;

import model.FormDetails;
import model.FormSubmission;
import model.FormSubmissionRequest;
import model.FormSubmissionResponse;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Mono;

import java.util.Optional;

public class FormIORequestHandler {
    public Mono<FormDetails> getFormDetailsById(String formId) {

        return null;
    }

    public Mono<FormSubmissionRequest> createFormSubmission(final String tennantName, final String formId,
                                                            final FormSubmission formSubmission, final HttpHeaders httpHeaders) {

        return null;
    }

    public Mono<String> getAdminFormIoToken(final String groups) {

        return null;
    }

    public Mono<FormSubmission> updateFormSubmission(String te, String formName, String submissionId, FormSubmission formSubmission, HttpHeaders headers) {
        return null;
    }
}
