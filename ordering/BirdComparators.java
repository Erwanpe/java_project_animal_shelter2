package ordering;

import model.Bird;

import java.util.Comparator;

public class BirdComparators {

    public static final Comparator<Bird> BY_CANFLY =
            Comparator.comparing(Bird::getCanFly)
                      .thenComparing(Bird::getId);

    private BirdComparators() {}
}