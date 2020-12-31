package com.msbs.android.asik.model;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface StoryDao {

    @Query("SELECT * FROM story ORDER BY updated_at")
    LiveData<List<Story>> loadAllStories();

    @Query("SELECT * FROM story")
    Story loadStoryObject();

    @Query("SELECT * FROM story where storystate LIKE  :query " + "OR storycity LIKE :query order by storystate")
    DataSource.Factory<Integer, Story> loadAllStoriesFromSearch(String query);

    @Query("SELECT * FROM story order by storystate")
    DataSource.Factory<Integer, Story> loadAllStoryView();

    @Insert
    void insertTask(Story storyEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTask(Story storyEntry);

    @Delete
    void deleteTask(Story story);

    @Query("SELECT * FROM story WHERE primaryId = :id")
    LiveData<Story> loadStoryById(int id);

    @Query("SELECT * FROM story WHERE audiotitle GLOB 'Washington*'")
    LiveData<List<Story>> loadStoryNameById();

    @Query("SELECT * from story WHERE storystate like  :desc")
    LiveData<List<Story>> getSearchResults(String desc);


    //Querying Saved Audio//
    @Query("SELECT * FROM story WHERE userfire = :userfire")
    LiveData<List<Story>> loadSavedAudioById(String userfire);

    //Querying Edit Audio//
    @Query("SELECT * FROM story WHERE primaryId = :id")
    LiveData<Story> loadEditAudioById(int  id);

}
