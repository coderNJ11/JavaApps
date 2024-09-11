package model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.inject.Named;
import java.io.Serializable;
import java.util.Map;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
@Named
public class FormSubmissionRequest implements Serializable {

    private Map<String, Object> data;
    private Map<String, Object> metaData;
    private String state;
    private String action;

    @JsonProperty("_id")
    private String id;

    @JsonProperty("_fvid")
    private Integer _fvid;
}
