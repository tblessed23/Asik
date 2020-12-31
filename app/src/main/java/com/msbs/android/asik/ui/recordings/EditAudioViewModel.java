package com.msbs.android.asik.ui.recordings;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.msbs.android.asik.model.AppDatabase;
import com.msbs.android.asik.model.Story;

public class EditAudioViewModel extends ViewModel {


    //  private UserEditRepository mRepository;
    private LiveData<Story> task;


    public EditAudioViewModel(){ };
    //Add a task member variable for the TaskEntry object wrapped in a LiveData


    // Create a constructor where you call loadTaskById of the taskDao to initialize the tasks variable
    // Note: The constructor should receive the database and the taskId
    public EditAudioViewModel(AppDatabase database, int taskId) {
        task = database.storyDao().loadEditAudioById(taskId);
    }



    // Create a getter for the task variable
    public LiveData<Story> getTask() {
        return task;
    }
}