package repository;

import exception.ShelterException;
import model.Animal;
import model.AnimalStatus;
import model.Bird;
import model.Cat;
import model.Dog;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class AnimalFileLoader {

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
                    Animal animal = parseLine(line);
                    animalRepo.add(animal);
                } catch (ShelterException e) {
                    System.out.println("[animals.txt] Ligne " + lineNumber + " ignorée : " + e.getMessage());
                }
            }
        } catch (FileNotFoundException e) {
            throw new ShelterException("Animal data file not found: " + filePath);
        }
    }

    private Animal parseLine(String line) {
        String[] fields = line.split(";");
        if (fields.length < 7) {
            throw new ShelterException("Malformed animal record (expected 7 fields): " + line);
        }

        String type = fields[0].trim().toUpperCase();
        String id = fields[1].trim();
        String name = fields[2].trim();
        int age = parseInt(fields[3].trim(), line);
        int arrivalDate = parseInt(fields[4].trim(), line);
        AnimalStatus status = parseStatus(fields[5].trim(), line);
        String extra = fields[6].trim();

        return switch (type) {
            case "DOG" -> new Dog(id, name, age, arrivalDate, status, extra);
            case "CAT" -> new Cat(id, name, age, arrivalDate, status, Boolean.parseBoolean(extra));
            case "BIRD" -> new Bird(id, name, age, arrivalDate, status, Boolean.parseBoolean(extra));
            default -> throw new ShelterException("Unknown animal type '" + type + "' in line: " + line);
        };
    }

    private int parseInt(String value, String line) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new ShelterException("Invalid number in line: " + line);
        }
    }

    private AnimalStatus parseStatus(String value, String line) {
        try {
            return AnimalStatus.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ShelterException("Invalid status '" + value + "' in line: " + line);
        }
    }
}