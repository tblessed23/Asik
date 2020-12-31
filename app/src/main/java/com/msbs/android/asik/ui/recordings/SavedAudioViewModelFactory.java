package com.msbs.android.asik.ui.recordings;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.msbs.android.asik.model.AppDatabase;

import org.jetbrains.annotations.NotNull;

public class SavedAudioViewModelFactory extends ViewModelProvider.NewInstanceFactory {


    private final AppDatabase mDb;
    private final String mTaskId;


    // Initialize the member variables in the constructor with the parameters received
    public  SavedAudioViewModelFactory(AppDatabase database, String taskId) {
        mDb = database;
        mTaskId = taskId;


    }

    // Uncomment the following method
    @NotNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new SavedAudioViewModel(mDb, mTaskId);
    }
}

