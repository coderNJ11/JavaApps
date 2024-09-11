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
public class ResourceVersion {
    String version;
    String url;
    Authentication.AuthType authType;
    List<ResourceAction> actions;
}
