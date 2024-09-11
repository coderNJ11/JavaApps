package model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.inject.Named;

@Getter
@Setter
@Named
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
public class APIMessageMapping {
    String receivedMessage;
    String customMessage;
}
