package com.msbs.android.asik.ui.favorites;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.msbs.android.asik.model.AppDatabase;
import com.msbs.android.asik.model.Favorites;
import com.msbs.android.asik.ui.recordings.SavedAudioViewModel;
import java.util.List;

public class FavoritesActivityViewModel extends ViewModel {

    //Constant for logging
    private static final String TAG = SavedAudioViewModel.class.getSimpleName();

    private final LiveData<List<Favorites>> tasks;

    // Create a constructor where you call loadTaskById of the taskDao to initialize the tasks variable
    // Note: The constructor should receive the database and the taskId
    public FavoritesActivityViewModel(AppDatabase database, String taskId) {
        tasks = database.favoritesDao().loadFavoritesById(taskId);
    }

    public LiveData<List<Favorites>> getTasks() {
        return tasks;
    }
}
