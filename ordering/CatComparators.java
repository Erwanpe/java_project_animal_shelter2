package ordering;

import model.Cat;

import java.util.Comparator;

public class CatComparators {

    public static final Comparator<Cat> BY_INDOORONLY =
            Comparator.comparing(Cat::getIndoorOnly)
                      .thenComparing(Cat::getId);

    private CatComparators(){}
}
