package com.jayt.begundarshan.mFragments;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jayt.begundarshan.R;
import com.jayt.begundarshan.SplashActivity;
import com.jayt.begundarshan.adapter.EditorialAdapter;
import com.jayt.begundarshan.adapter.RssViewAdapter;
import com.jayt.begundarshan.common.Function;


public class RssFeed extends Fragment {
    View view;

    // swipe up to refresh
    private SwipeRefreshLayout swipeContainer;

    // Recycler View Field
    RecyclerView recyclerView;

    public RssFeed() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.rssfeed, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.rssView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        new GetRssFeed().execute("norefresh");

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.rssSwipeContainer);

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                new GetRssFeed().execute("refresh");
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        return view;
    }

    class GetRssFeed extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... args) {

            // if called on pull to refresh
            if(args[0].equals("refresh"))
                Function.loadRssFeed();
            return args[0];
        }

        @Override
        protected void onPostExecute(String response) {

            // Now we call setRefreshing(false) to signal refresh has finished
            if(response.equals("refresh"))
                swipeContainer.setRefreshing(false);

            // updating UI from Background Thread
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    RssViewAdapter rssAdapter = new RssViewAdapter(getActivity(), SplashActivity.rssFeedList);
                    recyclerView.setAdapter(rssAdapter);
                }
            });
        }
    }
}
