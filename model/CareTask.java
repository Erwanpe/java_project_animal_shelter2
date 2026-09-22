package model;

import contract.Identifiable;
import exception.ShelterException;

public class CareTask implements Identifiable {
    private String idTask;
    private String description;
    private String requiredSkill;
    private String assignedWorkerId;
    private CareTaskStatus status;

    public CareTask(String idTask, String description, String requiredSkill) {
        this.idTask = idTask;
        this.description = description;
        this.requiredSkill = requiredSkill;
        this.assignedWorkerId = null;
        this.status = CareTaskStatus.UNASSIGNED;
    }

    @Override
    public String getId() {
        return idTask;
    }

    public String getIdTask() {
        return idTask;
    }

    public String getDescription() {
        return description;
    }

    public String getRequiredSkill() {
        return requiredSkill;
    }

    public String getAssignedWorkerId() {
        return assignedWorkerId;
    }

    public CareTaskStatus getStatus() {
        return status;
    }

    public void assignTo(String workerId) {
        if (status != CareTaskStatus.UNASSIGNED) {
            throw new ShelterException("Task " + idTask + " is already " + status + "; cannot be re-assigned.");
        }
        this.assignedWorkerId = workerId;
        this.status = CareTaskStatus.ASSIGNED;
    }

    public void markCompleted() {
        if (status != CareTaskStatus.ASSIGNED) {
            throw new ShelterException(
                    "Task " + idTask + " must be ASSIGNED before it can be completed (current: " + status + ").");
        }
        this.status = CareTaskStatus.COMPLETED;
    }
}