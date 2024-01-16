package ua.foxmided.foxstudent103852.schoolappspring.service.interfaces;

import java.util.List;
import java.util.Optional;

public abstract interface CrudService<T, U> {
    T add(T t);

    Optional<T> get(U u);

    List<T> getAll();

    Optional<T> update(T t);

    Optional<T> delete(T t);
}
