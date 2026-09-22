package test;

import concurrent.ConcurrentTaskProcessor;
import concurrent.TaskCompletionTracker;
import model.CareTask;
import model.CareTaskStatus;

import java.util.ArrayList;
import java.util.List;

public class ConcurrencyTest {

    public static void main(String[] args) {
        int passed = 0;
        int total = 0;

        total++;
        if (testAllWorkersCompleteNormally())
            passed++;
        total++;
        if (testOverrunningWorkerIsInterruptedWithoutLosingState())
            passed++;
        total++;
        if (testRepeatedRunsWithFreshThreadsStayInvariantSafe())
            passed++;

        System.out.println("\n=== ConcurrencyTest: " + passed + "/" + total + " passed ===");
        if (passed != total) {
            System.exit(1);
        }
    }

    private static boolean testAllWorkersCompleteNormally() {
        System.out.println("[TEST] All ASSIGNED tasks complete normally under concurrent workers");

        List<CareTask> tasks = buildAssignedTasks(5);
        ConcurrentTaskProcessor processor = new ConcurrentTaskProcessor();

        processor.processAssignedTasks(tasks, 100, 5000);

        TaskCompletionTracker tracker = processor.getTracker();
        boolean expected = tracker.getCompletedCount() == 5;
        for (CareTask t : tasks) {
            expected &= t.getStatus() == CareTaskStatus.COMPLETED;
        }

        report(expected, "5/5 tasks COMPLETED",
                "completed=" + tracker.getCompletedCount() + ", states=" + statesOf(tasks));
        return expected;
    }

    private static boolean testOverrunningWorkerIsInterruptedWithoutLosingState() {
        System.out.println("[TEST] Slow workers are interrupted by the owner; no task is lost or corrupted");

        List<CareTask> tasks = buildAssignedTasks(3);
        ConcurrentTaskProcessor processor = new ConcurrentTaskProcessor();

        processor.processAssignedTasks(tasks, 1000, 50);

        boolean noCorruption = true;
        for (CareTask t : tasks) {
            boolean validState = t.getStatus() == CareTaskStatus.COMPLETED || t.getStatus() == CareTaskStatus.ASSIGNED;
            noCorruption &= validState;
        }

        report(noCorruption, "every task COMPLETED or ASSIGNED (never lost/corrupted)",
                "states=" + statesOf(tasks) + ", interrupted=" + processor.getTracker().getInterruptedTaskIds());
        return noCorruption;
    }

    private static boolean testRepeatedRunsWithFreshThreadsStayInvariantSafe() {
        System.out.println("[TEST] Repeated runs with recreated threads stay invariant-safe (5 runs)");

        boolean allRunsOk = true;
        for (int run = 1; run <= 5; run++) {
            List<CareTask> tasks = buildAssignedTasks(4);
            ConcurrentTaskProcessor processor = new ConcurrentTaskProcessor(); // fresh tracker, fresh threads
            processor.processAssignedTasks(tasks, 50, 2000);

            for (CareTask t : tasks) {
                allRunsOk &= (t.getStatus() == CareTaskStatus.COMPLETED || t.getStatus() == CareTaskStatus.ASSIGNED);
            }
        }

        report(allRunsOk, "invariant holds on every one of 5 independent runs", "allRunsOk=" + allRunsOk);
        return allRunsOk;
    }

    private static List<CareTask> buildAssignedTasks(int count) {
        List<CareTask> tasks = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            CareTask t = new CareTask("CT" + i, "Simulated care task " + i, "generic");
            t.assignTo("worker-" + i); // ASSIGNED is the required starting state
            tasks.add(t);
        }
        return tasks;
    }

    private static String statesOf(List<CareTask> tasks) {
        StringBuilder sb = new StringBuilder();
        for (CareTask t : tasks) {
            sb.append(t.getIdTask()).append("=").append(t.getStatus()).append(" ");
        }
        return sb.toString().trim();
    }

    private static void report(boolean pass, String expected, String actual) {
        System.out.println("  " + (pass ? "PASS" : "FAIL") + " - expected: " + expected + " | actual: " + actual);
    }
}
