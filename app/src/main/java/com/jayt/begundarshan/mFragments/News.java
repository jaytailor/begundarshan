package com.jayt.begundarshan.mFragments;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Debug;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
    
    ArrayList<BaseModel> newsList = new ArrayList<>();

    // Progress Dialog
    private ProgressDialog pDialog;

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

        new DownloadNews().execute();

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

            String urlParameters = "";

            try{
                news = Function.excuteGet(Endpoints.SERVER_URL+"getallnews?list=20", urlParameters);

                if(news == null){
                    Toast.makeText(getActivity(),"No news returned from server...",
                            Toast.LENGTH_SHORT).show();
                    Log.d("News.java: ","No news returned from server...");
                }

                if(news != null && news.length()>10){ // Just checking if not empty

                    try {
                        newsList.clear();
                        JSONObject jsonResponse = new JSONObject(news);
                        JSONArray jsonArray = jsonResponse.optJSONArray("newsitems");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            NewsItems newsitems = new NewsItems();

                            newsitems.setTitle(jsonObject.getString("title"));
                            newsitems.setContent(jsonObject.getString("content"));
                            newsitems.setWriter(jsonObject.getString("writer"));
                            newsitems.setImage(jsonObject.getString("image"));
                            newsitems.setPublished_at(jsonObject.getString("published_at"));
                            newsitems.setIs_breaking(jsonObject.getString("is_breaking"));
                            newsList.add(i, newsitems);

                            // Only put ad (fetched from splash activity ads list) at first and 5th place
                            if (i == 0){
                                newsList.add(i, SplashActivity.dataList.get(0));
                            }

                            if (i == 5){
                                newsList.add(i, SplashActivity.dataList.get(1));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }catch (RuntimeException e){
                e.printStackTrace();
            }finally {
                // dismiss the dialog after getting all products
                pDialog.dismiss();
            }
            return news;
        }

        @Override
        protected void onPostExecute(String xml) {

            // updating UI from Background Thread
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    CustomAdapter adapter = new CustomAdapter(getActivity(), newsList);
                    newsRecyclerView.setAdapter(adapter);
                }
            });
        }
    }


}
