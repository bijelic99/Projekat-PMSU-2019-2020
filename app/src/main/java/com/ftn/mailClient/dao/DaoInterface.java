package com.ftn.mailClient.dao;

import androidx.room.*;
import com.ftn.mailClient.model.Identifiable;

import java.util.List;

@Dao
public interface DaoInterface<T extends Identifiable> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(T value);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<T> values);

    @Update
    void update(T value);

    @Delete
    void delete(T value);


}
