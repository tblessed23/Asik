package com.msbs.android.asik.ui.favorites;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.msbs.android.asik.R;
import com.msbs.android.asik.model.Favorites;

import java.util.ArrayList;
import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder> {

    //The Layout for this file is favorite_layout

    // Class variables for the List that holds task data and the Context
    private List<Favorites> mTaskEntries;
    private Context mContext;


    /**
     * Constructor for the TaskAdapter that initializes the Context.
     *
     * @param context  the current Context
     * @param Reviews the ItemClickListener
     */
    public FavoritesAdapter(Context context, ArrayList<Favorites> Reviews) {
        mContext = context;
        mTaskEntries = Reviews;
    }

    /**
     * Called when ViewHolders are created to fill a RecyclerView.
     *
     * @return A new FavoritesViewHolder that holds the view for each task
     */
    @Override
    public FavoritesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the task_layout to a view
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.list_favorites, parent, false);

        return new FavoritesViewHolder(view);
    }

    /**
     * Called by the RecyclerView to display data at a specified position in the Cursor.
     *
     * @param holder   The ViewHolder to bind Cursor data to
     * @param position The position of the data in the Cursor
     */
    @Override
    public void onBindViewHolder(FavoritesViewHolder holder, int position) {
        // Determine the values of the wanted data
        Favorites taskEntry = mTaskEntries.get(position);
        String titleFavorities = taskEntry.getTitleFavorites();


        //Set values
        holder.favoriteTitle.setText(titleFavorities);

        //Handle Editing Database Entry
        holder.arrow.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                int elementId = mTaskEntries.get(position).getId();
                Intent intent = new Intent(mContext, NowPlayingFavoritesActivity.class);
                intent.putExtra(NowPlayingFavoritesActivity.EXTRA_TASK_ID, elementId);
                mContext.startActivity(intent);
            }
        });


    }



    /**
     * Returns the number of items to display.
     */
    @Override
    public int getItemCount() {
        if (mTaskEntries == null) {
            return 0;
        }
        return mTaskEntries.size();
    }

    /**Used for uodating/deleting database information**/
    public List<Favorites> getTasks() {
        return mTaskEntries;
    }

    /**
     * When data changes, this method updates the list of taskEntries
     * and notifies the adapter to use the new values on it
     */
    public void setTasks(List<Favorites> taskEntries) {
        mTaskEntries = taskEntries;
        notifyDataSetChanged();
    }


    // Inner class for creating ViewHolders
    class FavoritesViewHolder extends RecyclerView.ViewHolder {

        // Class variables for the task description and priority TextViews
        TextView favoriteTitle;
        TextView favoriteUrl;
        Button arrow;


        /**
         * Constructor for the FavoritesViewHolders.
         *
         * @param itemView The view inflated in onCreateViewHolder
         */
        public FavoritesViewHolder(View itemView) {
            super(itemView);

            favoriteTitle = itemView.findViewById(R.id.favorite_title);
            favoriteUrl = itemView.findViewById(R.id.favorite_url);
            arrow= itemView.findViewById(R.id.favorite_listen_button);
        }
    }
}
