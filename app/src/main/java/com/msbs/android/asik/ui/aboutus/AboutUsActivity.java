package com.msbs.android.asik.ui.aboutus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.msbs.android.asik.R;

public class AboutUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new AboutUsFragment())
                .commit();
    }
}