package com.jayt.begundarshan.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ceylonlabs.imageviewpopup.ImagePopup;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.jayt.begundarshan.common.Endpoints;
import com.jayt.begundarshan.common.Function;
import com.jayt.begundarshan.holder.BaseViewHolder;
import com.jayt.begundarshan.interfaces.BaseModel;
import com.jayt.begundarshan.model.AdsList;
import com.jayt.begundarshan.model.Constants;
import com.jayt.begundarshan.model.SurveyModel;
import com.jayt.begundarshan.model.VideosParentModel;
import com.jayt.begundarshan.model.WishMessageParentModel;
import com.jayt.begundarshan.model.WishMessages;
import com.jayt.begundarshan.model.YoutubeVideo;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.jayt.begundarshan.DetailsActivity;
import com.jayt.begundarshan.R;
import com.jayt.begundarshan.model.NewsItems;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class CustomAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private Context ctx;
    private List<? extends BaseModel> mList;
    private LayoutInflater mInflator;

    private TextView newsTitle, content, published_at, surveyTitle, surveyResult, videoTitle;
    private ImageView newsImg, mainAd, playButton;
    private ImagePopup imagePopup;
    private ImageButton yesButton, noButton;
    private LinearLayout yesNoLayout;

    // video
    private List<YoutubeVideo> youtubeVideos;

    public CustomAdapter(Context ctx, List<? extends BaseModel> list) {
        this.ctx = ctx;
        this.mList = list;
        this.mInflator = LayoutInflater.from(ctx);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        switch (viewType) {
            case Constants.ViewType.NEWS_TYPE:
                return new NewsViewHolder(mInflator.inflate(R.layout.news_items, parent, false));
            case Constants.ViewType.AD_TYPE:
                return new AdsViewHolder(mInflator.inflate(R.layout.news_page_ad, parent, false));
            case Constants.ViewType.WISH_TYPE:
                return new WishMessageViewHolder(mInflator.inflate(R.layout.news_page_wishes, parent, false));
            case Constants.ViewType.SURVEY_TYPE:
                return new SurveyViewHolder(mInflator.inflate(R.layout.survey_item, parent, false));
            case Constants.ViewType.VIDEO_TYPE:
                return new MainVideoViewHolder(mInflator.inflate(R.layout.video_list, parent, false));
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if (mList != null) {
            return mList.get(position).getViewType();
        }
        return 0;
    }

    public int getItemCount() {
        if (mList == null)
            return 0;
        return mList.size();
    }


    @Override
    @SuppressWarnings("unchecked")
    public void onBindViewHolder(final BaseViewHolder holder, final int position) {
        holder.bind(mList.get(position));
    }

    public class NewsViewHolder extends BaseViewHolder<NewsItems> implements View.OnClickListener{
        private NewsItems obj;

        NewsViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            newsTitle = (TextView) itemView.findViewById(R.id.newsTitle);
            content = (TextView) itemView.findViewById(R.id.newsContent);
            published_at = (TextView) itemView.findViewById(R.id.newsPublishedAt);
            newsImg = (ImageView) itemView.findViewById(R.id.newsImage);

        }

        @Override
        public void bind(NewsItems newsObject) {
            newsTitle.setText(newsObject.getTitle());
            obj = newsObject;

            // Check if it is a breaking news
            final String title;

            if(newsObject.getIs_breaking().equals("true") ){
                title = "ब्रेकिंग न्यूज़: " + newsObject.getTitle();
            }else{
                title = newsObject.getTitle();
            }

            // Change the layout if ad (will write code later for ad). or stretch image if title, content and writer are empty
            if(newsObject.getWriter().equals("") && newsObject.getContent().equals("") ){
                newsImg.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
                newsImg.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
            }else{
                newsImg.getLayoutParams().height = (int) ctx.getResources().getDimension(R.dimen.imageview_height);
                newsImg.getLayoutParams().width = (int) ctx.getResources().getDimension(R.dimen.imageview_width);
            }

            // Now set the values to view
            newsTitle.setText(title);
            content.setText(newsObject.getContent());
            published_at.setText(newsObject.getPublished_at());

            // If there are no images associated with news then show a default image from drawables
            if( newsObject.getImage() == null || newsObject.getImage().size() == 0){
                newsImg.setImageResource(R.drawable.begundarshanlogo);

            }else{

                if(newsObject.getImage().get(0).length() < 5)
                {
                    newsImg.setVisibility(View.GONE);
                }else{
                    // get first news image and load that
                    String image = newsObject.getImage().get(0);
                    Picasso.with(ctx)
                            .load(image).fit()
                            .into(newsImg);
                }
            }
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(ctx, DetailsActivity.class);
            intent.putStringArrayListExtra("image", obj.getImage());
            intent.putExtra("published_at", obj.getPublished_at());
            intent.putExtra("title", obj.getTitle());
            intent.putExtra("content", obj.getContent());
            intent.putExtra("writer", obj.getWriter());
            ctx.startActivity(intent);
        }
    }

    public class AdsViewHolder extends BaseViewHolder<AdsList> implements View.OnClickListener{

        AdsViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            mainAd = (ImageView) itemView.findViewById(R.id.mainAd);

            // Create a pop up of image if clicked
            imagePopup = new ImagePopup(ctx);
            imagePopup.setBackgroundColor(Color.DKGRAY);
            imagePopup.setFullScreen(true);
            imagePopup.setHideCloseIcon(false);
            imagePopup.setImageOnClickClose(true);
            imagePopup.setKeepScreenOn(true);
        }

        @Override
        public void bind(AdsList object) {

            if(object.getImageurl().length() < 5)
            {
                mainAd.setVisibility(View.GONE);
            }else{
                String image = object.getImageurl();
                Picasso.with(ctx)
                        .load(image).fit()
                        .into(mainAd);
                imagePopup.initiatePopupWithPicasso(image);
            }
        }

        @Override
        public void onClick(View v) {
            imagePopup.viewPopup();
        }
    }

    public class WishMessageViewHolder extends BaseViewHolder<WishMessages> implements View.OnClickListener{
        ImageView wishMessageImage;
        TextView wishMessage;

        WishMessageViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            wishMessageImage = (ImageView) itemView.findViewById(R.id.mainWishImage);
            wishMessage = (TextView) itemView.findViewById(R.id.mainWishMessageText);

            // Create a pop up of image if clicked
            imagePopup = new ImagePopup(ctx);
            imagePopup.setBackgroundColor(Color.DKGRAY);
            imagePopup.setFullScreen(true);
            imagePopup.setHideCloseIcon(false);
            imagePopup.setImageOnClickClose(true);
            imagePopup.setKeepScreenOn(true);
        }

        @Override
        public void bind(WishMessages object) {
            try{
                if(object != null ) {
                        if(object.getImageurl().length() < 5)
                        {
                            mainAd.setVisibility(View.GONE);
                        }else{
                            String image = object.getImageurl();
                            Picasso.with(ctx)
                                    .load(object.getImageurl()).fit()
                                    .into(wishMessageImage);
                            imagePopup.initiatePopupWithPicasso(image);
                        }
                        wishMessage.setText(object.getMessage());
                    }
            }
            catch(NullPointerException ex){
                ex.printStackTrace();
            }
        }

        @Override
        public void onClick(View v) {
            imagePopup.viewPopup();
        }
    }

//    old horizontal scroll view based wish messages where u can scroll through bunch of wish messages.
//    public class WishMessageViewHolder extends BaseViewHolder<WishMessageParentModel> implements View.OnClickListener{
//        LinearLayout mGallery;
//        View view;
//        ImageView wishMessageImage;
//        LayoutInflater mInflater;
//        TextView wishMessage;
//
//        WishMessageViewHolder(View itemView) {
//            super(itemView);
//
//            itemView.setOnClickListener(this);
//
//            mGallery = (LinearLayout) itemView.findViewById(R.id.id_wish_gallery);
//            mInflater = LayoutInflater.from(ctx);
//        }
//
//        @Override
//        public void bind(WishMessageParentModel object) {
//
//            // get the wish message parent object which has list of wish messages object
//            // traverse through that list and inflate a imageview for each and everyone.
//            try{
//                if(object != null) {
//                    for (int i = 0; i < object.getWishMessageList().size(); i++) {
//                        view = mInflater.inflate(R.layout.wish_items, mGallery, false);
//                        wishMessageImage = (ImageView) view.findViewById(R.id.wishImage);
//                        //wishMessage = (TextView) view.findViewById(R.id.wishMessageText);
//                        Picasso.with(ctx)
//                                .load(object.getWishMessageList().get(i).getImageurl()).fit()
//                                .into(wishMessageImage);
//
//                        //wishMessage.setText(object.getWishMessageList().get(i).getMessage());
//
//                        // add in gallery view
//                        if (view.getParent() != null)
//                            ((ViewGroup) view.getParent()).removeView(view); // <- fix
//
//                        mGallery.addView(view);
//
//                    }
//                }
//            }
//            catch(NullPointerException ex){
//                ex.printStackTrace();
//            }
//        }
//
//        @Override
//        public void onClick(View v) {
//            imagePopup.viewPopup();
//        }
//
//    }

    public class MainVideoViewHolder extends BaseViewHolder<VideosParentModel> implements View.OnClickListener{
        protected RelativeLayout relativeLayoutOverYouTubeThumbnailView;
        YouTubeThumbnailView youTubeThumbnailView;
        LinearLayout mGallery;
        View view;
        LayoutInflater mInflater;

        MainVideoViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            playButton = (ImageView)itemView.findViewById(R.id.main_youtube_btn);
            videoTitle = (TextView) itemView.findViewById(R.id.id_video_heading);
            mGallery = (LinearLayout) itemView.findViewById(R.id.id_video_gallery);

            playButton.setOnClickListener(this);
            relativeLayoutOverYouTubeThumbnailView = (RelativeLayout) itemView.findViewById(R.id.main_youtube_thumbnail);
            youTubeThumbnailView = (YouTubeThumbnailView) itemView.findViewById(R.id.main_youtube);
            mInflater = LayoutInflater.from(ctx);
        }

        @Override
        public void bind(VideosParentModel object) {

            // get the wish message parent object which has list of wish messages object
            // traverse through that list and inflate a imageview for each and everyone.
            try{
                if(object != null) {
                    for (int i = 0; i < object.getVideoList().size(); i++) {
                        view = mInflater.inflate(R.layout.video_items, mGallery, false);

                        videoTitle.setText(youtubeVideos.get(i).getTitle());

                        String fullVideoThumbnail = "http://img.youtube.com/vi/" +youtubeVideos.get(i).getUrl()+ "/hqdefault.jpg";
                        Picasso.with(ctx)
                                .load(fullVideoThumbnail).fit()
                                .into(playButton);

                        // add in gallery view
                        if (view.getParent() != null)
                            ((ViewGroup) view.getParent()).removeView(view); // <- fix

                        mGallery.addView(view);

                        final YouTubeThumbnailLoader.OnThumbnailLoadedListener  onThumbnailLoadedListener = new YouTubeThumbnailLoader.OnThumbnailLoadedListener(){
                            @Override
                            public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {

                            }

                            @Override
                            public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                                youTubeThumbnailView.setVisibility(View.VISIBLE);
                                relativeLayoutOverYouTubeThumbnailView.setVisibility(View.VISIBLE);
                            }
                        };

                        youTubeThumbnailView.initialize("AIzaSyCIl3Eqt3STpA0f6XuxRywkHRT8GEzpo70", new YouTubeThumbnailView.OnInitializedListener() {
                            @Override
                            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {

                                youTubeThumbnailLoader.setVideo(youtubeVideos.get(0).getUrl());
                                youTubeThumbnailLoader.setOnThumbnailLoadedListener(onThumbnailLoadedListener);
                                youTubeThumbnailLoader.release();
                            }

                            @Override
                            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
                                //write something for failure
                            }
                        });

                    }
                }
            }
            catch(NullPointerException ex){
                ex.printStackTrace();
            }
        }

        @Override
        public void onClick(View v) {

            if(getLayoutPosition() != -1){
                Intent intent = YouTubeStandalonePlayer.createVideoIntent((Activity) ctx,
                        "AIzaSyCIl3Eqt3STpA0f6XuxRywkHRT8GEzpo70",
                        youtubeVideos.get(getLayoutPosition()).getUrl(),
                        100,     //after this time, video will start automatically
                        true,   //autoplay or not
                        true);

                ctx.startActivity(intent);
            }
        }

    }

    public class SurveyViewHolder extends BaseViewHolder<SurveyModel> implements View.OnClickListener{
        String gId, gSurveyText;
        int gYes, gNo;
        float yesPercent, noPercent;

        SurveyViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            surveyTitle = (TextView) itemView.findViewById(R.id.id_Survey_Title);
            surveyResult = (TextView) itemView.findViewById(R.id.surveyResult);

            yesButton = (ImageButton)itemView.findViewById(R.id.yesButton);
            noButton = (ImageButton)itemView.findViewById(R.id.noButton);
            yesNoLayout = (LinearLayout)itemView.findViewById(R.id.yesNoLayout);
        }

        @Override
        public void bind(final SurveyModel object) {

            surveyTitle.setText(object.getSurveyTitle());

            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        // after it is clicked. remove control and show result
                        yesNoLayout.setVisibility(View.GONE);
                        object.setYes(object.getYes() + 1);

                        yesPercent = (object.getYes()*100)/(object.getYes()+object.getNo());
                        noPercent = (object.getNo()*100)/(object.getYes()+object.getNo());

                        String result = "हाँ: "+yesPercent+"%   नहीं: "+ noPercent+"%";

                        surveyResult.setText(result);
                        surveyResult.setVisibility(View.VISIBLE);

                        // Now make sure that you update global variable to post
                        gId = object.getId();
                        gSurveyText = object.getSurveyTitle();
                        gYes = object.getYes();
                        gNo = object.getNo();

                        // Also update the result in server
                        new postSurveyUpdate().execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        // after it is clicked. remove control and show result

                        yesNoLayout.setVisibility(View.GONE);
                        object.setNo(object.getNo() + 1);

                        surveyResult.setVisibility(View.VISIBLE);

                        yesPercent = (object.getYes()*100)/(object.getYes()+object.getNo());
                        noPercent = (object.getNo()*100)/(object.getYes()+object.getNo());
                        String result = "हाँ: "+yesPercent+"%   नहीं: "+ noPercent+"%";
                        surveyResult.setText(result);

                        // Now make sure that you update global variable to post
                        gId = object.getId();
                        gSurveyText = object.getSurveyTitle();
                        gYes = object.getYes();
                        gNo = object.getNo();

                        // Also update the result in server
                        new postSurveyUpdate().execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        }

        private class postSurveyUpdate extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... urls) {
                sendSurveyUpdate(gId, gSurveyText, gYes, gNo);

                return "";
            }

            // onPostExecute displays the results of the AsyncTask.
            @Override
            protected void onPostExecute(String result) {
                // nothing
            }
        }

        private void sendSurveyUpdate(String id, String survey_test, int yes, int no){
            try {
                try {
                    // Create the json to be posted to server
                    JSONObject jsonToPost = buidJsonObject(id, survey_test, yes, no);
                    Function.executePost(Endpoints.SERVER_URL + "updatesurvey", jsonToPost);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            catch (IOException ex){
                ex.printStackTrace();
            }

        }

        private JSONObject buidJsonObject(String id, String survey_text, int yes, int no) throws JSONException {

            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("id", id);
            jsonObject.accumulate("survey_text",  survey_text);
            jsonObject.accumulate("yes", yes);
            jsonObject.accumulate("no", no);

            return jsonObject;
        }

        @Override
        public void onClick(View v) {
            //imagePopup.viewPopup();
        }
    }
}

