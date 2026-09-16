package model;

public class AdoptionApplication {

    private String id;
    private String applicantName;
    private Animal animal;
    private boolean applicantVerified;
    private boolean animalEligible;
    private boolean medicalCleared;
    private ApplicationStatus decision;

    public AdoptionApplication(String id, String applicantName, Animal animal) {
        this.id = id;
        this.applicantName = applicantName;
        this.animal = animal;
        this.applicantVerified = false;
        this.animalEligible = false;
        this.medicalCleared = false;
        this.decision = ApplicationStatus.PENDING;
    }

    public String getId() {return id;}

    public String getApplicantName() {return applicantName;}

    public Animal getAnimal() {return animal;}

    public boolean isApplicantVerified() {
        return applicantVerified;
    }

    public boolean isAnimalEligible() {
        return animalEligible;
    }

    public boolean isMedicalCleared() {
        return medicalCleared;
    }

    public ApplicationStatus getDecision() {
        return decision;
    }
}