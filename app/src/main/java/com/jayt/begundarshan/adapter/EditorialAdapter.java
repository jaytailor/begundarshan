package com.jayt.begundarshan.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jayt.begundarshan.DetailsActivity;
import com.jayt.begundarshan.R;
import com.jayt.begundarshan.model.EditorialModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class EditorialAdapter  extends RecyclerView.Adapter {

    Context ctx;
    List<EditorialModel> articles;
    protected TextView articleTitle, articleContent, articleWriter, publishedDate;
    protected ImageView articleImage;

    public EditorialAdapter(Context ctx, List<EditorialModel> articles) {
        this.ctx = ctx;
        this.articles = articles;
    }

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.editorial_items, parent, false);

        return new ArticleViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return this.articles.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        articleTitle.setText(articles.get(position).getEditorial_title());
        articleContent.setText(articles.get(position).getEditorial_content());
        articleWriter.setText(articles.get(position).getEditorial_writer());
        publishedDate.setText(articles.get(position).getEditorial_published_at());

        // If no ad returned then set ad image view to false
        if(articles.get(position).getEditorial_image().equals("")){
            //articleImage.setLayoutParams(new LinearLayout.LayoutParams(0,0));
            articleImage.setVisibility(View.GONE);
        }else{
            Picasso.with(ctx)
                    .load(articles.get(position).getEditorial_image())
                    .resize(300, 250)
                    .into(articleImage);
        }


    }

    public class ArticleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

         ArticleViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            articleTitle = (TextView) itemView.findViewById(R.id.editorialTitle);
            articleContent = (TextView) itemView.findViewById(R.id.editorialContent1);
            articleWriter = (TextView) itemView.findViewById(R.id.editorialWriter);
            publishedDate = (TextView) itemView.findViewById(R.id.editorialPublishedDate);

            articleImage = (ImageView) itemView.findViewById(R.id.editorialAd1);
        }


        @Override
        public void onClick(View v) {
            Intent intent = new Intent(ctx, DetailsActivity.class);
            intent.putExtra("published_at", articles.get(getLayoutPosition()).getEditorial_published_at());
            intent.putExtra("title", articles.get(getLayoutPosition()).getEditorial_title());
            intent.putExtra("content", articles.get(getLayoutPosition()).getEditorial_content());
            intent.putExtra("writer", articles.get(getLayoutPosition()).getEditorial_writer());
            ctx.startActivity(intent);
        }
    }

}
