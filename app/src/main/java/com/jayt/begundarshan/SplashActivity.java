package com.jayt.begundarshan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.jayt.begundarshan.adapter.EditorialAdapter;
import com.jayt.begundarshan.common.Endpoints;
import com.jayt.begundarshan.common.Function;
import com.jayt.begundarshan.interfaces.BaseModel;
import com.jayt.begundarshan.model.AdsList;
import com.jayt.begundarshan.model.EditorialModel;
import com.jayt.begundarshan.model.YoutubeVideo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by Heeya Softwares
 */

public class SplashActivity extends Activity {


    // Vector for articles
    public static Vector<BaseModel> articleList = new Vector<>();

    // Vector for video URL
    public static Vector<YoutubeVideo> youtubeVideos = new Vector<YoutubeVideo>();

    // List for ads
    public static ArrayList<AdsList> dataList = new ArrayList<AdsList>();
    public static ArrayList<AdsList> topAdsList = new ArrayList<AdsList>();

    public SplashActivity() {
    }

    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashfile);

        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                // get all the data from server while starting activity
                new GetArticleList().execute();

                Intent intent=new Intent(SplashActivity.this, MainActivity.class);
//                intent.putExtra("ads", dataList);
//                intent.putExtra("articles", articleList);
//                intent.putExtra("videos", youtubeVideos);
                startActivity(intent);
                finish();
            }
        },3000);


    }

    class GetArticleList extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... args) {
            String articleResponse = "", videoResponse = "", adResponse = "", topAds= "";

            String urlParameters = "";
            try{
                articleResponse = Function.excuteGet(Endpoints.SERVER_URL+"getalleditorial", urlParameters);
                videoResponse = Function.excuteGet(Endpoints.SERVER_URL+"getallvideos", urlParameters);
                adResponse = Function.excuteGet(Endpoints.SERVER_URL+"ads", urlParameters);
                topAds = Function.excuteGet(Endpoints.SERVER_URL+"topads?priority=5", urlParameters);

                if(articleResponse == null && videoResponse == null || adResponse == null){
                    Toast.makeText(SplashActivity.this,"Seems like server is down...Try after sometime...",
                            Toast.LENGTH_SHORT).show();
                    Log.d("Splash Activity: ","Seems like server is down...Try after sometime...");
                }

                if(adResponse != null && adResponse.length()>10){ // Just checking if not empty

                    try {
                        // Make sure to clear previously populated list of ads
                        dataList.clear();

                        JSONObject jsonResponse = new JSONObject(adResponse);
                        JSONArray jsonArray = jsonResponse.optJSONArray("campaigns");

                        // only proceed if ads are returned
                        if(jsonArray != null){
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                AdsList adsitems = new AdsList();

                                adsitems.setImageurl(jsonObject.getString("imageurl"));
                                adsitems.setPriority(jsonObject.getString("priority"));

                                dataList.add(i, adsitems);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if(topAds != null && topAds.length()>10){ // Just checking if not empty

                    try {
                        // Make sure to clear previously populated list of ads
                        topAdsList.clear();

                        JSONObject jsonResponse = new JSONObject(adResponse);
                        JSONArray jsonArray = jsonResponse.optJSONArray("campaigns");

                        // only proceed if ads are returned
                        if(jsonArray != null){
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                AdsList adsitems = new AdsList();

                                adsitems.setImageurl(jsonObject.getString("imageurl"));
                                adsitems.setPriority(jsonObject.getString("priority"));

                                topAdsList.add(i, adsitems);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if(articleResponse != null && articleResponse.length()>10){ // Just checking if not empty

                    try {
                        //Load editorial List but clear from earlier call
                        articleList.clear();

                        JSONObject jsonResponse = new JSONObject(articleResponse);
                        JSONArray jsonArray = jsonResponse.optJSONArray("article_list");

                        // only proceed if article are returned
                        if(jsonArray != null){
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                EditorialModel article = new EditorialModel();

                                article.setEditorial_title(jsonObject.getString("title"));
                                article.setEditorial_content(jsonObject.getString("content"));
                                article.setEditorial_writer(jsonObject.getString("writer"));
                                article.setEditorial_image(jsonObject.getString("image"));
                                article.setEditorial_published_at(jsonObject.getString("published_at"));

                                articleList.add(i, article);

                                // Add an ad at 2nd position
                                if (SplashActivity.dataList.size() >= 2){
                                    if (i == 0){
                                        articleList.add(i, SplashActivity.dataList.get(0));
                                    }

                                    if (i == 2){
                                        articleList.add(i, SplashActivity.dataList.get(1));
                                    }
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if(videoResponse != null && videoResponse.length()>10){ // Just checking if not empty

                    try {
                        //Load video List but clear from earlier call
                        youtubeVideos.clear();

                        JSONObject jsonResponse = new JSONObject(videoResponse);
                        JSONArray jsonArray = jsonResponse.optJSONArray("video_list");

                        // only proceed if videos are returned
                        if(jsonArray != null) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                YoutubeVideo video = new YoutubeVideo();

                                video.setTitle(jsonObject.getString("title"));
                                video.setUrl(jsonObject.getString("url"));
                                video.setVideo_date(jsonObject.getString("video_date"));

                                youtubeVideos.add(i, video);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }catch (RuntimeException e){
                e.printStackTrace();
            }

            return articleResponse;
        }

        @Override
        protected void onPostExecute(String xml) {

            Toast.makeText(SplashActivity.this,"Starting Begun Darshan...",
                    Toast.LENGTH_SHORT).show();
            // updating UI from Background Thread
//            SplashActivity.this.runOnUiThread(new Runnable() {
//                public void run() {
//                    EditorialAdapter editorialAdapterAdapter = new EditorialAdapter(SplashActivity.this, articleList);
//                    recyclerView.setAdapter(editorialAdapterAdapter);
//                }
//            });
        }
    }

}
