package concurrent;

import model.CareTask;

public class CareTaskWorker implements Runnable {

    private final CareTask task;
    private final TaskCompletionTracker tracker;
    private final long simulatedWorkMillis;

    public CareTaskWorker(CareTask task, TaskCompletionTracker tracker, long simulatedWorkMillis) {
        this.task = task;
        this.tracker = tracker;
        this.simulatedWorkMillis = simulatedWorkMillis;
    }

    @Override
    public void run() {
        try {
            // Simulates real shelter work: feeding, walking, a medical act...
            Thread.sleep(simulatedWorkMillis);

            // Normal completion path. markCompleted() either fully succeeds
            // (ASSIGNED -> COMPLETED) or throws before mutating anything, so
            // there is never a partially-updated CareTask visible to others.
            task.markCompleted();
            tracker.recordCompletion(task.getIdTask());

        } catch (InterruptedException e) {
            // Interruption path: we reach here only from the sleep above,
            // i.e. strictly before markCompleted() ran. The task is left
            // exactly as it was (still ASSIGNED) - nothing is lost, nothing
            // is half-applied.
            tracker.recordInterruption(task.getIdTask());
            Thread.currentThread().interrupt(); // restore the flag for the owner/JVM

        } catch (RuntimeException e) {
            // A domain rule was violated (e.g. task already completed by
            // someone else). Recorded, not thrown, so the pool keeps running.
            tracker.recordFailure(task.getIdTask(), e.getMessage());
        }
    }
}
