package model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.inject.Named;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
@Named
public class FormDetails {

    String name;
    String id;
    String formId;
    String formName;
    String formType;
    String formVersion;
    String formVersionId;
    String formDescription;
    String formTemplate;
}
