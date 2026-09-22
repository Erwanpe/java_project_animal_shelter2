package report;

import model.Animal;
import model.AnimalStatus;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AnimalReportService {

    public List<Animal> sortedBy(List<Animal> animals, Comparator<Animal> comparator) {
        return animals.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    public List<Animal> filterByStatus(List<Animal> animals, AnimalStatus status) {
        return animals.stream()
                .filter(a -> a.getStatus() == status)
                .collect(Collectors.toList());
    }

    public List<Animal> filterByType(List<Animal> animals, Class<? extends Animal> type) {
        return animals.stream()
                .filter(type::isInstance)
                .collect(Collectors.toList());
    }

    public List<Animal> filterByAgeRange(List<Animal> animals, int minAgeWeeks, int maxAgeWeeks) {
        return animals.stream()
                .filter(a -> a.getAge() >= minAgeWeeks && a.getAge() <= maxAgeWeeks)
                .collect(Collectors.toList());
    }

    public List<Animal> filterByArrivalOnOrAfter(List<Animal> animals, int arrivalDate) {
        return animals.stream()
                .filter(a -> a.getArrivalDate() >= arrivalDate)
                .collect(Collectors.toList());
    }

    public List<Animal> filterByMedicalReadiness(List<Animal> animals, boolean ready) {
        return animals.stream()
                .filter(a -> a.getMedicalRecord().isReadyForAdoption() == ready)
                .collect(Collectors.toList());
    }

    public List<Animal> adoptableAnimals(List<Animal> animals) {
        return animals.stream()
                .filter(Animal::isAdoptionEligible)
                .collect(Collectors.toList());
    }

    public Map<AnimalStatus, Long> countByStatus(List<Animal> animals) {
        return animals.stream()
                .collect(Collectors.groupingBy(Animal::getStatus, Collectors.counting()));
    }

    public Map<String, Long> countBySpecies(List<Animal> animals) {
        return animals.stream()
                .collect(Collectors.groupingBy(a -> a.getClass().getSimpleName(), Collectors.counting()));
    }
}
