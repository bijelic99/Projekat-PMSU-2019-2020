package com.ftn.mailClient.dao;

import androidx.room.Dao;
import androidx.room.Query;
import com.ftn.mailClient.model.User;

@Dao
public interface UserDao extends  DaoInterface<User> {
    @Query("DELETE FROM User")
    public void deleteTable();
}
