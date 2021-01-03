package com.msbs.android.asik.ui.users;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.msbs.android.asik.MainActivity;
import com.msbs.android.asik.R;
import com.msbs.android.asik.loggingin.LoggedInViewModel;
import com.msbs.android.asik.model.AppDatabase;
import com.msbs.android.asik.model.User;

import java.util.ArrayList;
import java.util.List;


public class UserDisplayDetailsFragment extends Fragment {

    //Constant for logging
    private static final String TAG = MainActivity.class.getSimpleName();
    private LoggedInViewModel loggedInViewModel;
    Button mButton;
    private TextView loggedInUserTextView;
    private UserAdapter mAdapter;
    private final List<User> mStoryEntries = new ArrayList<>();
    private Button logOutButton;

    /**
     * TextView that is displayed when the list is empty
     */
    private TextView mEmptyStateTextView;

    //Implement Database
    private AppDatabase mDb;

    public UserDisplayDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState){
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.activity_user_display_details, container, false);
        // Member variables for the adapter and RecyclerView
        RecyclerView mRecyclerView = rootView.findViewById(R.id.recyclerViewTasks);



        // Set the layout for the RecyclerView to be a linear layout, which measures and
        // positions items within a RecyclerView into a linear list
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Initialize the adapter and attach it to the RecyclerView
        mAdapter = new UserAdapter(getActivity());
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
        UserDisplayViewModelFactory factory = new UserDisplayViewModelFactory(mDb, mTaskId);
        // Declare a AddTaskViewModel variable and initialize it by calling ViewModelProviders.of
        // for that use the factory created above AddTaskViewModel
        final UserDisplayViewModel viewModel
                = ViewModelProviders.of(this, factory).get(UserDisplayViewModel.class);

        // Observe the LiveData object in the ViewModel. Use it also when removing the observer
        viewModel.getTasks().observe(getViewLifecycleOwner(), new Observer<List<User>>() {
            @Override
            public void onChanged(@Nullable List<User> taskEntry) {
                viewModel.getTasks().removeObserver(this);
                mAdapter.setTasks(taskEntry);

            }
        });

    }



}
