package model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.inject.Named;
import java.util.List;

@Getter
@Setter
@Named
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
public class APIExceptionConfig {
    List<Integer> errorSattusCodeSendingJsonResponse;
    List<Integer> errorMessagesToSendingSuccessStatus;
    List<APIMessageMapping> customResponseMessageMapping;
    List<APIStatusCodeMapping> errorResponseStatusCodeMapping;
    String joltSpecForSendAsSuccessRespoonse;
    String joltSpecForSendAsErrorResponse;
    Boolean errorResponseTransfrmation;
}
