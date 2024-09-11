package service;

import model.APIDetails;
import model.Configuration;
import model.FormSubmission;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;
import reactor.util.function.Tuple2;

import java.util.Map;


public class EFormsServiceHelper {

    public Tuple2<Map, Map> getFalttenedJsonData(String inputData) {
        return null;
    }


    public JSONObject getInputJsonObject(String submissionRequest) {

        return null;
    }

    public FormSubmission getConvertedFormSubmission(String submissionRequest) {

        return null;
    }

    public String getFormSubmissionAction(String requestSubmissionId, JSONObject inputJsonObject) {

        return null;
    }

    public MultiValueMap<String,String> getHeaders(HttpHeaders headers) {

        return null;
    }


    public APIDetails getApiDetails(Configuration emailConfig, String serviceName, String resourceName, String lowerCase, String apiName) {

        return null;
    }

    public APIDetails geApiConfigDetails(Configuration emailConfig, String serviceName, String resourceName, String lowerCase, String apiName) {
        return null;
    }
}
