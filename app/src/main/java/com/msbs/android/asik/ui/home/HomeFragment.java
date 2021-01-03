package com.msbs.android.asik.ui.home;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.msbs.android.asik.R;
import com.msbs.android.asik.loggingin.LoginActivity;
import com.msbs.android.asik.ui.recordings.RecordAudioActivity;

public class HomeFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);


      //Find the Record Button
        Button record = (Button) root.findViewById(R.id.record_button);

        // Set a click listener on that Button
        record.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the Lauryn Hill songs View is clicked on.
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                Intent recordIntent = new Intent(getActivity(), RecordAudioActivity.class);
                Bundle bundle = ActivityOptions
                        .makeSceneTransitionAnimation(getActivity())
                        .toBundle();
                startActivity(recordIntent, bundle);


            }
        });

        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        AdView mAdView = (AdView) root.findViewById(R.id.adView);
        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);
        return root;
    }
}