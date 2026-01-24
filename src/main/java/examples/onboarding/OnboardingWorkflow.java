package examples.onboarding;

import engine.DurableContext;
import engine.StepExecutor;

import java.util.concurrent.CompletableFuture;

public class OnboardingWorkflow {

    public static void run(
            String workflowId,
            boolean crashAfterFirst
    ) throws Exception {

        DurableContext ctx =
                new DurableContext(workflowId);

        // Step 1: Create record
        String empId =
                StepExecutor.step(ctx,
                        "create-record",
                        () -> {
                            Thread.sleep(1000);
                            return "EMP-"
                                    + System.currentTimeMillis();
                        });

        // Simulate crash
        if (crashAfterFirst) {
            System.out.println(
                    "Crashing after Step 1..."
            );
            System.exit(0);
        }

        // Step 2: Laptop (parallel)
        CompletableFuture<String> laptop =
                CompletableFuture.supplyAsync(() -> {
                    try {
                        return StepExecutor.step(
                                ctx,
                                "assign-laptop",
                                () -> {
                                    Thread.sleep(1500);
                                    return "Laptop Assigned";
                                });
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

        // Step 3: Access (parallel)
        CompletableFuture<String> access =
                CompletableFuture.supplyAsync(() -> {
                    try {
                        return StepExecutor.step(
                                ctx,
                                "give-access",
                                () -> {
                                    Thread.sleep(1500);
                                    return "Access Granted";
                                });
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

        CompletableFuture
                .allOf(laptop, access)
                .join();

        // Step 4: Email
        StepExecutor.step(
                ctx,
                "send-email",
                () -> {
                    System.out.println(
                            "Welcome email sent to "
                                    + empId
                    );
                    return "Mail Sent";
                });

        System.out.println(
                "Workflow Completed"
        );
    }
}
