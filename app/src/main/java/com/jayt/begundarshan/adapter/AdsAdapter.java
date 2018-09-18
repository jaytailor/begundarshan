package com.jayt.begundarshan.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ceylonlabs.imageviewpopup.ImagePopup;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import com.jayt.begundarshan.R;
import com.jayt.begundarshan.model.AdsList;

public class AdsAdapter extends RecyclerView.Adapter {

    private Context ctx;
    private ArrayList<AdsList> adslist;
    private String image;
    private ImagePopup imagePopup;

    // Get the image view
    private ImageView adsImg;

    public AdsAdapter(Context c, ArrayList<AdsList> adslist) {
        this.ctx = c;
        this.adslist = adslist;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ads_items, parent, false);

        return new AdsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        image = adslist.get(position).getImageurl();

        if(image.length() < 5)
        {
            adsImg.setVisibility(View.GONE);
        }else{
            Picasso.with(ctx)
                    .load(image)
                    .resize(300, 250)
                    .into(adsImg);
        }
    }

    @Override
    public int getItemCount() {
        return this.adslist.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class AdsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        AdsViewHolder(View itemView) {
            super(itemView);

            adsImg = (ImageView) itemView.findViewById(R.id.adsImage);
            itemView.setOnClickListener(this);

            // Create a pop up of image if clicked
            imagePopup = new ImagePopup(ctx);
            imagePopup.setBackgroundColor(Color.DKGRAY);
            imagePopup.setFullScreen(true);
            imagePopup.setHideCloseIcon(false);
            imagePopup.setImageOnClickClose(true);
            imagePopup.setKeepScreenOn(true);

            imagePopup.initiatePopupWithPicasso(image);
        }


        @Override
        public void onClick(View v) {
            imagePopup.viewPopup();
        }
    }
}

