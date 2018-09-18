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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import com.jayt.begundarshan.DetailsActivity;
import com.jayt.begundarshan.MainActivity;
import com.jayt.begundarshan.R;
import com.jayt.begundarshan.model.NewsItems;

public class CustomAdapter extends RecyclerView.Adapter {

    private Context ctx;
    private ArrayList<NewsItems> newsItems;

    private TextView newsTitle, content, published_at;
    private ImageView newsImg, mainAd1;

    int count = 0;

    public CustomAdapter(Context ctx, ArrayList<NewsItems> newsItems) {
        this.ctx = ctx;
        this.newsItems = newsItems;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_items, parent, false);

        return new NewsViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return this.newsItems.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // Check if it is a breaking news
        final String title;
        if(newsItems.get(position).getIs_breaking().equals("true")){
            title = "ब्रेकिंग न्यूज़: " + newsItems.get(position).getTitle();;
        }else{
            title = newsItems.get(position).getTitle();;
        }

        // Change the layout if ad (will write code later for ad). or stretch image if title, content and writer are empty
        if(newsItems.get(position).getWriter().equals("") && newsItems.get(position).getContent().equals("") ){
            newsImg.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
            newsImg.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        }else{
            newsImg.getLayoutParams().height = (int) ctx.getResources().getDimension(R.dimen.imageview_height);
            newsImg.getLayoutParams().width = (int) ctx.getResources().getDimension(R.dimen.imageview_width);
        }

        // Now set the values to view
        newsTitle.setText(title);
        content.setText(newsItems.get(position).getContent());
        published_at.setText(newsItems.get(position).getPublished_at());

        // If there are no images associated with news then show a default image from drawables

        if( newsItems.get(position).getImage().equals("") || newsItems.get(position).getImage() == null){
            newsImg.setImageResource(R.drawable.begundarshanlogo);

        }else{

            if(newsItems.get(position).getImage().length() < 5)
            {
                newsImg.setVisibility(View.GONE);
            }else{
                String image = newsItems.get(position).getImage();
                Picasso.with(ctx)
                        .load(image)
                        .resize(300, 200)
                        .into(newsImg);
            }
        }

        // Also load ad on main screen if priority is
//        if(SplashActivity.dataList.get(0).getPriority().equals("11") && count == 0){
//            count += 1;
//            mainAd1.setVisibility(View.VISIBLE);
//            Picasso.with(ctx)
//                    .load(SplashActivity.dataList.get(0).getImageurl())
//                    .resize(300, 200)
//                    .into(mainAd1);
//        }

    }

    public class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        NewsViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            newsTitle = (TextView) itemView.findViewById(R.id.newsTitle);
            content = (TextView) itemView.findViewById(R.id.newsContent);
            published_at = (TextView) itemView.findViewById(R.id.newsPublishedAt);
            newsImg = (ImageView) itemView.findViewById(R.id.newsImage);
            mainAd1 = (ImageView) itemView.findViewById(R.id.mainAd1);

        }


        @Override
        public void onClick(View v) {
            Intent i = new Intent(ctx, DetailsActivity.class);
            i.putExtra("image", newsItems.get(getLayoutPosition()).getImage());
            i.putExtra("published_at", newsItems.get(getLayoutPosition()).getPublished_at());
            i.putExtra("title", newsItems.get(getLayoutPosition()).getTitle());
            i.putExtra("content", newsItems.get(getLayoutPosition()).getContent());
            i.putExtra("writer", newsItems.get(getLayoutPosition()).getWriter());
            ctx.startActivity(i);
        }
    }
}

