package concurrent;

import model.CareTask;
import model.CareTaskStatus;

import java.util.ArrayList;
import java.util.List;

public class ConcurrentTaskProcessor {

    private final TaskCompletionTracker tracker = new TaskCompletionTracker();

    public TaskCompletionTracker getTracker() {
        return tracker;
    }

    /**
     * Processes every ASSIGNED task in the given list concurrently.
     *
     * @param tasks               candidate tasks (only ASSIGNED ones are processed)
     * @param simulatedWorkMillis how long each worker "works" before finishing
     * @param joinTimeoutMillis   how long the owner waits per worker before
     *                            interrupting it
     */
    public void processAssignedTasks(List<CareTask> tasks, long simulatedWorkMillis, long joinTimeoutMillis) {
        List<CareTask> assigned = new ArrayList<>();
        for (CareTask t : tasks) {
            if (t.getStatus() == CareTaskStatus.ASSIGNED) {
                assigned.add(t);
            }
        }

        List<Thread> workers = new ArrayList<>();
        for (CareTask task : assigned) {
            Thread worker = new Thread(
                    new CareTaskWorker(task, tracker, simulatedWorkMillis),
                    "CareTaskWorker-" + task.getIdTask());
            workers.add(worker);
            worker.start();
        }

        for (Thread worker : workers) {
            try {
                worker.join(joinTimeoutMillis);
                if (worker.isAlive()) {
                    worker.interrupt();
                    worker.join(200);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
