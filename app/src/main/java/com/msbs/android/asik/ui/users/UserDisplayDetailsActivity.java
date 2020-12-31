package com.msbs.android.asik.ui.users;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.msbs.android.asik.R;

public class UserDisplayDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_display_details);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new UserDisplayDetailsFragment())
                .commit();

        setActionBarTitle("My Profile");
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);}
}