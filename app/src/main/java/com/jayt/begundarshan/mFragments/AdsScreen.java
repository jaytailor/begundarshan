package com.jayt.begundarshan.mFragments;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

    public AdsScreen() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.ads_screen, container, false);

        adsRecyclerView = (RecyclerView) view.findViewById(R.id.adsRecyclerView);
        adsRecyclerView.setHasFixedSize(true);
        adsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        new ShowAds().execute("refresh");

        return view;
    }


    class ShowAds extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        protected String doInBackground(String... args) {
            String ads = "";

            return ads;
        }

        @Override
        protected void onPostExecute(String xml) {

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

