package model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class FormSubmission {


    @JsonProperty(value = "_id")
    private String id;


    @JsonProperty(value = "metaData")
    private Map<String, Object> metaData;


    @JsonProperty(value = "data")
    private Map<String, Object> data;


    @JsonProperty(value = "state")
    private String state;


    @JsonProperty(value = "_fvid")
    private Integer _fvid;


}
