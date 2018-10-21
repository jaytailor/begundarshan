package com.jayt.begundarshan.mFragments;


import android.app.ProgressDialog;
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
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.jayt.begundarshan.R;
import com.jayt.begundarshan.SplashActivity;
import com.jayt.begundarshan.adapter.AdsAdapter;
import com.jayt.begundarshan.common.Endpoints;
import com.jayt.begundarshan.common.Function;
import com.jayt.begundarshan.model.AdsList;

public class AdsScreen extends Fragment {
    View view;

    // Recycler View Field
    RecyclerView adsRecyclerView;

    // swipe up to refresh
    private SwipeRefreshLayout swipeContainer;

    public AdsScreen() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.ads_screen, container, false);

        adsRecyclerView = (RecyclerView) view.findViewById(R.id.adsRecyclerView);
        adsRecyclerView.setHasFixedSize(true);
        adsRecyclerView.setItemViewCacheSize(20);
        adsRecyclerView.setDrawingCacheEnabled(true);
        adsRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        adsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        new ShowAds().execute("norefresh");

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.adsSwipeContainer);

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                new ShowAds().execute("refresh");
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        return view;
    }


    class ShowAds extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        protected String doInBackground(String... args) {
            // if called on pull to refresh
            if(args[0].equals("refresh"))
                Function.loadAds("ads");

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
                    AdsAdapter adapter = new AdsAdapter(getActivity(), SplashActivity.orderedAdList);
                    adsRecyclerView.setAdapter(adapter);

                }
            });
        }
    }

}

