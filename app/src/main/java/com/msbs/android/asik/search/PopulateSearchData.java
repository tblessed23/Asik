package com.msbs.android.asik.search;

import android.util.Log;

import androidx.annotation.NonNull;

import com.msbs.android.asik.model.AppDatabase;
import com.msbs.android.asik.model.Story;
import com.msbs.android.asik.model.StoryDao;

import org.reactivestreams.Subscription;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class PopulateSearchData {

    private CompositeDisposable roomDisposables = new CompositeDisposable();
    private static final String LOG_TAG = PopulateSearchData.class.getSimpleName();
    private static StoryDao storyDao;
    private Story fable;

    public PopulateSearchData(AppDatabase instance) {
        storyDao = instance.storyDao();
    }


    public void populateData() {
        Flowable.fromCallable(() -> {
            storyDao.loadAllStories();

            Story loadStoryData=   storyDao.loadStoryObject();
            Log.d(LOG_TAG, "rXjAVA input composite: " + loadStoryData);
            return "Done";
        })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.single())
                .subscribe(new FlowableSubscriber<String>() {
                    @Override
                    public void onSubscribe(@NonNull Subscription s) {

                    }

                    @Override
                    public void onNext(String s) {

                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.d(LOG_TAG, "Error observer: " + t);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(LOG_TAG, "Task completed: " );
                    }
                });
    }
}

