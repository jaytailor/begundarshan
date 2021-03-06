package com.jayt.begundarshan.mFragments;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.jayt.begundarshan.R;
import com.squareup.picasso.Picasso;

public class Information extends Fragment {

    View view;
    ImageButton bhaskar, navjyoti, jagran, patrika;

    String imgForAdsPrice = "https://begundarshan.sgp1.digitaloceanspaces.com/ads/adsPrice.png";

    public Information() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.information, container, false);


        // Find the image buttons
        bhaskar = (ImageButton) view.findViewById(R.id.dainikBhaskar);
        navjyoti = (ImageButton) view.findViewById(R.id.dainikNavjyoti);
        jagran = (ImageButton) view.findViewById(R.id.dainikJagran);
        patrika = (ImageButton) view.findViewById(R.id.rajPatrika);

        bhaskar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse("https://www.bhaskar.com"));
                    startActivity(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        navjyoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse("http://www.dainiknavajyoti.in"));
                    startActivity(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        jagran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse("https://www.jagran.com/"));
                    startActivity(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        patrika.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse("https://www.patrika.com/rajasthan-news"));
                    startActivity(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

}
