package com.msbs.android.asik.loggingin;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.msbs.android.asik.model.AppDatabase;
import com.msbs.android.asik.model.User;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class UserViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final AppDatabase mDb;
    private final int mTaskId;


    // Initialize the member variables in the constructor with the parameters received
    public  UserViewModelFactory(AppDatabase database, int taskId) {
        mDb = database;
        mTaskId = taskId;

    }

    @NotNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new UserViewModel(mDb, mTaskId);
    }


}
