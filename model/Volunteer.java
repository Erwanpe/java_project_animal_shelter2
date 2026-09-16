package model;

public class Volunteer {
    private int idVol;
    private String nameVol;
    private boolean availability;
    private String skills;
    private VolunteerStatus assignmentStatus;

    public Volunteer(int idVol, String nameVol, boolean availability, String skills, VolunteerStatus assignmentStatus){
        this.idVol = idVol;
        this.nameVol = nameVol;
        this.availability = availability;
        this.skills = skills;
        this.assignmentStatus = assignmentStatus;
    }

    public int getIdVolunteer(){return idVol;}

    public String getNameVolunteer(){return nameVol;}

    public boolean getAvailibility(){return availability;}

    public String getSkills(){return skills;}

    public VolunteerStatus getAssignmenStatus(){return assignmentStatus;}
}