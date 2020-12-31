package com.msbs.android.asik.ui.recordings;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.msbs.android.asik.MainActivity;
import com.msbs.android.asik.R;
import com.msbs.android.asik.model.AppDatabase;
import com.msbs.android.asik.model.EmptyStateRecyclerView;
import com.msbs.android.asik.model.MainViewModel;
import com.msbs.android.asik.model.Story;

import java.util.ArrayList;
import java.util.List;


public class SavedAudioFragment extends Fragment {


    private static final String TAG = SavedAudioActivity.class.getSimpleName();

    // Member variables for the adapter and RecyclerView
    private EmptyStateRecyclerView mRecyclerView;
    private StoryAdapter mAdapter;
    private List<Story> mStoryEntries = new ArrayList<>();



    //Implement Database
    private AppDatabase mDb;

    public SavedAudioFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.activity_saved_audio, container, false);
        mRecyclerView = rootView.findViewById(R.id.recyclerViewTasks);
        mRecyclerView.setEmptyView(rootView.findViewById(R.id.empty_view));


        // Set the layout for the RecyclerView to be a linear layout, which measures and
        // positions items within a RecyclerView into a linear list
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Initialize the adapter and attach it to the RecyclerView
        mAdapter = new StoryAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);

        DividerItemDecoration decoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(decoration);


        //Initialize Database
        mDb = AppDatabase.getInstance(getContext());

        setupViewModel();

        return rootView;
    }



    private void setupViewModel(){


        String mTaskId = FirebaseAuth.getInstance().getUid();
        // Remove the logging and the call to loadTaskById, this is done in the ViewModel now
        // Declare a AddTaskViewModelFactory using mDb and mTaskId
        SavedAudioViewModelFactory factory = new SavedAudioViewModelFactory(mDb, mTaskId);
        // Declare a AddTaskViewModel variable and initialize it by calling ViewModelProviders.of
        // for that use the factory created above AddTaskViewModel
        final SavedAudioViewModel viewModel
                = ViewModelProviders.of(this, factory).get(SavedAudioViewModel.class);

        // Observe the LiveData object in the ViewModel. Use it also when removing the observer
        viewModel.getTasks().observe(getViewLifecycleOwner(), new Observer<List<Story>>() {
            @Override
            public void onChanged(@Nullable List<Story> taskEntry) {
                viewModel.getTasks().removeObserver(this);
                mAdapter.setTasks(taskEntry);

            }
        });

    }
}