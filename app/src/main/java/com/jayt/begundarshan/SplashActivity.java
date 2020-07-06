package com.jayt.begundarshan;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.jayt.begundarshan.common.Function;
import com.jayt.begundarshan.interfaces.BaseModel;
import com.jayt.begundarshan.model.AdsList;
import com.jayt.begundarshan.model.BreakingNews;
import com.jayt.begundarshan.model.SurveyModel;
import com.jayt.begundarshan.model.WishMessageParentModel;
import com.jayt.begundarshan.model.WishMessages;
import com.jayt.begundarshan.model.YoutubeVideo;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by Heeya Softwares
 */

public class SplashActivity extends Activity {

    // Vector for News
    public static ArrayList<BaseModel> newsList = new ArrayList<>();

    // Vector for First News (just one item)
    public static ArrayList<BaseModel> firstNewsList = new ArrayList<>();

    // Vector for articles
    public static Vector<BaseModel> articleList = new Vector<>();

    // Vector for video URL
    public static ArrayList<YoutubeVideo> youtubeVideos = new ArrayList<YoutubeVideo>();

    // List for ads
    public static ArrayList<AdsList> orderedAdList = new ArrayList<AdsList>();
    public static ArrayList<AdsList> topAdsList = new ArrayList<AdsList>();

    // List for wish messages
    public static ArrayList<WishMessages> wishMessages = new ArrayList<WishMessages>();
    public static WishMessageParentModel wishContainer = new WishMessageParentModel();

    // List for videos on main
    //public static VideosParentModel videoContainer = new VideosParentModel();

    // Load surveys
    public static ArrayList<SurveyModel> surveyItem = new ArrayList<SurveyModel>();

    // Load breaking news
    public static ArrayList<BreakingNews> breakingNewsList = new ArrayList<BreakingNews>();

    // Load google admob interstitial ads
    public static InterstitialAd interstitialAd = null;

    public SplashActivity() {
    }

    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashfile);

        // Initialise google ads
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                System.out.println("Ad mob initialised");
                interstitialAd= new InterstitialAd(SplashActivity.this);
                interstitialAd.setAdUnitId(getString(R.string.interstitialAdId));
                AdRequest adRequest = new AdRequest.Builder().build();
                interstitialAd.loadAd(adRequest);
            }
        });

        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                // get all the data from server while starting activity
                new LoadEntities().execute();
                Intent intent=new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);

                finish();
            }
        },3000);


    }

    class LoadEntities extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... args) {
            String response = "SUCCESS";
            try{

                // Load survey (if any)
                Function.loadSurvey();

                // Load breaking news (if any)
                Function.loadBreakingNews();

                // Load ordered ads for ads tab
                Function.loadAds("ads");

                // Load top ads for main news page
                Function.loadAds("topads?priority=5");

                // Load Videos
                Function.loadVideos();

                // Load wish messages
                Function.loadWishMessage();

                // Load news now
                Function.loadNews();

                // Load articles
                Function.loadArticles();

            }catch (RuntimeException e){
                e.printStackTrace();
                Toast.makeText(SplashActivity.this,"Seems like server is down...Try after sometime...",
                            Toast.LENGTH_SHORT).show();
                response = "FAIL";
            }

            return response;
        }

        @Override
        protected void onPostExecute(String xml) {

            Toast.makeText(SplashActivity.this,"Starting Begun Darshan...",
                    Toast.LENGTH_SHORT).show();
        }
    }

}
