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
public class ProcessInsatnceInfo {
    private String processId;
    private String processName;
    private String processType;
    private String templateId;
    private String templateName;
}
