package com.msbs.android.asik.loggingin;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.msbs.android.asik.model.AppDatabase;
import com.msbs.android.asik.model.User;

import java.util.List;

public class UserViewModel extends ViewModel {

    //Constant for logging
    private static final String TAG = UserViewModel.class.getSimpleName();

    private final LiveData<User> tasks;

    // Create a constructor where you call 'loadById 'of the taskDao to initialize the tasks variable
    // Note: The constructor should receive the database and the taskId
    public UserViewModel(AppDatabase database, int taskId) {
        tasks = database.userDao().loadUserEditById(taskId);
    }


    public LiveData<User> getTasks() {
        return tasks;
    }
}
