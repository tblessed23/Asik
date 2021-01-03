package com.msbs.android.asik;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Slide;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;;
import com.google.android.material.navigation.NavigationView;
import com.msbs.android.asik.loggingin.LoggedInViewModel;
import com.msbs.android.asik.loggingin.LoginActivity;
import com.msbs.android.asik.loggingin.PreMainActivity;
import com.msbs.android.asik.search.SearchActivity;

import com.msbs.android.asik.ui.favorites.FavoritesActivity;
import com.msbs.android.asik.ui.recordings.RecordAudioActivity;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private TextView loggedInUserTextView;
    //private Button logOutButton;

    private LoggedInViewModel loggedInViewModel;
    private TextView messageTextView;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setExitTransition(new Explode());
        setContentView(R.layout.activity_main);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_about, R.id.nav_profile, R.id.nav_recordings)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        loggedInViewModel = ViewModelProviders.of(this).get(LoggedInViewModel.class);


        loggedInViewModel.getLoggedOutLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean loggedOut) {
                if (loggedOut) {
                    Toast.makeText(MainActivity.this, "User Logged Out", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, PreMainActivity.class);
                    startActivity(intent);

                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_search:
                Intent startSearchActivity = new Intent(this, SearchActivity.class);
                startActivity(startSearchActivity);
                break;
            case R.id.action_record:
                Intent startRecordActivity = new Intent(this, RecordAudioActivity.class);
                startActivity(startRecordActivity);
                break;
            case R.id.action_favorites:
                Intent startFavoritesActivity = new Intent(this, FavoritesActivity.class);
                startActivity(startFavoritesActivity);
                break;
            case R.id.action_logout:
                loggedInViewModel.logOut();
                break;


        }
        return false;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}