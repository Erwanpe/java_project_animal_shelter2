package model;

import contract.Identifiable;

import java.util.Set;

public class Volunteer implements Identifiable {
    private String idVol;
    private String nameVol;
    private boolean availability;
    private Set<String> skills;
    private VolunteerStatus assignmentStatus;

    public Volunteer(String idVol, String nameVol, boolean availability, Set<String> skills, VolunteerStatus assignmentStatus){
        this.idVol = idVol;
        this.nameVol = nameVol;
        this.availability = availability;
        this.skills = skills;
        this.assignmentStatus = assignmentStatus;
    }

    @Override
    public String getId() {
        return idVol;
    }

    public String getIdVolunteer(){return idVol;}

    public String getNameVolunteer(){return nameVol;}

    public boolean getAvailability(){return availability;}

    public Set<String> getSkills(){return skills;}

    public VolunteerStatus getAssignmentStatus(){return assignmentStatus;}

    protected void setAssignmentStatus(VolunteerStatus assignmentStatus) {
        this.assignmentStatus = assignmentStatus;
    }
}