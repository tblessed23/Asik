package com.msbs.android.asik.ui.users;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.msbs.android.asik.model.AppDatabase;
import com.msbs.android.asik.model.Story;
import com.msbs.android.asik.model.User;

import java.util.List;

public class UserDisplayViewModel extends ViewModel {
    //Constant for logging
    private static final String TAG =UserDisplayViewModel.class.getSimpleName();

    private final LiveData<List<User>> tasks;

    // Create a constructor where you call loadTaskById of the taskDao to initialize the tasks variable
    // Note: The constructor should receive the database and the taskId
    public UserDisplayViewModel(AppDatabase database, String taskId) {
        tasks = database.userDao().loadUserDetailsById(taskId);
    }

    public LiveData<List<User>> getTasks() {
        return tasks;
    }
}
