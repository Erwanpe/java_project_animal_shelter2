package ordering;

import model.Animal;

import java.util.Comparator;

public class AnimalComparators {

    public static final Comparator<Animal> BY_ARRIVAL_DATE =
            Comparator.comparingInt(Animal::getArrivalDate)
                      .thenComparing(Animal::getId);

    public static final Comparator<Animal> BY_AGE =
            Comparator.comparingInt(Animal::getAge)
                      .thenComparing(Animal::getId);

    public static final Comparator<Animal> BY_NAME =
            Comparator.comparing(Animal::getName)
                      .thenComparing(Animal::getId);

    private AnimalComparators() {}
}