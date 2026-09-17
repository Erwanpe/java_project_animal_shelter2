package model;

public class MedicalRecord {

    private boolean vaccinationUpToDate;
    private boolean underTreatment;
    private String notes;

    public MedicalRecord() {
        this.vaccinationUpToDate = false;
        this.underTreatment = false;
        this.notes = "";
    }

    public boolean isVaccinationUpToDate() {
        return vaccinationUpToDate;
    }

    public void setVaccinationUpToDate(boolean vaccinationUpToDate) {
        this.vaccinationUpToDate = vaccinationUpToDate;
    }

    public boolean isUnderTreatment() {
        return underTreatment;
    }

    public void setUnderTreatment(boolean underTreatment) {
        this.underTreatment = underTreatment;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isReadyForAdoption() {
        return vaccinationUpToDate && !underTreatment;
    }
}