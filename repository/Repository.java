package repository;

import contract.Identifiable;
import exception.ShelterException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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

    public boolean existsById(String id) {
        return storage.containsKey(id);
    }

    public List<T> getAll() {
        return Collections.unmodifiableList(new ArrayList<>(storage.values()));
    }

    public void remove(String id) {
        if (!storage.containsKey(id)) {
            throw new ShelterException("No item found with id: " + id);
        }
        storage.remove(id);
    }

    public List<T> filter(Predicate<T> condition) {
        return storage.values().stream()
                .filter(condition)
                .collect(Collectors.toList());
    }
}