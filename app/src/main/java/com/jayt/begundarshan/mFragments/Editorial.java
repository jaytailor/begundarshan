package com.jayt.begundarshan.mFragments;


import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jayt.begundarshan.R;
import com.jayt.begundarshan.SplashActivity;
import com.jayt.begundarshan.adapter.EditorialAdapter;
import com.jayt.begundarshan.adapter.VideoAdapter;
import com.jayt.begundarshan.common.Endpoints;
import com.jayt.begundarshan.common.Function;
import com.jayt.begundarshan.model.EditorialModel;
import com.jayt.begundarshan.model.YoutubeVideo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Vector;

public class Editorial extends Fragment{

    View view;

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

        new GetArticleList().execute();

        return view;
    }

    class GetArticleList extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... args) {
            String articles = "";

            return articles;
        }

        @Override
        protected void onPostExecute(String xml) {

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
