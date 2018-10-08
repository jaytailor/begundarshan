package com.jayt.begundarshan;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {
    Context ctx;
    String title = "", content = "", writer="", published_at="";
    ArrayList<String> image;

    public DetailsActivity(Context ctx, Object obj) {
        this.ctx = ctx;
    }

    public DetailsActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        TextView titleView, contentView, writerView, publishedDateView;
        ImageView newsImageView;

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

        newsImageView = (ImageView) findViewById(R.id.image);
        writerView = (TextView) findViewById(R.id.detailWriter);
        publishedDateView = (TextView) findViewById(R.id.detailPublishedAt);

        // Whatsapp share
        ImageButton whatsapp = (ImageButton) findViewById(R.id.shareOnWhatsapp);

        // Change the layout if ad (will write code later for ad). or stretch image if title, content and writer are emptys
        if(writer.equals("") && title.equals("") && content.equals("") ){
            newsImageView.getLayoutParams().height = (int) getResources().getDimension(R.dimen.detailimage_height);
            newsImageView.getLayoutParams().width = (int) getResources().getDimension(R.dimen.detailimage_width);
        }

        try{
            titleView.setText(title);
            contentView.setText(content);
            writerView.setText(writer);
            publishedDateView.setText(published_at);

            if( image == null || image.size() == 0){
                // just load from static image
                newsImageView.getLayoutParams().height = (int) getResources().getDimension(R.dimen.detailimage_height);;
                newsImageView.getLayoutParams().width = (int) getResources().getDimension(R.dimen.detailimage_width);
                newsImageView.setImageResource(R.drawable.begundarshanlogo);

            }else{

                // For now just show first image,
                // later we will load all images in horizontal swipe
                if(image.get(0).length() < 5)
                {
                    newsImageView.setVisibility(View.GONE);
                }else{
                    Picasso.with(this)
                            .load(image.get(0))
                            .resize(300, 250)
                            .into(newsImageView);
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
                            "https://play.google.com/store/apps/details?id==com.jayt.begundarshan";
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
}

