package model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document
@Data
@Getter
@Setter
public class Notification {

    @Id
    private String id;
    private Map<String, Object> data;
    private String eventId;
    private String updateTime;
    private String createdTime;
    private String routeId;
    private Boolean processed;
    private Boolean processing;
    private String message;
    private String key;

    public Notification() {
        // Default constructor
    }

    public Notification(String id, Map<String, Object> data, String eventId, String updateTime, String createdTime) {
        this.id = id;
        this.data = data;
        this.eventId = eventId;
        this.updateTime = updateTime;
        this.createdTime = createdTime;
    }

    public String getRouteId() {
        return routeId;
    }

    public Boolean isProcessed() {
        return processed;
    }
}