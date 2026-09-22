package concurrent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TaskCompletionTracker {

    private final Set<String> completedTaskIds = new HashSet<>();
    private final List<String> interruptedTaskIds = new ArrayList<>();
    private final List<String> failedTaskIds = new ArrayList<>();

    public synchronized void recordCompletion(String taskId) {
        completedTaskIds.add(taskId);
    }

    public synchronized void recordInterruption(String taskId) {
        interruptedTaskIds.add(taskId);
    }

    public synchronized void recordFailure(String taskId, String reason) {
        failedTaskIds.add(taskId + " (" + reason + ")");
    }

    public synchronized int getCompletedCount() {
        return completedTaskIds.size();
    }

    public synchronized boolean isCompleted(String taskId) {
        return completedTaskIds.contains(taskId);
    }

    public synchronized List<String> getInterruptedTaskIds() {
        return new ArrayList<>(interruptedTaskIds);
    }

    public synchronized List<String> getFailedTaskIds() {
        return new ArrayList<>(failedTaskIds);
    }
}
