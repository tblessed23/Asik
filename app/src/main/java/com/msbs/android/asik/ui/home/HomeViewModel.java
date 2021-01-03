package com.msbs.android.asik.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("At Kisa YOU are the Storyteller. The Preserver of History. Tell the stories of your ancestors how you want in one place. What's in your heart...let the world know. It's time to tell your story.");
    }

    public LiveData<String> getText() {
        return mText;
    }
}