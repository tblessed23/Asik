package com.msbs.android.asik.ui.users;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.msbs.android.asik.R;
import com.msbs.android.asik.loggingin.LoggedInViewModel;
import com.msbs.android.asik.loggingin.UserViewModel;
import com.msbs.android.asik.loggingin.UserViewModelFactory;
import com.msbs.android.asik.model.AppDatabase;
import com.msbs.android.asik.model.AppExecutors;
import com.msbs.android.asik.model.User;

public class UserEditDetailsActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    // Extra for the task ID to be received in the intent
    public static final String EXTRA_TASK_ID = "extraTaskId";
    // Extra for the task ID to be received after rotation
    public static final String INSTANCE_TASK_ID = "instanceTaskId";

    // Constant for default task id to be used when not in update mode
    private static final int DEFAULT_TASK_ID = -1;
    // Constant for logging
    private static final String TAG = UserEditDetailsActivity.class.getSimpleName();
    // Fields for views
    TextInputEditText mEditEM, mEditFN, mEditLN, mEditCI, mEditST, mEditCO, mEditPH, mEditDN, mEditDE;
    private Button logOutButton;
    private User users;
    String audiotitle;
    String titleFavorities;
    int storyId;
    String titleFavs;
    String titlea;
    String audioa;
    int ida;
    private LoggedInViewModel loggedInViewModel;
    Button mButton;
    private TextView loggedInUserTextView;

    private int mTaskId = DEFAULT_TASK_ID;

    // Member variable for the Database
    private AppDatabase mDb;
    //defining AwesomeValidation object
    private AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edit_details);


        initViews();

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        awesomeValidation.addValidation(this, R.id.filleAncestorLNdTextField2, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.nameerror);
        awesomeValidation.addValidation(this, R.id.filledAncestorLNTextField3, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.nameerror);
        awesomeValidation.addValidation(this, R.id.filledFamilyNameTextField4, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.nameerror);
        awesomeValidation.addValidation(this, R.id.filledTextFieldPhone, RegexTemplate.TELEPHONE, R.string.err_tel);
        awesomeValidation.addValidation(this, R.id.filledTextFieldEmail, android.util.Patterns.EMAIL_ADDRESS, R.string.err_email);



        // Initialize member variable for the data base
        mDb = AppDatabase.getInstance(getApplicationContext());

        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_TASK_ID)) {
            mTaskId = savedInstanceState.getInt(INSTANCE_TASK_ID, DEFAULT_TASK_ID);
        }

        Intent intent = getIntent();


        if (getIntent().hasExtra("StoriesId"))
            ida = getIntent().getIntExtra("StoriesId", 0);


        if (intent != null && intent.hasExtra(EXTRA_TASK_ID)) {
            mButton.setText(R.string.update_button);

            if (mTaskId == DEFAULT_TASK_ID) {

                // populate the UI

                mTaskId = intent.getIntExtra(EXTRA_TASK_ID, DEFAULT_TASK_ID);

                // Remove the logging and the call to loadTaskById, this is done in the ViewModel now
                // Declare a AddTaskViewModelFactory using mDb and mTaskId
                UserViewModelFactory factory = new UserViewModelFactory(mDb, mTaskId);
                // Declare a AddTaskViewModel variable and initialize it by calling ViewModelProviders.of
                // for that use the factory created above AddTaskViewModel
                final UserViewModel viewModel
                        = ViewModelProviders.of(this, factory).get(UserViewModel.class);

                // Observe the LiveData object in the ViewModel. Use it also when removing the observer
                viewModel.getTasks().observe(this, new Observer <User>() {
                    @Override
                    public void onChanged(@Nullable User taskEntry) {
                        viewModel.getTasks().removeObserver(this);
                        populateUI(taskEntry);

                    }
                });
            }

        }

setActionBarTitle("Edit Profile");

    }


    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);}

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(INSTANCE_TASK_ID, mTaskId);
        super.onSaveInstanceState(outState);
    }

    /**
     * initViews is called from onCreate to init the member variable views
     */
    private void initViews() {
        mEditEM = findViewById(R.id.label_email);
        mEditFN= findViewById(R.id.label_firstname);
        mEditLN=findViewById(R.id.label_lastname);
        mEditCI=findViewById(R.id.label_city);
        mEditST=findViewById(R.id.label_state);
        mEditCO=findViewById(R.id.label_country);
        mEditPH=findViewById(R.id.label_phone);
        mEditDN=findViewById(R.id.label_displayname);
        mEditDE=findViewById(R.id.label_displayemail);




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
     * @param users the taskEntry to populate the UI
     */
    private void populateUI(User users) {
        //  return if the task is null
        if (users == null) {
            return;
        }

        // use the variable task to populate the UI
        mEditEM.setText(users.getEmail());
        mEditFN.setText(users.getFirstname());
        mEditLN.setText(users.getLastname());
        mEditCI.setText(users.getCity());
        mEditST.setText(users.getState());
        mEditCO.setText(users.getCountry());
        mEditPH.setText(users.getPhone());
        mEditDN.setText(users.getDisplayname());
        mEditDE.setText(users.getDisplayemail());
    }

    /**
     * onSaveButtonClicked is called when the "save" button is clicked.
     * It retrieves user input and inserts that new task data into the underlying database.
     */
    public void onSaveButtonClicked() {

        if (awesomeValidation.validate()) {
            Toast.makeText(this, "Validation Successful", Toast.LENGTH_LONG).show();
            String email = mEditEM.getText().toString();
            String firstname = mEditFN.getText().toString();
            String lastname = mEditLN.getText().toString();
            String city = mEditCI.getText().toString();
            String state = mEditST.getText().toString();
            String country = mEditCO.getText().toString();
            String phone = mEditPH.getText().toString();
            String displayname = mEditDN.getText().toString();
            String displayemail = mEditDE.getText().toString();
            String userId = FirebaseAuth.getInstance().getUid();


            // Make taskEntry final so it is visible inside the run method
            final User taskEntry = new User(userId, email, firstname, lastname, city, state, country, phone, displayname, displayemail);
            // Get the diskIO Executor from the instance of AppExecutors and
            // call the diskIO execute method with a new Runnable and implement its run method
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    // Insert the task only if mTaskId matches DEFAULT_TASK_ID
                    // Otherwise update it
                    // call finish in any case
                    if (mTaskId == DEFAULT_TASK_ID) {
                        // insert new task
                        mDb.userDao().insertTask(taskEntry);
                    } else {
                        //update task
                        taskEntry.setPrimaryId(mTaskId);
                        mDb.userDao().updateTask(taskEntry);
                    }
                    finish();
                }
            });
        }
    }
}