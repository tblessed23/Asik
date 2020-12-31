package com.msbs.android.asik.model;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import org.jetbrains.annotations.NotNull;

public class UserEditViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final AppDatabase mDb;
    private final int mTaskId;


    // Initialize the member variables in the constructor with the parameters received
    public  UserEditViewModelFactory(AppDatabase database, int taskId) {
        mDb = database;
        mTaskId = taskId;


    }

    @NotNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new UserEditViewModel(mDb, mTaskId);
    }
}

