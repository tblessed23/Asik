package com.msbs.android.asik.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FavoritesDao {

    @Query("SELECT * FROM favorites")
    LiveData<List<Favorites>> loadAllFavorites();

    @Insert
    void insertFavorites(Favorites favoriteEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateFavorites(Favorites favoriteEntry);

    @Delete
    void deleteFavorites(Favorites favoriteEntry);


    // The query for this method should get all the data for that id in the favorites table
    @Query("SELECT * FROM favorites WHERE primaryId = :id")
    LiveData<Favorites> loadFavoritesAgainById(int id);

    //Querying Saved Audio//
    @Query("SELECT * FROM favorites WHERE userfire = :userfire")
    LiveData<List<Favorites>> loadFavoritesById(String userfire);
}

