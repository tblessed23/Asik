package com.msbs.android.asik.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "favorites")
public class Favorites {


    @PrimaryKey(autoGenerate =true)
    public int primaryId;
    @NonNull
    public String userfire;
    private String titleFavorites;
    private String urlFavorites;


    @Ignore
    public Favorites(@NotNull String userfire, String titleFavorites, String urlFavorites) {
        this.userfire = userfire;
        this.titleFavorites = titleFavorites;
        this.urlFavorites = urlFavorites;
    }

    public Favorites(int primaryId, @NotNull String userfire, String titleFavorites, String urlFavorites) {
        this.primaryId = primaryId;
        this.userfire = userfire;
        this.titleFavorites = titleFavorites;
        this.urlFavorites = urlFavorites;
    }

    public int getId() {
        return primaryId;
    }

    public void setId(int id) {
        this.primaryId = id;
    }

    /***UserId**/
    public String getUserId() {
        return userfire;
    }

    public void setUserId(String userfire) {
        this.userfire = userfire;
    }

    public String getTitleFavorites() {
        return titleFavorites;
    }

    public void setTitleFavorites(String title) {
        this.titleFavorites = titleFavorites;
    }

    public String getUrlFavorites() {return urlFavorites;}
    public void setUrlFavorites(String url) {this.urlFavorites = urlFavorites;}
}