package engine;

import java.util.concurrent.atomic.AtomicInteger;

public class DurableContext {

    private final String workflowId;
    private final AtomicInteger counter =
            new AtomicInteger(0);

    public DurableContext(String workflowId) {
        this.workflowId = workflowId;
    }

    public String nextStepKey(String id) {
        return id + "-"
                + counter.incrementAndGet();
    }

    public String getWorkflowId() {
        return workflowId;
    }
}
