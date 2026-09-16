package model;

public class MedicalRecord {

    private boolean vaccinationUpToDate;
    private boolean underTreatment;
    private String notes;

    public MedicalRecord(boolean vaccinationUpToDate, boolean underTreatment, String notes) {
        this.vaccinationUpToDate = vaccinationUpToDate;
        this.underTreatment = underTreatment;
        this.notes = notes ;
    }

    public boolean getIsVaccinationUpToDate() {
        return vaccinationUpToDate;
    }

    public boolean getUnderTreatment(){
        return underTreatment;
    }

    public String getNotes(){
        return notes;
    }
}