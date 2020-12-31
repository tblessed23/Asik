package com.msbs.android.asik.loggingin;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.msbs.android.asik.R;
import com.msbs.android.asik.model.AppDatabase;
import com.msbs.android.asik.ui.users.UserDisplayDetailsActivity;

public class RegisterActivity extends AppCompatActivity {


    private EditText emailEditText;
    private EditText passwordEditText;
    private Button registerButton;
    private AppDatabase mDb;
    private LoginRegisterViewModel loginRegisterViewModel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize member variable for the data base
        mDb = AppDatabase.getInstance(getApplicationContext());

        loginRegisterViewModel = ViewModelProviders.of(this).get(LoginRegisterViewModel.class);
        loginRegisterViewModel.getUserLiveData().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser != null) {
                    Intent intent = new Intent(RegisterActivity.this, UserDisplayDetailsActivity.class);
                    startActivity(intent);
                }
            }
        });

        emailEditText = findViewById(R.id.register_email);
        passwordEditText = findViewById(R.id.register_password);
        registerButton = findViewById(R.id.register_button);


        registerButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onClick(View view) {

                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                if (email.length() > 0 && password.length() > 0) {
                    loginRegisterViewModel.register(email, password);

                } else {
                    Toast.makeText(RegisterActivity.this, "Email Address and Password Must Be Entered", Toast.LENGTH_SHORT).show();
                }


            }
        });
        setActionBarTitle("Register At Kisa");
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);}
}

