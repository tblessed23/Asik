package com.msbs.android.asik.loggingin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.msbs.android.asik.R;

public class PreMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_main);


        Button login = (Button) findViewById(R.id.login);

        // Set a click listener on that View
        login.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the Lauryn Hill songs View is clicked on.
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(PreMainActivity.this, LoginActivity.class);
                startActivity(loginIntent);
            }
        });



        // Find the View that shows the Lauryn Hill songs category
        Button register = (Button) findViewById(R.id.register);

        // Set a click listener on that View
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(PreMainActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
            }
        });
    }
}