package com.msbs.android.asik.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

//This deals with the StoryDao
public class UserEditViewModel extends ViewModel {


    private LiveData<Story> task;


    public UserEditViewModel(){ };



    // Create a constructor where you call 'loadById' 'of the taskDao to initialize the tasks variable
    // Note: The constructor should receive the database and the taskId
    public UserEditViewModel(AppDatabase database, int taskId) {
        task = database.storyDao().loadStoryById(taskId);
    }



    // Create a getter for the task variable
    public LiveData<Story> getTask() {
        return task;
    };
}

