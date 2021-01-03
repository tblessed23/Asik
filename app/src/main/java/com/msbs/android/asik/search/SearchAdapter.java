package com.msbs.android.asik.search;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.msbs.android.asik.R;

import com.msbs.android.asik.model.Story;


public class SearchAdapter extends PagedListAdapter<Story, SearchAdapter.SearchViewHolder> {
    private static final String LOG_TAG = SearchAdapter.class.getSimpleName();
    private final SearchClickListener mSearchClickListener;



    public SearchAdapter( Activity activity, SearchClickListener listener) {
        super(Story.DIFF_CALLBACK);
        this.mSearchClickListener = listener;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.list_search,
                parent,
                false   );
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {

        if (position <= -1) {  return; }
        Story stories = getItem(position);
        final String storystate = stories.getStorystate();
        final String storycity = stories.getStorycity();
        final String storytitle = stories.getAudiotitle();




        try {holder.txtState.setText(storystate);
            holder.txtCity.setText(storycity);
            holder.txtTitle.setText(storytitle);

        } catch (Exception e) {
            e.printStackTrace();  }

    }

    public interface SearchClickListener {
        void onSearchClickListener(int itemId);
    }



    public class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView txtState;
        private final TextView txtCity;
        private final TextView txtTitle;


        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTitle=itemView.findViewById(R.id.searchstorytitletext_view);
            txtState = itemView.findViewById(R.id.searchstorystatetext_view);
            txtCity= itemView.findViewById(R.id.searchstorycitytext_view);
            TextView txtPlaySearch = itemView.findViewById(R.id.search_play_textview);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int elementId = getItem(getAdapterPosition()).getPrimaryId();;
            mSearchClickListener.onSearchClickListener(elementId);
        }
    }
}


