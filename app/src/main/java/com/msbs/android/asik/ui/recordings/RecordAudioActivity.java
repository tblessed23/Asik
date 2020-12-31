package com.msbs.android.asik.ui.recordings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.loadingview.LoadingView;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.msbs.android.asik.R;
import com.msbs.android.asik.UserAudioDetailActivity;
import com.msbs.android.asik.model.AppDatabase;
import com.msbs.android.asik.model.AppExecutors;
import com.msbs.android.asik.model.Story;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RecordAudioActivity extends AppCompatActivity implements
        MediaRecorder.OnInfoListener, MediaPlayer.OnCompletionListener{

    private static MediaRecorder mediaRecorder;
    private static MediaPlayer mediaPlayer;

    private String audioFilePath;
    private Button mStopButton;
    private Button mPlayButton;
    private Button mRecordButton;
    private Button mEditButton;
    private Button mPauseButton;
    private Button mResumeButton;
    private Button mSaveButton;

    private SeekBar mSeekBar;
    private int mInterval = 10;
    private Handler mHandler;
    private Runnable mRunnable;

    private TextView mPass;
    private TextView mDuration;
    private TextView mDue;
    private TextView mStateWarning;
    private TextView mTitleWarning;
    String getStateText;
    String getTitleText;
    View view;

    TextView textView;
    CountDownTimer countDownTimer;
    int second = -1, minute, hour;
    int intDuration = 0;

    private String practicekisa = customFilepath();



    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static final String LOG_TAG = "Record_Log";

    private boolean isRecording = false;

    //Firebase Instance Variable
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mAudioStorageReference;
    private Uri audioUri;

    private Story stories;
    private Uri downloadUri;
    private LoadingView loadingView;

    // Member variable for the Database
    private AppDatabase mDb;
    Button mButton;
    TextInputEditText mEditT, mEditState;
    private int mTaskId = DEFAULT_TASK_ID;
    // Constant for default task id to be used when not in update mode
    private static final int DEFAULT_TASK_ID = -1;

    //Check if User is Authenticated
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_audio);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        mSaveButton = findViewById(R.id.saveRecordButton);



        initViews();

        loadingView = findViewById(R.id.loadingView);
        loadingView.setVisibility(View.GONE);

        // Initialize member variable for the data base
        mDb = AppDatabase.getInstance(getApplicationContext());

        //Intialize Firebase Components
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();

        // Create a storage reference from our app
        mAudioStorageReference = mFirebaseStorage.getReference().child
                ("k_audio").child(customFilepath());



        audioSetup();



        ActivityCompat.requestPermissions(this, permissions,
                REQUEST_RECORD_AUDIO_PERMISSION);

        mEditState.addTextChangedListener(loginTextWatcher);
        mEditT.addTextChangedListener(loginTextWatcher);

        setActionBarTitle("Record Your Story");


    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);}


    private TextWatcher loginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int
                count) {
            String stateInput = mEditState.getText().toString().trim();
            String titleInput = mEditT.getText().toString().trim();
            mSaveButton.setEnabled(!stateInput.isEmpty() && !
                    titleInput.isEmpty());

        }
        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void initViews() {
        mEditT=findViewById(R.id.record_story_title);
        mEditState=findViewById(R.id.record_story_state);

        mButton = findViewById(R.id.saveRecordButton);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSaveButtonClicked();
            }
        });
    }

    private void showDuration() {
        if (mediaPlayer != null) { //make sure media player was created
            long totalDuration = mediaPlayer.getDuration();
            Toast.makeText(this, "Your duration: " +
                    RecordingWorks.milliSecondsToTimer(totalDuration), Toast.LENGTH_LONG).show();
            intDuration = mediaPlayer.getDuration();
        }
    }

    //Create file path for file uploaded into Firebase Storage
    private String customFilepath() {
        SimpleDateFormat dateFormat = new SimpleDateFormat
                ("dd_MMM_yyyy_HH:mm", Locale.US);
        String date = dateFormat.format(new Date());

        return date + ".3pg";
    }




    public void playButton(View view) throws IOException {

        second = -1;
        minute = 0;
        hour = 0;
        textView.setText("00:00:00");

        mPlayButton.setEnabled(false);
        mRecordButton.setEnabled(false);
        mStopButton.setEnabled(true);


        mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource(audioFilePath);
        mediaPlayer.setOnCompletionListener(this::onCompletion);
        mediaPlayer.prepareAsync();
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Toast.makeText(RecordAudioActivity.this,"prepatre",Toast.LENGTH_LONG).show();
                mediaPlayer.start();
                showDuration();
                showTimer();
            }

        });





    }





    public void recordButton(View view) {
        second = -1;
        minute = 0;
        hour = 0;
        textView.setText("00:00:00");

        isRecording = true;
        mStopButton.setEnabled(true);
        mPlayButton.setEnabled(false);
        mRecordButton.setEnabled(false);


        try {
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(
                    MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setOutputFile(audioFilePath);
            mediaRecorder.setMaxDuration(1800000);
            mediaRecorder.setOnInfoListener(this::onInfo);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.prepare();
        } catch (Exception e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
        mediaRecorder.start();
        //onInfo(mediaRecorder, position);

        showTimer();

    }




    public void stopButton(View view) {

        countDownTimer.cancel();
        mStopButton.setEnabled(false);
        mPlayButton.setEnabled(true);

        if (isRecording)
        {

            mRecordButton.setEnabled(false);
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            isRecording = false;


        } else {
            showDuration();
            mediaPlayer.release();
            mediaPlayer = null;
            mRecordButton.setEnabled(true);
        }


    }


    private void audioSetup()
    {

        textView = findViewById(R.id.text);
        mRecordButton =  findViewById(R.id.record_button);
        mPlayButton =  findViewById(R.id.play_button);
        mStopButton =  findViewById(R.id.stop_button);
        if (!hasMicrophone())
        {
            mStopButton.setEnabled(false);
            mPlayButton.setEnabled(false);
            mRecordButton.setEnabled(false);
        } else {
            mPlayButton.setEnabled(false);
            mStopButton.setEnabled(false);

        }

        // Record to the external cache directory for visibility
        audioFilePath = getExternalCacheDir().getAbsolutePath();
        audioFilePath += "/audioagain.3gp";

    }



    /***check if android device has microphone***/
    protected boolean hasMicrophone() {
        PackageManager pmanager = this.getPackageManager();
        return pmanager.hasSystemFeature(
                PackageManager.FEATURE_MICROPHONE);
    }

    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[]
            permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions,
                grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) finish();

    }


    //display recording time
    public void showTimer() {
        countDownTimer = new CountDownTimer(Long.MAX_VALUE, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                second++;
                textView.setText(recorderTime());
            }
            public void onFinish() {

            }
        };
        countDownTimer.start();
    }

    //recorder time
    public String recorderTime() {
        if (second == 60) {
            minute++;
            second = 0;
        }
        if (minute == 60) {
            hour++;
            minute = 0;
        }
        return String.format("%02d:%02d:%02d", hour, minute, second);
    }


    /**
     * onSaveButtonClicked is called when the "save" button is clicked.
     * It retrieves user input and inserts that new task data into the
     underlying database.
     */
    public void onSaveButtonClicked() {
        downloadfile();
        loadingView.start();


    }

    private String downloadfile() {


        InputStream stream = null;
        try {
            stream = new FileInputStream(new File(audioFilePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        assert stream != null;
        UploadTask uploadTask = mAudioStorageReference.putStream(stream);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(RecordAudioActivity.this, "No need to Firebase Console", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new


                                        OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess
                                                    (UploadTask.TaskSnapshot
                                                             taskSnapshot) {

                                                Log.i("Mainq", "File uri: " +
                                                        taskSnapshot.toString());
                                                Toast.makeText
                                                        (RecordAudioActivity.this, "Check Firebase Console",

                                                                Toast.LENGTH_SHORT).show();

                                            }

                                        });


        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot,
                Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task)
                    throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                return mAudioStorageReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    downloadUri = task.getResult();
                    assert downloadUri != null;
                    int pid = 0;
                    final String audiourl = downloadUri.toString();
                    String userId = FirebaseAuth.getInstance().getUid();
                    String audiotitle = mEditT.getText().toString();
                    String storystate = mEditState.getText().toString();
                    Date date = new Date();
                    final Story upload = new Story(pid, userId, audiotitle,null, null,
                            storystate, null, null, null, audiourl,
                            date);
                    AppExecutors.getInstance().diskIO().execute(new Runnable()
                    {
                        @Override
                        public void run() {
                            // Insert the task only if mTaskId matches
                            //DEFAULT_TASK_ID
                            // Otherwise update it
                            // call finish in any case
                            if (mTaskId == DEFAULT_TASK_ID) {
                                // insert new task
                                upload.setAudioUrl(audiourl);
                                mDb.storyDao().insertTask(upload);

                            }

                            finish();
                        }
                    });
                }
            }
        });

        return null;
    }

    public void onInfo(MediaRecorder mr, int what, int extra) {
        if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {
            Toast.makeText(this, "Maximum Recording Session Reached",
                    Toast.LENGTH_LONG).show();
            stopButton(view);
        }


    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

    }


    @Override
    public void onCompletion(MediaPlayer mp) {
        stopButton(view);
    }


}