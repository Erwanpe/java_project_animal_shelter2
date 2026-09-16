package repository;

import contract.Identifiable;
import exception.ShelterException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class Repository<T extends Identifiable> {

    private final Map<String, T> storage;

    public Repository() {
        this.storage = new HashMap<>();
    }

    public void add(T item) {
        if (item == null) {
            throw new ShelterException("Cannot add a null item to the repository.");
        }
        if (storage.containsKey(item.getId())) {
            throw new ShelterException("Duplicate id: " + item.getId());
        }
        storage.put(item.getId(), item);
    }

    public T findById(String id) {
        T item = storage.get(id);
        if (item == null) {
            throw new ShelterException("No item found with id: " + id);
        }
        return item;
    }

    public List<T> getAll() {
        return Collections.unmodifiableList(new ArrayList<>(storage.values()));
    }

    // Nouvelle méthode générique de recherche
    public List<T> filter(Predicate<T> condition) {
        List<T> result = new ArrayList<>();
        for (T item : storage.values()) {
            if (condition.test(item)) {
                result.add(item);
            }
        }
        return result;
    }
} 