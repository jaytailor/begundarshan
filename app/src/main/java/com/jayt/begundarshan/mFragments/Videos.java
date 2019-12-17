package com.jayt.begundarshan.mFragments;

import android.os.AsyncTask;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jayt.begundarshan.R;
import com.jayt.begundarshan.SplashActivity;
import com.jayt.begundarshan.adapter.VideoAdapter;
import com.jayt.begundarshan.common.Function;

public class Videos extends Fragment{
    View view;

    // Recycler View Field
    RecyclerView recyclerView;

    // swipe up to refresh
    private SwipeRefreshLayout swipeContainer;

    public Videos() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.videos, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(10);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        new GetVideoList().execute("norefresh");

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.videoSwipeContainer);

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                new GetVideoList().execute("refresh");
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        return view;
    }

    class GetVideoList extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... args) {
            // if called on pull to refresh
            if(args[0].equals("refresh"))
                Function.loadVideos();

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
                    VideoAdapter videoAdapter = new VideoAdapter(getActivity(), SplashActivity.youtubeVideos);
                    recyclerView.setAdapter(videoAdapter);
                }
            });
        }
    }
}

