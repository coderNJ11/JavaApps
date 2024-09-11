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
public class Authentication {

    AuthType authType;
    String a3Context;
    String a3Secret;
    String hmacSecret;
    String hmacSecretVersion;
    String a3ReceiverAppId;

    public enum AuthType {
        HMAC,
        A3
    }
}
