package model;

import contract.Identifiable;

public class CareTask implements Identifiable {
    private String idTask;
    private String description;
    private String requiredSkill;
    private Volunteer assignedVolunteer;
    private CareTaskStatus status;

    public CareTask(String idTask, String description, String requiredSkill) {
        this.idTask = idTask;
        this.description = description;
        this.requiredSkill = requiredSkill;
        this.assignedVolunteer = null;
        this.status = CareTaskStatus.UNASSIGNED;
    }

    @Override
    public String getId() {
        return idTask;
    }

    public String getIdTask() {return idTask;}

    public String getDescription() {return description;}

    public String getRequiredSkill() {return requiredSkill;}

    public Volunteer getAssignedVolunteer() {return assignedVolunteer;}

    public CareTaskStatus getStatus() {return status;}

    protected void setAssignedVolunteer(Volunteer assignedVolunteer) {
        this.assignedVolunteer = assignedVolunteer;
    }

    protected void setStatus(CareTaskStatus status) {
        this.status = status;
    }

    public void markCompleted() {
        setStatus(CareTaskStatus.COMPLETED);
    }
}