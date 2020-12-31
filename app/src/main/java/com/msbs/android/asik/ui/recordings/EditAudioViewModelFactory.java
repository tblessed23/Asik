package com.msbs.android.asik.ui.recordings;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.msbs.android.asik.model.AppDatabase;

import org.jetbrains.annotations.NotNull;

public class EditAudioViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final AppDatabase mDb;
    private final int mTaskId;


    // Initialize the member variables in the constructor with the parameters received
    public  EditAudioViewModelFactory(AppDatabase database, int taskId) {
        mDb = database;
        mTaskId = taskId;


    }

    // Uncomment the following method
    @NotNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new EditAudioViewModel(mDb, mTaskId);
    }
}
