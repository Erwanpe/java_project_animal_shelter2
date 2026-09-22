package ordering;

import model.Animal;
import model.Dog;

import java.util.Comparator;

public class AnimalComparators {

    public static final Comparator<Animal> BY_ID =
            Comparator.comparing(Animal::getId);

    public static final Comparator<Animal> BY_ARRIVAL_DATE =
            Comparator.comparingInt(Animal::getArrivalDate)
                      .thenComparing(Animal::getId);

    public static final Comparator<Animal> BY_AGE =
            Comparator.comparingInt(Animal::getAge)
                      .thenComparing(Animal::getId);

    public static final Comparator<Animal> BY_NAME =
            Comparator.comparing(Animal::getName)
                      .thenComparing(Animal::getId);

    /** Groups animals by species (Bird / Cat / Dog, alphabetically), then by id. */
    public static final Comparator<Animal> BY_SPECIES =
            Comparator.<Animal, String>comparing(a -> a.getClass().getSimpleName())
                      .thenComparing(Animal::getId);

    /**
     * Groups animals by species first, then - within the Dog group only -
     * orders them by breed ("race"), then falls back to id as the final
     * deterministic tiebreak. Cat and Bird currently have no equivalent
     * "race" field, so they simply order by id inside their own species
     * group; extend raceKey(...) if a race-like field is added to them.
     */
    public static final Comparator<Animal> BY_SPECIES_THEN_RACE =
            Comparator.<Animal, String>comparing(a -> a.getClass().getSimpleName())
                      .thenComparing(AnimalComparators::raceKey)
                      .thenComparing(Animal::getId);

    private static String raceKey(Animal animal) {
        if (animal instanceof Dog dog) {
            return dog.getBreed() == null ? "" : dog.getBreed();
        }
        return "";
    }

    private AnimalComparators() {}
}