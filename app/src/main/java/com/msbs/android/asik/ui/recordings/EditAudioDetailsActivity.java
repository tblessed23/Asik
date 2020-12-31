package com.msbs.android.asik.ui.recordings;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.msbs.android.asik.R;
import com.msbs.android.asik.model.AppDatabase;
import com.msbs.android.asik.model.AppExecutors;
import com.msbs.android.asik.model.Favorites;
import com.msbs.android.asik.model.Story;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EditAudioDetailsActivity extends AppCompatActivity {

    // Extra for the task ID to be received in the intent
    public static final String EXTRA_TASK_ID = "extraTaskId";
    // Extra for the task ID to be received after rotation
    public static final String INSTANCE_TASK_ID = "instanceTaskId";

    // Constant for default task id to be used when not in update mode
    private static final int DEFAULT_TASK_ID = -1;

    private int mTaskId = DEFAULT_TASK_ID;

    // Constant for logging
    private static final String TAG = EditAudioDetailsActivity.class.getSimpleName();
    // Fields for views
    TextInputEditText mEditT, mEditAFN, mEditALN, mEditFN, mEditCity, mEditCounty, mEditState;
    TextView mEditUrl, mDateTime;
    private Story stories;
    String audiotitle;
    String titleFavorities;
    int storyId;
    String titleFavs;
    String titlea;
    String audioa;
    String userfire;
    int ida;
    // Constant for date format
    private static final String DATE_FORMAT = "MM/dd/yyy";
    // Date formatter
    private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());

    Button mButton;
    Button mFavoriteButton;
    String favTaskId = FirebaseAuth.getInstance().getUid();

    // Member variable for the Database
    private AppDatabase mDb;
    //defining AwesomeValidation object
    private AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_audio_details);

        mFavoriteButton = findViewById(R.id.favoriteButton);
        initViews();

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        awesomeValidation.addValidation(this, R.id.filleAncestorLNdTextField2, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.nameerror);
        awesomeValidation.addValidation(this, R.id.filledAncestorLNTextField3, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.nameerror);
        awesomeValidation.addValidation(this, R.id.filledFamilyNameTextField4, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.nameerror);

        // Initialize member variable for the data base
        mDb = AppDatabase.getInstance(getApplicationContext());

        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_TASK_ID)) {
            mTaskId = savedInstanceState.getInt(INSTANCE_TASK_ID, DEFAULT_TASK_ID);
        }

        Intent intent = getIntent();

        if (getIntent().hasExtra("Stories"))
            titlea = getIntent().getStringExtra("Stories");

        if (getIntent().hasExtra("StoriesLink"))
            audioa = getIntent().getStringExtra("StoriesLink");

        if (getIntent().hasExtra("StoriesUserFire"))
            userfire = getIntent().getStringExtra("StoriesUserFire");


        if (intent != null && intent.hasExtra(EXTRA_TASK_ID)) {
            mButton.setText(R.string.update_button);

            if (mTaskId == DEFAULT_TASK_ID) {

                // populate the UI

                mTaskId = intent.getIntExtra(EXTRA_TASK_ID, DEFAULT_TASK_ID);

                // Remove the logging and the call to loadTaskById, this is done in the ViewModel now
                // Declare a AddTaskViewModelFactory using mDb and mTaskId
                EditAudioViewModelFactory factory = new EditAudioViewModelFactory(mDb, mTaskId);
                // Declare a AddTaskViewModel variable and initialize it by calling ViewModelProviders.of
                // for that use the factory created above AddTaskViewModel
                final EditAudioViewModel viewModel
                        = ViewModelProviders.of(this, factory).get(EditAudioViewModel.class);

                // Observe the LiveData object in the ViewModel. Use it also when removing the observer
                viewModel.getTask().observe(this, new Observer<Story>() {
                    @Override
                    public void onChanged(@Nullable Story taskEntry) {
                        viewModel.getTask().removeObserver(this);
                        populateUI(taskEntry);
                        queryFavorites();
                    }
                });
            }
        }


setActionBarTitle("Edit Your Story");
    }


    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);}


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(INSTANCE_TASK_ID, mTaskId);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void queryFavorites(){
        LiveData<Favorites> currentId = mDb.favoritesDao().loadFavoritesAgainById(mTaskId);
        currentId.observe(this, new Observer<Favorites>() {
            @Override
            public void onChanged(Favorites favorites) {
                if (favorites !=null && favorites.getId()==mTaskId){
                    Drawable image = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_favorite_24, null);
                    mFavoriteButton.setEnabled(false);
                    mFavoriteButton.setText(R.string.favoritebutton);
                    mFavoriteButton.setCompoundDrawablesWithIntrinsicBounds(image, null, null, null);
                }else {
                    mFavoriteButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            saveButton();
                        }
                    });
                }
            }
        });

    }

    /**
     * onSaveButtonClicked is called when the "save" button is clicked.
     * It retrieves user input and inserts that new task data into the underlying database.
     */

    public void saveButton() {
        String audiolink = audioa;
        String audioa = titlea;
        String user = FirebaseAuth.getInstance().getUid();
        int id = mTaskId;

        final Favorites favorites = new Favorites(id, user, audioa, audiolink);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.favoritesDao().insertFavorites(favorites);
                finish();
            }
        });
    }

    /**
     * initViews is called from onCreate to init the member variable views
     */
    private void initViews() {
        mEditAFN = findViewById(R.id.edit_ancestor_first_name);
        mEditALN= findViewById(R.id.edit_ancestor_last_name);
        mEditT=findViewById(R.id.story_title);
        mEditFN=findViewById(R.id.edit_family_name);
        mEditCity=findViewById(R.id.story_city);
        mEditCounty=findViewById(R.id.story_county);
        mEditState=findViewById(R.id.story_state);
        mEditUrl=findViewById(R.id.url_text_view);
        mDateTime=findViewById(R.id.taskUpdatedAt);


        mButton = findViewById(R.id.saveButton);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (view == mButton) {
                    onSaveButtonClicked();
                }
            }
        });
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

        // use the variable task to populate the UI
        mEditT.setText(stories.getAudiotitle());
        mEditAFN.setText(stories.getAncestorfirstname());
        mEditALN.setText(stories.getAncestorlastname());
        mEditFN.setText(stories.getFamilyname());
        mEditCity.setText(stories.getStorycity());
        mEditCounty.setText(stories.getStorycounty());
        mEditState.setText(stories.getStorystate());
        mEditUrl.setText(stories.getAudioUrl());
        mDateTime.setText(dateFormat.format(stories.getUpdatedAt()));
    }

    /**
     * onSaveButtonClicked is called when the "save" button is clicked.
     * It retrieves user input and inserts that new task data into the underlying database.
     */
    public void onSaveButtonClicked() {

        if (awesomeValidation.validate()) {
            Toast.makeText(this, "Validation Successfull", Toast.LENGTH_LONG).show();
            audiotitle = mEditT.getText().toString();
            String ancestorfirstname = mEditAFN.getText().toString();
            String ancestorlastname = mEditALN.getText().toString();
            String familyname = mEditFN.getText().toString();
            String storycity = mEditCity.getText().toString();
            String storycounty = mEditCounty.getText().toString();
            String storystate = mEditState.getText().toString();
            String audiourl = mEditUrl.getText().toString();

            String userfire = FirebaseAuth.getInstance().getUid();
            //Create a date variable and assign to it the current Date
            Date date = new Date();


            // Make taskEntry final so it is visible inside the run method
            final Story taskEntry = new Story(userfire, audiotitle, storycity, storycounty, storystate, ancestorfirstname, ancestorlastname, familyname, audiourl, date);
            // Get the diskIO Executor from the instance of AppExecutors and
            // call the diskIO execute method with a new Runnable and implement its run method
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    //Insert the story only if mTaskId matches DEFAULT_TASK_ID
                    // Otherwise update it
                    // call finish in any case
                    if (mTaskId == DEFAULT_TASK_ID) {
                        // insert new task
                        mDb.storyDao().insertTask(taskEntry);
                    } else {
                        //update task
                        taskEntry.setPrimaryId(mTaskId);
                        mDb.storyDao().updateTask(taskEntry);
                    }
                    finish();
                }
            });
        }
    }
}