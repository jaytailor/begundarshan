package com.jayt.begundarshan.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;
import android.widget.Toast;

import com.jayt.begundarshan.SplashActivity;
import com.jayt.begundarshan.model.AdsList;
import com.jayt.begundarshan.model.EditorialModel;
import com.jayt.begundarshan.model.NewsItems;
import com.jayt.begundarshan.model.SurveyModel;
import com.jayt.begundarshan.model.WishMessages;
import com.jayt.begundarshan.model.YoutubeVideo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Function {

    public static boolean isNetworkAvailable(Context context)
    {
        return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }

    public static String excuteGet(String targetURL, String urlParameters)
    {
        URL url;
        HttpURLConnection connection = null;
        try {
            //Create connection
            url = new URL(targetURL);
            connection = (HttpURLConnection)url.openConnection();
            //connection.setRequestMethod("POST");
            //connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("content-type", "application/json;  charset=utf-8");
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches (false);
            connection.setDoInput(true);
            connection.setDoOutput(false);

            InputStream is;

            int status = connection.getResponseCode();

            if (status != HttpURLConnection.HTTP_OK) {
                is = connection.getErrorStream();
                Log.d("MYLOG", "ERROR! Response status is: " + status);
            }
            else
                is = connection.getInputStream();

            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if(connection != null) {
                connection.disconnect();
            }
        }
    }

    public static String executePost(String myUrl, JSONObject postJson) throws IOException, JSONException {
        String result = "";
        HttpURLConnection conn = null;

        try{
            URL url = new URL(myUrl);

            // 1. create HttpURLConnection
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");

            // 2. build JSON object
            JSONObject jsonObject = postJson;

            // 3. add JSON content to POST request body
            setPostRequestContent(conn, jsonObject);

            // 4. make POST request to the given URL
            conn.connect();

            int status = conn.getResponseCode();
            InputStream is;

            if (status == HttpURLConnection.HTTP_OK || status == HttpURLConnection.HTTP_CREATED) {
                is = conn.getInputStream();
                result = "OK";
            }
            else{
                is = conn.getErrorStream();
                result = "FAIL";
                Log.d("MYLOG", "ERROR! Response status is: " + status);
            }

            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }

            // close response body
            rd.close();

            // 5. return response message
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if(conn != null) {
                conn.disconnect();
            }
        }

    }

    private static void setPostRequestContent(HttpURLConnection conn,
                                              JSONObject jsonObject) throws IOException {

        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(jsonObject.toString());
        writer.flush();
        writer.close();
        os.close();
    }

    public static String loadArticles(){

        String articleResponse = "";
        String urlParameters = "";
        int numOfObj = 0;

        try{
            articleResponse = Function.excuteGet(Endpoints.SERVER_URL+"getalleditorial", urlParameters);

            if(articleResponse != null && articleResponse.length()>10){ // Just checking if not empty

                try {
                    //Load editorial List but clear from earlier call
                    SplashActivity.articleList.clear();

                    JSONObject jsonResponse = new JSONObject(articleResponse);
                    JSONArray jsonArray = jsonResponse.optJSONArray("article_list");

                    if (SplashActivity.orderedAdList.size() >= 2) {
                            SplashActivity.articleList.add(0, SplashActivity.orderedAdList.get(0));
                            numOfObj++;
                    }

                        // only proceed if article are returned
                    if(jsonArray != null){
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            EditorialModel article = new EditorialModel();

                            article.setEditorial_title(jsonObject.getString("title"));
                            article.setEditorial_content(jsonObject.getString("content"));
                            article.setEditorial_writer(jsonObject.getString("writer"));

                            JSONArray imageArray = jsonObject.getJSONArray("image");
                            if(imageArray != null) {
                                ArrayList<String> listOfImages = new ArrayList<>();

                                // Load all the images from the news
                                for (int k = 0; k < imageArray.length(); k++) {
                                    listOfImages.add(k, imageArray.getString(k));
                                }

                                article.setEditorial_image(listOfImages);
                            }

                            article.setEditorial_published_at(jsonObject.getString("published_at"));

                            SplashActivity.articleList.add(numOfObj, article);
                            numOfObj++;
                        }

                        if (SplashActivity.orderedAdList.size() >= 2) {
                            SplashActivity.articleList.add(numOfObj, SplashActivity.orderedAdList.get(1));
                            numOfObj++;
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

    public static String loadRssFeed(){

        String rssResponse = "";
        String urlParameters = "";
        int numOfObj = 0;
        Exception exception = null;

        try{
            URL url = new URL(Endpoints.RSS_URL);
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(false);


            rssResponse = Function.excuteGet(Endpoints.RSS_URL, urlParameters);

            if(rssResponse != null && rssResponse.length()>10){ // Just checking if not empty

                try {
                    //Load editorial List but clear from earlier call
                    SplashActivity.rssFeedList.clear();

                    JSONObject jsonResponse = new JSONObject(articleResponse);
                    JSONArray jsonArray = jsonResponse.optJSONArray("article_list");

                    if (SplashActivity.orderedAdList.size() >= 2) {
                        SplashActivity.articleList.add(0, SplashActivity.orderedAdList.get(0));
                        numOfObj++;
                    }

                    // only proceed if article are returned
                    if(jsonArray != null){
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            EditorialModel article = new EditorialModel();

                            article.setEditorial_title(jsonObject.getString("title"));
                            article.setEditorial_content(jsonObject.getString("content"));
                            article.setEditorial_writer(jsonObject.getString("writer"));

                            JSONArray imageArray = jsonObject.getJSONArray("image");
                            if(imageArray != null) {
                                ArrayList<String> listOfImages = new ArrayList<>();

                                // Load all the images from the news
                                for (int k = 0; k < imageArray.length(); k++) {
                                    listOfImages.add(k, imageArray.getString(k));
                                }

                                article.setEditorial_image(listOfImages);
                            }

                            article.setEditorial_published_at(jsonObject.getString("published_at"));

                            SplashActivity.articleList.add(numOfObj, article);
                            numOfObj++;
                        }

                        if (SplashActivity.orderedAdList.size() >= 2) {
                            SplashActivity.articleList.add(numOfObj, SplashActivity.orderedAdList.get(1));
                            numOfObj++;
                        }
                    }
                } catch (MalformedURLException e) {
                    exception = e;
                }
            }

        }catch (RuntimeException e){
            e.printStackTrace();
        }
        catch (XmlPullParserException e) {
            exception = e;
        }
        catch (IOException e) {
            exception = e;
        }
        return articleResponse;
    }

    public static String loadVideos(){
        String videoResponse = "", urlParameters = "";

        try{
            videoResponse = Function.excuteGet(Endpoints.SERVER_URL+"getallvideos", urlParameters);

            if(videoResponse != null && videoResponse.length()>10){ // Just checking if not empty

                try {
                    //Load video List but clear from earlier call
                    SplashActivity.youtubeVideos.clear();

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

                            SplashActivity.youtubeVideos.add(i, video);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }catch (RuntimeException e){
            e.printStackTrace();
        }
        return videoResponse;
    }

    public static String loadAds(String route){
        String adResponse = "", urlParameters = "";

        try{
            adResponse = Function.excuteGet(Endpoints.SERVER_URL+route, urlParameters);
            if(adResponse != null && adResponse.length()>10){ // Just checking if not empty

                try {
                    // Make sure to clear previously populated list of ads
                    if(route.equals("ads"))
                        SplashActivity.orderedAdList.clear(); // if ordered ads
                    else
                        SplashActivity.topAdsList.clear(); // if top ads for main page

                    JSONObject jsonResponse = new JSONObject(adResponse);
                    JSONArray jsonArray = jsonResponse.optJSONArray("campaigns");

                    // only proceed if ads are returned
                    if(jsonArray != null){
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            AdsList adsitems = new AdsList();

                            adsitems.setImageurl(jsonObject.getString("imageurl"));
                            adsitems.setPriority(jsonObject.getString("priority"));

                            if(route.equals("ads"))
                                // if ordered ads then ad in ordered ad list
                                SplashActivity.orderedAdList.add(i, adsitems);
                            else
                                // if top ads for main page
                                SplashActivity.topAdsList.add(i, adsitems);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }catch (RuntimeException e){
            e.printStackTrace(); }

        return adResponse;
    }

    public static String loadNews() {
        String newsResponse = "", urlParameters = "";
        int numOfObj = 0;

        newsResponse = Function.excuteGet(Endpoints.SERVER_URL+"getallnews?list=15", urlParameters);

        if(newsResponse != null && newsResponse.length()>10){ // Just checking if not empty

            try {
                SplashActivity.newsList.clear();
                JSONObject jsonResponse = new JSONObject(newsResponse);
                JSONArray jsonArray = jsonResponse.optJSONArray("newsitems");

                // First insert the ads at first and fifth position
                // But make sure not to add news at those indexes (0, 5)
                if (SplashActivity.topAdsList.size() >= 2){
                    SplashActivity.newsList.add(0, SplashActivity.topAdsList.get(0));
                    numOfObj++;
                }

                // Now add survey message if there is any
                if (SplashActivity.surveyItem.size() > 0){
                    SplashActivity.newsList.add(numOfObj, SplashActivity.surveyItem.get(0));
                    numOfObj++;
                }

                // Start from one as already have ad at zero and insert news for next
                // five places if news are more than five else run till the length of jsonArray
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    NewsItems newsitems = new NewsItems();
                    newsitems.setTitle(jsonObject.getString("title"));
                    newsitems.setContent(jsonObject.getString("content"));
                    newsitems.setWriter(jsonObject.getString("writer"));

                    JSONArray imageArray = jsonObject.getJSONArray("image");
                    if(imageArray != null){
                        ArrayList<String> listOfImages = new ArrayList<>();

                        // Load all the images from the news
                        for(int k = 0; k < imageArray.length(); k++){
                            listOfImages.add(k, imageArray.getString(k));
                        }
                        newsitems.setImage(listOfImages);
                    }

                    newsitems.setPublished_at(jsonObject.getString("published_at"));
                    newsitems.setIs_breaking(jsonObject.getString("is_breaking"));
                    SplashActivity.newsList.add(numOfObj, newsitems);
                    numOfObj++;
                }

                // add wish messages before showing final ad
                if(SplashActivity.wishContainer != null){

                    SplashActivity.newsList.add(numOfObj, SplashActivity.wishContainer);
                    numOfObj++;
                }

                // add an ad at the end
                if (SplashActivity.topAdsList.size() >= 2) {
                    SplashActivity.newsList.add(numOfObj, SplashActivity.topAdsList.get(1));
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            catch (ArrayIndexOutOfBoundsException ex){
                ex.printStackTrace();
            }
        }

        return newsResponse;
    }

    public static String loadWishMessage(){
        String wishesResponse = "";

        try{
            wishesResponse = Function.excuteGet(Endpoints.SERVER_URL+"getallwishes", "");
            if(wishesResponse != null && wishesResponse.length()>10){ // Just checking if not empty

                try {
                    // Make sure to clear previously populated list of wish message
                    SplashActivity.wishMessages.clear();

                    JSONObject jsonResponse = new JSONObject(wishesResponse);
                    JSONArray jsonArray = jsonResponse.optJSONArray("messages");

                    // only proceed if ads are returned
                    if(jsonArray != null){
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            WishMessages message = new WishMessages();

                            message.setImageurl(jsonObject.getString("imageurl"));
                            message.setMessage(jsonObject.getString("message_text"));
                            SplashActivity.wishMessages.add(i, message);
                        }
                    }

                    // now make sure that you set it in parent wish messages list
                    SplashActivity.wishContainer.setWishMessageList(SplashActivity.wishMessages);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }catch (RuntimeException e){
            e.printStackTrace();
        }

        return wishesResponse;
    }

    public static String loadSurvey(){
        String surveyResponse = "";

        try{
            surveyResponse = Function.excuteGet(Endpoints.SERVER_URL+"getsurveyresult", "");
            if(surveyResponse != null && surveyResponse.length()>10){ // Just checking if not empty

                try {
                    // Make sure to clear previously populated list of wish message
                    SplashActivity.surveyItem.clear();

                    JSONObject jsonResponse = new JSONObject(surveyResponse);
                    JSONArray jsonArray = jsonResponse.optJSONArray("survey");

                    // only proceed if ads are returned
                    if(jsonArray != null){
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            SurveyModel surveyModel = new SurveyModel();

                            surveyModel.setId(jsonObject.getString("id"));
                            surveyModel.setSurveyTitle(jsonObject.getString("survey_text"));
                            surveyModel.setYes(jsonObject.getInt("yes"));
                            surveyModel.setNo(jsonObject.getInt("no"));

                            SplashActivity.surveyItem.add(i, surveyModel);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }catch (RuntimeException e){
            e.printStackTrace();
        }

        return surveyResponse;
    }
}
