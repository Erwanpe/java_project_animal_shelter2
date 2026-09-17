package repository;

import exception.ShelterException;
import model.Animal;
import model.MedicalRecord;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class MedicalRecordFileLoader {

    public void loadFromFile(String filePath, Repository<Animal> animalRepo) {
        try (Scanner scanner = new Scanner(new File(filePath))) {
            int lineNumber = 0;
            while (scanner.hasNextLine()) {
                lineNumber++;
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }
                try {
                    applyLine(line, animalRepo);
                } catch (ShelterException e) {
                    System.out.println("[medical_records.txt] Ligne " + lineNumber + " ignorée : " + e.getMessage());
                }
            }
        } catch (FileNotFoundException e) {
            throw new ShelterException("Medical record data file not found: " + filePath);
        }
    }

    private void applyLine(String line, Repository<Animal> animalRepo) {
        String[] fields = line.split(";");
        if (fields.length < 4) {
            throw new ShelterException("Malformed medical record (expected 4 fields): " + line);
        }

        String animalId = fields[0].trim();
        boolean vaccinationUpToDate = Boolean.parseBoolean(fields[1].trim());
        boolean underTreatment = Boolean.parseBoolean(fields[2].trim());
        String notes = fields[3].trim();

        Animal animal = animalRepo.findById(animalId); // throws ShelterException if unknown

        MedicalRecord record = animal.getMedicalRecord();
        record.setVaccinationUpToDate(vaccinationUpToDate);
        record.setUnderTreatment(underTreatment);
        record.setNotes(notes);
    }
}