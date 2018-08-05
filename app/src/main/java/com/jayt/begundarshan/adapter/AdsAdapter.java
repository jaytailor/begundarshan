package com.jayt.begundarshan.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.CardView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ceylonlabs.imageviewpopup.ImagePopup;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import com.jayt.begundarshan.DetailsActivity;
import com.jayt.begundarshan.R;
import com.jayt.begundarshan.model.AdsList;
import com.jayt.begundarshan.model.NewsItems;

public class AdsAdapter extends BaseAdapter {

    Context c;
    ArrayList<AdsList> adslist;
    LayoutInflater layoutInflater;

    public AdsAdapter(Context c, ArrayList<AdsList> adslist) {
        this.c = c;
        this.adslist = adslist;
    }

    @Override
    public int getCount() {
        return adslist.size();
    }

    @Override
    public Object getItem(int position) {
        return adslist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(layoutInflater == null)
        {
            layoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if(convertView==null)
        {
            convertView=layoutInflater.inflate(R.layout.ads_items, parent,false);
        }

        final ImageView adsImg = (ImageView) convertView.findViewById(R.id.adsImage);

        // Create a pop up of image if clicked
        final ImagePopup imagePopup = new ImagePopup(c);
        imagePopup.setBackgroundColor(Color.DKGRAY);
        imagePopup.setFullScreen(true);
        imagePopup.setHideCloseIcon(false);
        imagePopup.setImageOnClickClose(true);

        // Get the values from the adslist model
        final String image = adslist.get(position).getImageurl();

        imagePopup.initiatePopupWithPicasso(image);

        if(image.length() < 5)
        {
            adsImg.setVisibility(View.GONE);
        }else{
            Picasso.with(c)
                    .load(image)
                    .resize(300, 250)
                    .into(adsImg);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(c,"Ad",Toast.LENGTH_SHORT).show();
                imagePopup.viewPopup();

            }
        });

        return convertView;
    }
}

