package com.jayt.begundarshan.mFragments;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Debug;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.jayt.begundarshan.SplashActivity;
import com.jayt.begundarshan.common.Endpoints;
import com.jayt.begundarshan.interfaces.BaseModel;
import com.jayt.begundarshan.model.Constants;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.jayt.begundarshan.R;
import com.jayt.begundarshan.adapter.CustomAdapter;
import com.jayt.begundarshan.common.Function;
import com.jayt.begundarshan.model.AdsList;
import com.jayt.begundarshan.model.NewsItems;

public class News extends Fragment {

    // Progress Dialog
    private ProgressDialog pDialog;

    // swipe up to refresh
    private SwipeRefreshLayout swipeContainer;

    // Recycler View Field
    RecyclerView newsRecyclerView;

    public News() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.news, container, false);

        newsRecyclerView = (RecyclerView) rootView.findViewById(R.id.newsRecyclerView);
        newsRecyclerView.setHasFixedSize(true);
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        new DownloadNews().execute("norefresh");

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.newsSwipeContainer);

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                new DownloadNews().execute("refresh");
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        return rootView;
    }

    class DownloadNews extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading News ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

        }
        protected String doInBackground(String... args) {
            String news = "";

            try{
                // if called on pull to refresh
                if(args[0].equals("refresh"))
                    Function.loadNews();

                return args[0];

            }catch (RuntimeException e){
                e.printStackTrace();
            }finally {
                // dismiss the dialog after getting all products
                pDialog.dismiss();
            }
            return news;
        }

        @Override
        protected void onPostExecute(String response) {

            // Now we call setRefreshing(false) to signal refresh has finished
            if(response.equals("refresh"))
                swipeContainer.setRefreshing(false);
            else
                // dismiss the dialog after getting all products
                pDialog.dismiss();

            // updating UI from Background Thread
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    CustomAdapter adapter = new CustomAdapter(getActivity(), SplashActivity.newsList);
                    newsRecyclerView.setAdapter(adapter);
                }
            });
        }
    }


}
