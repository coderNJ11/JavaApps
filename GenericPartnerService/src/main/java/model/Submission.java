package model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import javax.inject.Named;
import java.util.Date;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
@Named
public class Submission {

    @BsonProperty("_id")
    @BsonId
    private ObjectId id;

    @BsonProperty("form")
    private ObjectId form;

    @BsonProperty("lastSubmissionDate")
    private String lastSubmissionDate;

    @BsonProperty("created")
    private Date created;

    @BsonProperty("project")
    private ObjectId project;

}
