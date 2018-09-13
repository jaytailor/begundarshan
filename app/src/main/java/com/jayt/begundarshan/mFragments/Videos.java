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

import java.util.Vector;

import com.jayt.begundarshan.R;
import com.jayt.begundarshan.adapter.AdsAdapter;
import com.jayt.begundarshan.adapter.VideoAdapter;
import com.jayt.begundarshan.common.Endpoints;
import com.jayt.begundarshan.common.Function;
import com.jayt.begundarshan.model.AdsList;
import com.jayt.begundarshan.model.YoutubeVideo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Videos extends Fragment{
    View view;

    // Recycler View Field
    RecyclerView recyclerView;

    // Vector for video URL
    Vector<YoutubeVideo> youtubeVideos = new Vector<YoutubeVideo>();

    public Videos() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.videos, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        new GetVideoList().execute();
        return view;
    }

    class GetVideoList extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... args) {
            String videolist = "";

            String urlParameters = "";
            try{
                videolist = Function.excuteGet(Endpoints.SERVER_URL+"getallvideos", urlParameters);

                if(videolist == null){
                    Toast.makeText(getActivity(),"No Videos returned from server...Try after sometime...",
                            Toast.LENGTH_SHORT).show();
                }

                if(videolist.length()>10){ // Just checking if not empty

                    try {
                        //Load video List but clear from earlier call
                        youtubeVideos.clear();

                        JSONObject jsonResponse = new JSONObject(videolist);
                        JSONArray jsonArray = jsonResponse.optJSONArray("video_list");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            YoutubeVideo video = new YoutubeVideo();

                            video.setTitle(jsonObject.getString("title"));
                            video.setUrl(jsonObject.getString("url"));
                            video.setVideo_date(jsonObject.getString("video_date"));

                            youtubeVideos.add(i, video);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }catch (RuntimeException e){
                e.printStackTrace();
            }

            return videolist;
        }

        @Override
        protected void onPostExecute(String xml) {

            // updating UI from Background Thread
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    VideoAdapter videoAdapter = new VideoAdapter(getActivity(), youtubeVideos);
                    recyclerView.setAdapter(videoAdapter);
                }
            });
        }
    }
}

