package com.jayt.begundarshan.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.Fragment;

import com.ceylonlabs.imageviewpopup.ImagePopup;
import com.jayt.begundarshan.SplashActivity;
import com.jayt.begundarshan.holder.BaseViewHolder;
import com.jayt.begundarshan.interfaces.BaseModel;
import com.jayt.begundarshan.model.AdsList;
import com.jayt.begundarshan.model.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import com.jayt.begundarshan.DetailsActivity;
import com.jayt.begundarshan.MainActivity;
import com.jayt.begundarshan.R;
import com.jayt.begundarshan.model.NewsItems;

public class CustomAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private Context ctx;
    private List<? extends BaseModel> mList;
    private LayoutInflater mInflator;

    private TextView newsTitle, content, published_at;
    private ImageView newsImg, mainAd;
    private ImagePopup imagePopup;

    public CustomAdapter(Context ctx, List<? extends BaseModel> list) {
        this.ctx = ctx;
        this.mList = list;
        this.mInflator = LayoutInflater.from(ctx);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        System.out.println(viewType);
        switch (viewType) {
            case Constants.ViewType.NEWS_TYPE:
                return new NewsViewHolder(mInflator.inflate(R.layout.news_items, parent, false));
            case Constants.ViewType.AD_TYPE:
                return new AdsViewHolder(mInflator.inflate(R.layout.news_page_ad, parent, false));
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
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.bind(mList.get(position));
    }

    public class NewsViewHolder extends BaseViewHolder<NewsItems> implements View.OnClickListener{
        NewsItems obj;

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
            if(newsObject.getIs_breaking().equals("true")){
                title = "ब्रेकिंग न्यूज़: " + newsObject.getTitle();;
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

            if( newsObject.getImage().equals("") || newsObject.getImage() == null){
                newsImg.setImageResource(R.drawable.begundarshanlogo);

            }else{

                if(newsObject.getImage().length() < 5)
                {
                    newsImg.setVisibility(View.GONE);
                }else{
                    String image = newsObject.getImage();
                    Picasso.with(ctx)
                            .load(image)
                            .resize(300, 200)
                            .into(newsImg);
                }
            }
        }

        @Override
        public void onClick(View v) {
            Intent i = new Intent(ctx, DetailsActivity.class);
            i.putExtra("image", obj.getImage());
            i.putExtra("published_at", obj.getPublished_at());
            i.putExtra("title", obj.getTitle());
            i.putExtra("content", obj.getContent());
            i.putExtra("writer", obj.getWriter());
            ctx.startActivity(i);
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
                        .load(image)
                        .resize(300, 250)
                        .into(mainAd);
                imagePopup.initiatePopupWithPicasso(image);
            }
        }

        @Override
        public void onClick(View v) {
            imagePopup.viewPopup();
        }
    }
}

