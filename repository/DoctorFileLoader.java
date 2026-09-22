package repository;

import exception.ShelterException;
import model.Doctor;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class DoctorFileLoader {

    public void loadFromFile(String filePath, Repository<Doctor> doctorRepo) {
        try (Scanner scanner = new Scanner(new File(filePath))) {

            int lineNumber = 0;

            while (scanner.hasNextLine()) {
                lineNumber++;

                String line = scanner.nextLine().trim();

                if (line.isEmpty()) {
                    continue;
                }

                try {
                    applyLine(line, doctorRepo);

                } catch (ShelterException e) {
                    System.out.println(
                            "[Doctors.txt] Ligne "
                                    + lineNumber
                                    + " ignorée : "
                                    + e.getMessage()
                    );
                }
            }

        } catch (FileNotFoundException e) {
            throw new ShelterException(
                    "Doctor data file not found: " + filePath
            );
        }
    }

    private void applyLine(String line, Repository<Doctor> doctorRepo) {

        String[] fields = line.split(";");

        if (fields.length < 3) {
            throw new ShelterException(
                    "Malformed doctor (expected 3 fields): " + line
            );
        }

        String id = fields[0].trim();
        String name = fields[1].trim();
        String specialization = fields[2].trim();

        Doctor doctor = new Doctor(
                id,
                name,
                specialization
        );

        doctorRepo.add(doctor);
    }
}