package com.msbs.android.asik.loggingin;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Slide;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.msbs.android.asik.MainActivity;
import com.msbs.android.asik.R;
import com.msbs.android.asik.model.AppDatabase;

public class LoginActivity extends AppCompatActivity {
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button registerButton;
    Button registerButtonTwo;
    private final int mTaskId = DEFAULT_TASK_ID;
    // Constant for default task id to be used when not in update mode
    private static final int DEFAULT_TASK_ID = -1;
    private LoginRegisterViewModel loginRegisterViewModel;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set an exit transition
        getWindow().setExitTransition(new Explode());

        setContentView(R.layout.activity_login);




        // Initialize member variable for the data base
        AppDatabase mDb = AppDatabase.getInstance(getApplicationContext());

        loginRegisterViewModel = ViewModelProviders.of(this).get(LoginRegisterViewModel.class);
        loginRegisterViewModel.getUserLiveData().observe(this, new Observer<FirebaseUser>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser != null) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);



                    Bundle bundle = ActivityOptions
                            .makeSceneTransitionAnimation(LoginActivity.this)
                            .toBundle();


                    startActivity(intent, bundle);
                }
            }
        });


        emailEditText = findViewById(R.id.login_email);
        passwordEditText = findViewById(R.id.login_password);
        Button loginButton = findViewById(R.id.login_button);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onClick(View view) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                if (email.length() > 0 && password.length() > 0) {
                    loginRegisterViewModel.login(email, password);
                } else {
                    Toast.makeText(LoginActivity.this, "Email Address and Password Must Be Entered", Toast.LENGTH_SHORT).show();
                }
            }
        });

        setActionBarTitle("Login to Kisa");


    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);}

}