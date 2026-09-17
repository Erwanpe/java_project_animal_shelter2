package repository;

import exception.ShelterException;
import model.Volunteer;
import model.VolunteerStatus;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class VolunteerFileLoader {

    public void loadFromFile(String filePath, Repository<Volunteer> volunteerRepo) {
        try (Scanner scanner = new Scanner(new File(filePath))) {
            int lineNumber = 0;
            while (scanner.hasNextLine()) {
                lineNumber++;
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }
                try {
                    Volunteer volunteer = parseLine(line);
                    volunteerRepo.add(volunteer);
                } catch (ShelterException e) {
                    System.out.println("[volunteers.txt] Ligne " + lineNumber + " ignorée : " + e.getMessage());
                }
            }
        } catch (FileNotFoundException e) {
            throw new ShelterException("Volunteer data file not found: " + filePath);
        }
    }

    private Volunteer parseLine(String line) {
        String[] fields = line.split(";");
        if (fields.length < 5) {
            throw new ShelterException("Malformed volunteer record (expected 5 fields): " + line);
        }

        String id = fields[0].trim();
        String name = fields[1].trim();
        boolean availability = Boolean.parseBoolean(fields[2].trim());

        Set<String> skills = Arrays.stream(fields[3].split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());

        VolunteerStatus status = parseStatus(fields[4].trim(), line);

        return new Volunteer(id, name, availability, skills, status);
    }

    private VolunteerStatus parseStatus(String value, String line) {
        try {
            return VolunteerStatus.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ShelterException("Invalid volunteer status '" + value + "' in line: " + line);
        }
    }
}