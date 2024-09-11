package model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.inject.Named;
import java.util.Map;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
@Named
public class FormSubmissionResponse {

    ProcessInstance entity;
    String _formio_submissionId;
    String _ibpm_routeId;
    String _ibpm_updatedDate;
    String _ibpm_createdDate;
    String _ibpm_completedDate;
    String _ibpm_appId;
    String _ibpm_templateId;

    private Map<String, Object> data;
    private Map<String, Object> metaData;
    private String state;
    private Integer _fvid;
    private String _id;



}
