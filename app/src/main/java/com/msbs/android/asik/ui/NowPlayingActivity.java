package com.msbs.android.asik.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.util.Util;
import com.msbs.android.asik.R;
import com.msbs.android.asik.model.AppDatabase;
import com.msbs.android.asik.model.Story;
import com.msbs.android.asik.model.UserEditViewModel;
import com.msbs.android.asik.model.UserEditViewModelFactory;
import com.msbs.android.asik.ui.recordings.EditAudioDetailsActivity;

public class NowPlayingActivity extends AppCompatActivity {

    public static final String EXTRA_TASK_ID = "extraTaskId";
    // Extra for the task ID to be received after rotation
    public static final String INSTANCE_TASK_ID = "instanceTaskId";

    // Constant for default task id to be used when not in update mode
    private static final int DEFAULT_TASK_ID = -1;
    // Constant for logging
    private static final String TAG = NowPlayingActivity.class.getSimpleName();
    // Fields for views
    TextView mEditT, mEditAfN, mState, mCounty, mFullname, mFamilyName;

    //Exo-player Variables

    private PlayerView mPlayerView;
    private SimpleExoPlayer player;
    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    private Uri videoLink;
    String textvurl;
    private Story storiesa;

    private int mTaskId = DEFAULT_TASK_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_playing);

        initViews();


        // Initialize member variable for the data base
        // Member variable for the Database
        AppDatabase mDb = AppDatabase.getInstance(getApplicationContext());

        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_TASK_ID)) {
            mTaskId = savedInstanceState.getInt(INSTANCE_TASK_ID, DEFAULT_TASK_ID);
        }

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_TASK_ID)) {

            //  mButton.setText(R.string.update_button);
            if (mTaskId == DEFAULT_TASK_ID) {
                // populate the UI
                mTaskId = intent.getIntExtra(EXTRA_TASK_ID, DEFAULT_TASK_ID);
                // Remove the logging and the call to loadTaskById, this is done in the ViewModel now
                // Declare a AddTaskViewModelFactory using mDb and mTaskId
                UserEditViewModelFactory factory = new UserEditViewModelFactory(mDb, mTaskId);
// Declare a AddTaskViewModel variable and initialize it by calling ViewModelProviders.of
// for that use the factory created above AddTaskViewModel
                final UserEditViewModel viewModel
                        = ViewModelProviders.of(this, factory).get(UserEditViewModel.class);

                // Observe the LiveData object in the ViewModel. Use it also when removing the observer
                viewModel.getTask().observe(this, new Observer<Story>() {
                    @Override
                    public void onChanged(@Nullable Story taskEntry) {
                        viewModel.getTask().removeObserver(this);
                        populateUI(taskEntry);

                    }

                });


            }
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(INSTANCE_TASK_ID, mTaskId);
        super.onSaveInstanceState(outState);
    }

    /**
     * initViews is called from onCreate to init the member variable views
     */
    private void initViews() {

        mEditT = findViewById(R.id.titleDetails);
        mState = findViewById(R.id.stateDetails);
        mCounty = findViewById(R.id.countyDetails);
        mFullname = findViewById(R.id.nameDetails);
        mFamilyName = findViewById(R.id.familynameDetails);

        // Initialize the player view.
        mPlayerView = findViewById(R.id.playerView);

    }

    /**
     * populateUI would be called to populate the UI when in update mode
     *
     * @param stories the taskEntry to populate the UI
     */
    private void populateUI(Story stories) {
        //  return if the task is null
        if (stories == null) {
            return;
        }

        String placeholderState = stories.getStorycity() + "," + "" +  stories.getStorystate();
        String placeholderFullname = stories.getAncestorlastname()+ "," + "" +  stories.getAncestorfirstname();

        mEditT.setText(stories.getAudiotitle());
        mState.setText(placeholderState);
        mCounty.setText(stories.getStorycounty());
        mFullname.setText(placeholderFullname);
        mFamilyName.setText(stories.getFamilyname());


        assert videoLink != null;
        videoLink = Uri.parse(stories.getAudioUrl());


        initializePlayer();

    }


    private void initializePlayer() {
        if (player == null) {

            player = new SimpleExoPlayer.Builder(this).build();
            mPlayerView.setPlayer(player);
            MediaItem mediaItem = MediaItem.fromUri(videoLink);
            player.setMediaItem(mediaItem);
            player.setPlayWhenReady(playWhenReady);
            player.seekTo(currentWindow, playbackPosition);
            player.prepare();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUi();
    }
    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        mPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT < 16) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT >= 16) {
            releasePlayer();
        }
    }

    private void releasePlayer() {
        if (player != null) {
            playWhenReady = player.getPlayWhenReady();
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            player.release();
            player = null;
        }
    }
}