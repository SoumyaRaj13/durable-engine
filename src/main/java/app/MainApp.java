package app;

import examples.onboarding.OnboardingWorkflow;

import java.util.Scanner;

public class MainApp {

    public static void main(String[] args) throws Exception {

        try (Scanner sc = new Scanner(System.in)) {

            System.out.print("Enter Workflow ID: ");
            String workflowId = sc.nextLine();

            System.out.print("Crash after step 1? (yes/no): ");
            String crash = sc.nextLine();

            boolean crashAfterFirst =
                    crash.equalsIgnoreCase("yes");

            OnboardingWorkflow.run(
                    workflowId,
                    crashAfterFirst
            );
        }
    }
}

