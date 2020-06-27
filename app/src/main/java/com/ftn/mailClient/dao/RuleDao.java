package com.ftn.mailClient.dao;

import androidx.room.Dao;
import androidx.room.Query;
import com.ftn.mailClient.model.Rule;

@Dao
public interface RuleDao extends DaoInterface<Rule> {
    @Query("DELETE FROM Rule")
    public void deleteTable();
}
