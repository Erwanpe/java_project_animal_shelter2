package model;

import contract.Adoptable;
import contract.Identifiable;
import exception.ShelterException;

public abstract class Animal implements Adoptable, Comparable<Animal>, Identifiable {

    private final String id;
    private final String name;
    private final int age;
    private final int arrivalDate;
    private AnimalStatus status;
    private final MedicalRecord medicalRecord;

    public Animal(String id, String name, int age, int arrivalDate, AnimalStatus status) {
        if (id == null || id.isBlank()) {
            throw new ShelterException("Animal id cannot be null or blank.");
        }
        if (status == null) {
            throw new ShelterException("Animal status cannot be null.");
        }
        this.id = id;
        this.name = name;
        this.age = age;
        this.arrivalDate = arrivalDate;
        this.status = status;
        this.medicalRecord = new MedicalRecord();
    }

    public MedicalRecord getMedicalRecord() {
        return medicalRecord;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public int getArrivalDate() {
        return arrivalDate;
    }

    public AnimalStatus getStatus() {
        return status;
    }

    protected void setStatus(AnimalStatus status) {
        this.status = status;
    }

    @Override
    public abstract boolean isAdoptionEligible();

    /**
     * Shared eligibility base used by every subtype's isAdoptionEligible():
     * an animal can only be considered for adoption if its lifecycle status
     * is AVAILABLE *and* its medical record says it's ready (vaccinations
     * up to date, not currently under treatment). This is what actually
     * links AnimalStatus and MedicalRecord together - without it, the two
     * pieces of state can silently disagree.
     */
    protected boolean isBaseEligible() {
        return status == AnimalStatus.AVAILABLE && medicalRecord.isReadyForAdoption();
    }

    @Override
    public void completeAdoption() {
        if (status == AnimalStatus.ADOPTED) {
            throw new ShelterException("Animal " + id + " has already been adopted.");
        }
        if (!isAdoptionEligible()) {
            throw new ShelterException("Animal " + id + " is not eligible for adoption.");
        }
        setStatus(AnimalStatus.ADOPTED);
    }

    @Override
    public int compareTo(Animal other) {
        return this.id.compareTo(other.id);
    }
}