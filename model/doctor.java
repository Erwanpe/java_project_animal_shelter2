package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class doctor {
    private final String id_doctor;
    private final String name;
    private final String specialization;
    private final List performedTreatments;

    public doctor(String id_doctor, String name, String specialization) {
        if (id_doctor == null || id_doctor.isBlank()) {
            throw new IllegalArgumentException("Doctor_id is required.");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("The doctor's name is required.");
        }
        if (specialization == null || specialization.isBlank()) {
            throw new IllegalArgumentException("The doctor's specialization is required.");
        }

        this.id_doctor = id_doctor;
        this.name = name;
        this.specialization = specialization;
        this.performedTreatments = new ArrayList<>();
    }

    public String getId() {
        return id_doctor;
    }

    public String getName() {
        return name;
    }

    public String getSpecialization() {
        return specialization;
    }

    /**
     * Renvoie une vue non modifiable de l'historique des traitements (Encapsulation).
     */
    public List getPerformedTreatments() {
        return Collections.unmodifiableList(performedTreatments);
    }


    /**
     * Enregistre un acte de soin réalisé sur un animal.
     */
    public void performTreatment(String animalId, String treatmentDescription) {
        if (animalId == null || animalId.isBlank()) {
            throw new IllegalArgumentException("The animal's ID is required.");
        }
        if (treatmentDescription == null || treatmentDescription.isBlank()) {
            throw new IllegalArgumentException("The treatment description is required.");
        }

        String record = "Animal [" + animalId + "] : " + treatmentDescription;
        this.performedTreatments.add(record);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        doctor doctor = (doctor) o;
        return Objects.equals(id_doctor, doctor.id_doctor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_doctor);
    }

    @Override
    public String toString() {
        return "Dr. " + name + " (ID: " + id_doctor + ", Spécialité: " + specialization + ")";
    }
}