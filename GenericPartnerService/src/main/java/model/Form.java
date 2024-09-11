package model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.bson.Document;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import javax.inject.Named;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
@Named
public class Form {

    @BsonProperty("id")
    @BsonId
    private ObjectId id;

    private String name;

    private String title;

    private List<String> tags;

    private Map<String,Object> properties;

    private Date lastSubmitted;

    private Boolean favourite;

    @BsonProperty("components")
    private List<Document> components;

    @BsonProperty("tennantDetails")
    private List<Document> tennantDetails;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Form form)) return false;
        return Objects.equals(getId(), form.getId()) && Objects.equals(getName(), form.getName()) && Objects.equals(getTitle(), form.getTitle());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getTitle());
    }
}
