package com.msbs.android.asik.ui.users;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.msbs.android.asik.model.AppDatabase;
import com.msbs.android.asik.model.User;

import java.util.List;

public class DisplayProfileViewModel extends ViewModel {

    //Constant for logging
    private static final String TAG = DisplayProfileViewModel.class.getSimpleName();

    private final LiveData<List<User>> tasks;

    // Create a constructor where you call loadTaskById of the taskDao to initialize the tasks variable
    // Note: The constructor should receive the database and the taskId
    public DisplayProfileViewModel(AppDatabase database, String taskId) {
        tasks = database.userDao().loadTaskById(taskId);
    }

    public LiveData<List<User>> getTasks() {
        return tasks;
    }
}
