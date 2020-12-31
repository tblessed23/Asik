package com.msbs.android.asik.ui.favorites;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.msbs.android.asik.R;

public class FavoritesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_favorites);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new FavoritesFragment())
                .commit();

        setActionBarTitle("Favorites");
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);}
}