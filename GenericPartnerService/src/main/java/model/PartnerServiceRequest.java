package model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.inject.Named;
import java.util.Map;

@Setter
@Getter
@Named
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
public class PartnerServiceRequest {

    @JsonProperty(value = "data")
    private Map<String, Object> data;
}
