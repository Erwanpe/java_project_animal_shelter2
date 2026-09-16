package model;

import contract.Adoptable;
import exception.ShelterException;

public abstract class Animal implements Adoptable {

    private final String id;
    private final String name;
    private final int age;
    private final int arrivalDate;
    private AnimalStatus status;
    private MedicalRecord medicalRecord;

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
}