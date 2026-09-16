package report;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

import model.Animal;

public class ShelterPopulation implements Iterable<Animal> {

    private final List<Animal> animals;

    public ShelterPopulation() {
        this.animals = new ArrayList<>();
    }

    public void add(Animal animal) {
        animals.add(animal);
    }

    @Override
    public Iterator<Animal> iterator() {
        return animals.iterator();
    }
}