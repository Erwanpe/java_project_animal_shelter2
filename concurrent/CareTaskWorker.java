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
            Thread.sleep(simulatedWorkMillis);

            task.markCompleted();
            tracker.recordCompletion(task.getIdTask());

        } catch (InterruptedException e) {
            tracker.recordInterruption(task.getIdTask());
            Thread.currentThread().interrupt();

        } catch (RuntimeException e) {
            tracker.recordFailure(task.getIdTask(), e.getMessage());
        }
    }
}
