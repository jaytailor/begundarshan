package com.jayt.begundarshan.mFragments;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jayt.begundarshan.R;
import com.jayt.begundarshan.SplashActivity;
import com.jayt.begundarshan.adapter.CustomAdapter;
import com.jayt.begundarshan.adapter.EditorialAdapter;
import com.jayt.begundarshan.adapter.VideoAdapter;
import com.jayt.begundarshan.common.Endpoints;
import com.jayt.begundarshan.common.Function;
import com.jayt.begundarshan.model.EditorialModel;
import com.jayt.begundarshan.model.NewsItems;
import com.jayt.begundarshan.model.YoutubeVideo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Vector;

public class Editorial extends Fragment{

    View view;

    // swipe up to refresh
    private SwipeRefreshLayout swipeContainer;

    // Recycler View Field
    RecyclerView recyclerView;

    public Editorial() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.editorial, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.editorialView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        new GetArticleList().execute("norefresh");

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                new GetArticleList().execute("refresh");
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        return view;
    }

    class GetArticleList extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... args) {

            // if called on pull to refresh
            if(args[0].equals("refresh"))
                Function.loadArticles();

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
                    EditorialAdapter editorialAdapterAdapter = new EditorialAdapter(getActivity(), SplashActivity.articleList);
                    recyclerView.setAdapter(editorialAdapterAdapter);
                }
            });
        }
    }

}
