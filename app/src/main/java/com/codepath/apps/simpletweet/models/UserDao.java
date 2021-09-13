package com.codepath.apps.simpletweet.models;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {

    // @Query annotation requires knowing SQL syntax
    // See http://www.sqltutorial.org/

    @Query("SELECT * FROM User WHERE id = :id")
    User byId(long id);

    @Query("SELECT * FROM User ORDER BY ID DESC LIMIT 25")
    List<User> recentItems();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public Long insertUser(User user);
}