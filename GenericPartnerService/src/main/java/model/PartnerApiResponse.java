package model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.inject.Named;

@Setter
@Getter
@Named
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
public class PartnerApiResponse {
    String body;
    Boolean transform;
}
