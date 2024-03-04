package TemporalWorkflow;

import io.temporal.workflow.SignalMethod;
import io.temporal.workflow.WorkflowMethod;

public interface TemporalWorkflow {

    @WorkflowMethod
    void executeWorkflow();

    @SignalMethod
    void parseMongoDBSignal(String signalName);
}
