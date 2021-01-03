package com.msbs.android.asik.ui.favorites;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.msbs.android.asik.R;
import com.msbs.android.asik.model.AppDatabase;
import com.msbs.android.asik.model.AppExecutors;
import com.msbs.android.asik.model.EmptyStateRecyclerView;
import com.msbs.android.asik.model.Favorites;
import java.util.ArrayList;
import java.util.List;

public class FavoritesFragment extends Fragment {

    // Constant for logging
    private static final String TAG = FavoritesFragment.class.getSimpleName();
    private AppDatabase mDb;
    private FavoritesAdapter mAdapter;
    private EmptyStateRecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    /**
     * TextView that is displayed when the list is empty
     */
    private TextView mEmptyStateTextView;


    public FavoritesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDb = AppDatabase.getInstance(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favorites, container, false);

        // Find a reference to the {@link RecyclerView} in the layout
        recyclerView = rootView.findViewById(R.id.my_recycler_view);
        recyclerView.setEmptyView(rootView.findViewById(R.id.empty_view_favorites));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // Create a new adapter that takes an empty list of moviess as input
        mAdapter = new FavoritesAdapter(getActivity(), new ArrayList<>());
        recyclerView.setAdapter(mAdapter);

        setHasOptionsMenu(true);


        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        recyclerView.setAdapter(mAdapter);

        if (mAdapter == null) {
            showErrorMessage();
            mEmptyStateTextView.setText(R.string.no_favorites);
        }

  /*
         Add a touch helper to the RecyclerView to recognize when a user swipes to delete an item.
         An ItemTouchHelper enables touch behavior (like swipe and move) on each ViewHolder,
         and uses callbacks to signal when a user is performing these actions.
         */
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        //The Adapter holds the items you want to delete
                        int position = viewHolder.getAdapterPosition();
                        List<Favorites> removeFavorite = mAdapter.getTasks();
                        mDb.favoritesDao().deleteFavorites(removeFavorite.get(position));
                    }
                });
            }
        }).attachToRecyclerView(recyclerView);

        setUpViewModel();
        return rootView;
    }




    private void setUpViewModel() {
       String mTaskId = FirebaseAuth.getInstance().getUid();
//        // Remove the logging and the call to loadTaskById, this is done in the ViewModel now
//        // Declare a AddTaskViewModelFactory using mDb and mTaskId
        FavoritesActivityViewModelFactory factory = new FavoritesActivityViewModelFactory(mDb, mTaskId);
        // Declare a AddTaskViewModel variable and initialize it by calling ViewModelProviders.of
        // for that use the factory created above AddTaskViewModel
        final FavoritesActivityViewModel viewModel
                = ViewModelProviders.of(this, factory).get(FavoritesActivityViewModel.class);

        // Observe the LiveData object in the ViewModel. Use it also when removing the observer
        viewModel.getTasks().observe(getViewLifecycleOwner(), new Observer<List<Favorites>>() {
            @Override
            public void onChanged(@Nullable List<Favorites> taskEntry) {
                viewModel.getTasks().removeObserver(this);
                mAdapter.setTasks(taskEntry);

            }
        });

    }


    /**
     * This method will make the error message visible and hide the movies
     * View.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showErrorMessage() {
        /* First, hide the currently visible data */
        recyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mEmptyStateTextView.setVisibility(View.VISIBLE);
    }
}