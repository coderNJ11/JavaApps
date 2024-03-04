package Activities;

import io.temporal.activity.ActivityMethod;
import org.bson.Document;

public interface TemporalActivities {
    @ActivityMethod
    void storeDataProcessingResultViaUI(Document data);

    @ActivityMethod
    void processUsingAILLMModeling(Document data);

    @ActivityMethod
    void generateReportAndModel(Document data);

    @ActivityMethod
    void findHeatMap(Document data);
}
