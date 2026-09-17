package ordering;

import model.Dog;

import java.util.Comparator;

public class DogComparators {

    public static final Comparator<Dog> BY_BREED =
            Comparator.comparing(Dog::getBreed)
                      .thenComparing(Dog::getId);

    private DogComparators() {}
}