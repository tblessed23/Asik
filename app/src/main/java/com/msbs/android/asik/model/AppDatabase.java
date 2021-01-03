package com.msbs.android.asik.model;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.msbs.android.asik.loggingin.UserDao;
import com.msbs.android.asik.search.PopulateSearchData;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Story.class, Favorites.class, User.class}, version = 5, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {

    private static final String LOG_TAG = AppDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "storieslist";
    private static AppDatabase sInstance;

    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);



    public static AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {


                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME)
                        .addCallback(callback)
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

    private static final RoomDatabase.Callback callback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            PopulateSearchData populateSearchData =  new PopulateSearchData(sInstance);
            populateSearchData.populateData();
        }
    };

    public abstract StoryDao storyDao();
    public abstract FavoritesDao favoritesDao();
    public abstract UserDao userDao();

}

