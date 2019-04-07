package com.jayt.begundarshan.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ceylonlabs.imageviewpopup.ImagePopup;
import com.jayt.begundarshan.DetailsActivity;
import com.jayt.begundarshan.holder.BaseViewHolder;
import com.jayt.begundarshan.interfaces.BaseModel;
import com.jayt.begundarshan.model.AdsList;
import com.jayt.begundarshan.model.Constants;
import com.jayt.begundarshan.model.EditorialModel;
import com.jayt.begundarshan.model.RssFeedModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import com.jayt.begundarshan.R;

public class RssViewAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private Context ctx;
    private ArrayList<RssFeedModel> rsslist;
    private List<? extends BaseModel> mList;
    private String image;
    private LayoutInflater mInflator;
    private TextView rssTitle, rssContent, rssWriter, publishedDate;
    private ImagePopup imagePopup;

    // Get the image view
    private TextView title;

    public RssViewAdapter(Context c, List<? extends BaseModel> list) {
        this.ctx = c;
        this.mList = list;
        this.mInflator = LayoutInflater.from(ctx);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case Constants.ViewType.RSS_TYPE:
                return new RSSViewHolder(mInflator.inflate(R.layout.rss_item, parent, false));
//            case Constants.ViewType.AD_TYPE:
//                return new EditorialAdapter.AdsViewHolder(mInflator.inflate(R.layout.news_page_ad, parent, false));
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

    public class RSSViewHolder extends BaseViewHolder<RssFeedModel> implements View.OnClickListener{
        RssFeedModel rssObject;
        RSSViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            rssTitle = (TextView) itemView.findViewById(R.id.rssTitle);
            rssContent = (TextView) itemView.findViewById(R.id.rssContent);
            publishedDate = (TextView) itemView.findViewById(R.id.rssPublishedDate);
//            articleImage = (ImageView) itemView.findViewById(R.id.editorialImage);

        }

        @Override
        public void bind(RssFeedModel rssfeed) {

            // assign object to global
            rssObject = rssfeed;
            rssTitle.setText(rssfeed.getRss_title());
            rssContent.setText(rssfeed.getRss_content());
            publishedDate.setText(rssfeed.getRss_published_at());
//
//            // If there are no images associated with news then show a default image from drawables
//            if( article.getEditorial_image() == null || article.getEditorial_image().size() == 0){
//                articleImage.setImageResource(R.drawable.begundarshanlogo);
//
//            }else {
//                if (article.getEditorial_image().get(0).length() < 5) {
//                    articleImage.setVisibility(View.GONE);
//                } else {
//                    Picasso.with(ctx)
//                            .load(article.getEditorial_image().get(0)).fit()
//                            .into(articleImage);
//                }
//            }
        }

        @Override
        public void onClick(View v) {

        }
    }
}
