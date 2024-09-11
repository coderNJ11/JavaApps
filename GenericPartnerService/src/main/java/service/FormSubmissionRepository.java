package service;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.reactivestreams.client.MongoCollection;
import lombok.extern.slf4j.Slf4j;
import model.Submission;
import reactor.core.publisher.Mono;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Date;

@Slf4j
@Named
public class FormSubmissionRepository {

    private final MongoCollection<Submission> formSubmissionCollection;

    @Inject
    public FormSubmissionRepository(MongoCollection<Submission> formSubmissionCollection) {
        this.formSubmissionCollection = formSubmissionCollection;
    }
    public Mono<Long> updateFormSubmissionPartnerAPIStatus(String submissionId, String processed) {

        UpdateOptions updateOptions = new UpdateOptions().upsert(false);
        Date updatedDate = new Date();
        return Mono.from(formSubmissionCollection
                .updateOne(
                        Filters.eq("_formio_submissionId", submissionId),
                        Updates.combine(
                                Updates.set("processed", processed),
                                Updates.set("updatedDate", updatedDate)
                        ),
                        updateOptions
                ))
                .flatMap(updateResult -> Mono.just(updateResult.getModifiedCount()));
    }
}
