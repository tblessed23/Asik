package com.msbs.android.asik.loggingin;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.msbs.android.asik.model.Story;
import com.msbs.android.asik.model.User;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM user")
    LiveData<List<User>> loadAllTasks();

    @Query("SELECT * FROM user WHERE userId = :id")
    LiveData<User> loadStoryById(String id);

    @Insert
    void insertTask(User user);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTask(User taskEntry);

    @Delete
    void deleteTask(User taskEntry);

    @Query("SELECT * FROM user WHERE userId = :userId")
    LiveData<List<User>> loadTaskById(String userId);


    //Query UserEditDetails
    @Query("SELECT * FROM user WHERE primaryId = :primaryId")
    LiveData<User> loadUserEditById(int primaryId);

    //Querying UserDisplayDetails//
    @Query("SELECT * FROM user WHERE userId = :userId")
    LiveData<List<User>> loadUserDetailsById(String userId);
}


