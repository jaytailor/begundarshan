package com.jayt.begundarshan.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ceylonlabs.imageviewpopup.ImagePopup;
import com.jayt.begundarshan.DetailsActivity;
import com.jayt.begundarshan.R;
import com.jayt.begundarshan.holder.BaseViewHolder;
import com.jayt.begundarshan.interfaces.BaseModel;
import com.jayt.begundarshan.model.AdsList;
import com.jayt.begundarshan.model.Constants;
import com.jayt.begundarshan.model.EditorialModel;
import com.jayt.begundarshan.model.NewsItems;
import com.squareup.picasso.Picasso;

import java.util.List;

public class EditorialAdapter  extends RecyclerView.Adapter<BaseViewHolder> {

    private Context ctx;
    // private List<EditorialModel> articles;
    private List<? extends BaseModel> mList;
    private LayoutInflater mInflator;

    private TextView articleTitle, articleContent, articleWriter, publishedDate;
    private ImageView articleImage, mainAd;
    private ImagePopup imagePopup;

    public EditorialAdapter(Context ctx, List<? extends BaseModel> list) {
        this.ctx = ctx;
        this.mList = list;
        this.mInflator = LayoutInflater.from(ctx);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case Constants.ViewType.EDITORIAL_TYPE:
                return new ArticleViewHolder(mInflator.inflate(R.layout.editorial_items, parent, false));
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

        holder.setIsRecyclable(false);
    }

    public class ArticleViewHolder extends BaseViewHolder<EditorialModel> implements View.OnClickListener{
        EditorialModel articleObj;
         ArticleViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            articleTitle = (TextView) itemView.findViewById(R.id.editorialTitle);
            articleContent = (TextView) itemView.findViewById(R.id.editorialContent1);
            articleWriter = (TextView) itemView.findViewById(R.id.editorialWriter);
            publishedDate = (TextView) itemView.findViewById(R.id.editorialPublishedDate);
            articleImage = (ImageView) itemView.findViewById(R.id.editorialImage);
        }

        @Override
        public void bind(EditorialModel article) {

             // assign object to global
            articleObj = article;
            articleTitle.setText(article.getEditorial_title());
            articleContent.setText(article.getEditorial_content());
            articleWriter.setText(article.getEditorial_writer());
            publishedDate.setText(article.getEditorial_published_at());

            // If no ad returned then set ad image view to false
            if(article.getEditorial_image().equals("") || article.getEditorial_image() == null){
                articleImage.setVisibility(View.GONE);
            }else{
                Picasso.with(ctx)
                        .load(article.getEditorial_image())
                        .resize(300, 250)
                        .into(articleImage);
            }
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(ctx, DetailsActivity.class);
            intent.putExtra("published_at", articleObj.getEditorial_published_at());
            intent.putExtra("title", articleObj.getEditorial_title());
            intent.putExtra("content", articleObj.getEditorial_content());
            intent.putExtra("writer", articleObj.getEditorial_writer());
            intent.putExtra("image", articleObj.getEditorial_image());
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
