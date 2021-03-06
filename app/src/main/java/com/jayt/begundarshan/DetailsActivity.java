package com.jayt.begundarshan;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity {

    String title = "", content = "", writer="", published_at="";
    ArrayList<String> image;
    AdView mAdView;

    private LinearLayout mGallery;
    private LayoutInflater mInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        mInflater = LayoutInflater.from(this);

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        TextView titleView, contentView, writerView, publishedDateView;
        ImageView newsImageView;

        // add native advertisement
        AdLoader adLoader = new AdLoader.Builder(this, getString(R.string.native_advanced_adunit_id))
                .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        //the native ad will be available inside this method  (unifiedNativeAd)

                        UnifiedNativeAdView unifiedNativeAdView = (UnifiedNativeAdView) getLayoutInflater().inflate(R.layout.native_ad_layout, null);
                        mapUnifiedNativeAdToLayout(unifiedNativeAd, unifiedNativeAdView);

                        FrameLayout nativeAdLayout = findViewById(R.id.id_native_ad);
                        nativeAdLayout.removeAllViews();
                        nativeAdLayout.addView(unifiedNativeAdView);
                    }
                })
                .build();
        adLoader.loadAd(new AdRequest.Builder().build());
        // *************

        Intent intent = getIntent();

        image = intent.getStringArrayListExtra("image");
        title = intent.getStringExtra("title");
        content = intent.getStringExtra("content");
        writer = intent.getStringExtra("writer");
        published_at = intent.getStringExtra("published_at");

        // Get all views
        titleView = (TextView) findViewById(R.id.title);
        contentView = (TextView) findViewById(R.id.content);
        contentView.setMovementMethod(new ScrollingMovementMethod());

        mGallery = (LinearLayout) findViewById(R.id.id_gallery);

        writerView = (TextView) findViewById(R.id.detailWriter);
        publishedDateView = (TextView) findViewById(R.id.detailPublishedAt);

        // Whatsapp share
        ImageButton whatsapp = (ImageButton) findViewById(R.id.shareOnWhatsapp);

        try{
            titleView.setText(title);
            contentView.setText(content);
            writerView.setText(writer);
            publishedDateView.setText(published_at);


            if( image == null || image.size() == 0){
                View view = mInflater.inflate(R.layout.activity_gallery_item, mGallery, false);
                newsImageView = (ImageView) view.findViewById(R.id.id_index_gallery_item_image);

                // just load from static image
                newsImageView.getLayoutParams().height = (int) getResources().getDimension(R.dimen.detailimage_height);;
                newsImageView.getLayoutParams().width = (int) getResources().getDimension(R.dimen.detailimage_width);
                newsImageView.setImageResource(R.drawable.begundarshanlogo);

                // add in gallery view
                mGallery.addView(view);
            }
            else{
                for (int i = 0; i < image.size(); i++)
                {
                    View view = mInflater.inflate(R.layout.activity_gallery_item, mGallery, false);
                    newsImageView = (ImageView) view.findViewById(R.id.id_index_gallery_item_image);

                    // For now just show first image,
                    // later we will load all images in horizontal swipe
                    if(image.get(0).length() < 5)
                    {
                        // just load from static image
                        newsImageView.getLayoutParams().height = (int) getResources().getDimension(R.dimen.detailimage_height);;
                        newsImageView.getLayoutParams().width = (int) getResources().getDimension(R.dimen.detailimage_width);
                        newsImageView.setImageResource(R.drawable.begundarshanlogo);
                    }else{
                        Picasso.with(this)
                                .load(image.get(i)).fit()
                                .into(newsImageView);
                    }

                    // add in gallery view
                    mGallery.addView(view);
                }
            }


        }catch(Exception e) {
            e.printStackTrace();
        }

        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PackageManager pm=getPackageManager();

                try {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    String whatsAppMessage = title + " : To read full news download Begun news App from " +
                            "https://play.google.com/store/apps/details?id=com.jayt.begundarshan";
                    sendIntent.setType("text/plain");
                    sendIntent.putExtra(Intent.EXTRA_TEXT, whatsAppMessage);

                    // Do not forget to add this to open whatsApp App specifically
                    PackageInfo info=pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
                    sendIntent.setPackage("com.whatsapp");
                    startActivity(Intent.createChooser(sendIntent, "Share with"));

                } catch (PackageManager.NameNotFoundException e) {
                    Toast.makeText(DetailsActivity.this, "WhatsApp not Installed", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });

    }

    public void mapUnifiedNativeAdToLayout(UnifiedNativeAd adFromGoogle, UnifiedNativeAdView myAdView) {
        MediaView mediaView = myAdView.findViewById(R.id.ad_media);
        myAdView.setMediaView(mediaView);

        myAdView.setHeadlineView(myAdView.findViewById(R.id.ad_headline));
        myAdView.setBodyView(myAdView.findViewById(R.id.ad_body));
        myAdView.setCallToActionView(myAdView.findViewById(R.id.ad_call_to_action));
        myAdView.setIconView(myAdView.findViewById(R.id.ad_icon));
        myAdView.setPriceView(myAdView.findViewById(R.id.ad_price));
        myAdView.setStarRatingView(myAdView.findViewById(R.id.ad_rating));
        myAdView.setStoreView(myAdView.findViewById(R.id.ad_store));
        myAdView.setAdvertiserView(myAdView.findViewById(R.id.ad_advertiser));

        ((TextView) myAdView.getHeadlineView()).setText(adFromGoogle.getHeadline());

        if (adFromGoogle.getBody() == null) {
            myAdView.getBodyView().setVisibility(View.GONE);
        } else {
            ((TextView) myAdView.getBodyView()).setText(adFromGoogle.getBody());
        }

        if (adFromGoogle.getCallToAction() == null) {
            myAdView.getCallToActionView().setVisibility(View.GONE);
        } else {
            ((Button) myAdView.getCallToActionView()).setText(adFromGoogle.getCallToAction());
        }

        if (adFromGoogle.getIcon() == null) {
            myAdView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) myAdView.getIconView()).setImageDrawable(adFromGoogle.getIcon().getDrawable());
        }

        if (adFromGoogle.getPrice() == null) {
            myAdView.getPriceView().setVisibility(View.GONE);
        } else {
            ((TextView) myAdView.getPriceView()).setText(adFromGoogle.getPrice());
        }

        if (adFromGoogle.getStarRating() == null) {
            myAdView.getStarRatingView().setVisibility(View.GONE);
        } else {
            ((RatingBar) myAdView.getStarRatingView()).setRating(adFromGoogle.getStarRating().floatValue());
        }

        if (adFromGoogle.getStore() == null) {
            myAdView.getStoreView().setVisibility(View.GONE);
        } else {
            ((TextView) myAdView.getStoreView()).setText(adFromGoogle.getStore());
        }

        if (adFromGoogle.getAdvertiser() == null) {
            myAdView.getAdvertiserView().setVisibility(View.GONE);
        } else {
            ((TextView) myAdView.getAdvertiserView()).setText(adFromGoogle.getAdvertiser());
        }

        myAdView.setNativeAd(adFromGoogle);
    }


    @Override
    public void onBackPressed() {
        if (SplashActivity.interstitialAd.isLoaded()) {
            SplashActivity.interstitialAd.show();
            SplashActivity.interstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    // Load the next interstitial.
                    SplashActivity.interstitialAd.loadAd(new AdRequest.Builder().build());
                    super.onAdClosed();
                    finish();
                }
            });
        }else{
            super.onBackPressed();
        }
    }
}

