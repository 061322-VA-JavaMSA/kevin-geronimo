package com.revature.dao;

import java.util.List;

public interface Dao<T> {
    T get(int id);

    List<T> getAll();

    T insert(T t);

    boolean update(T t);

    boolean delete(int id);
}
