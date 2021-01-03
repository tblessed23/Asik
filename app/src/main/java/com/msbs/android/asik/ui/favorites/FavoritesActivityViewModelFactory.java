package com.msbs.android.asik.ui.favorites;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.msbs.android.asik.model.AppDatabase;
import org.jetbrains.annotations.NotNull;

public class FavoritesActivityViewModelFactory extends ViewModelProvider.NewInstanceFactory {


    private final AppDatabase mDb;
    private final String mTaskId;


    // Initialize the member variables in the constructor with the parameters received
    public  FavoritesActivityViewModelFactory(AppDatabase database, String taskId) {
        mDb = database;
        mTaskId = taskId;


    }


    @NotNull
    @Override
    public <T extends ViewModel> T create(@NotNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new FavoritesActivityViewModel(mDb, mTaskId);
    }
}


