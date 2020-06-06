package com.ftn.mailClient.dao;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;
import com.ftn.mailClient.model.Identifiable;

public interface DaoInterface<T extends Identifiable> {
    @Insert
    void insert(T value);

    @Update
    void update(T value);

    @Delete
    void delete(T value);


}
