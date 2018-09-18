package com.jayt.begundarshan;

/* COPYRIGHT
   BegunDarshan
 */

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Toast;

import com.jayt.begundarshan.adapter.ViewPagerAdapter;
import com.jayt.begundarshan.mFragments.AdsScreen;
import com.jayt.begundarshan.mFragments.Editorial;
import com.jayt.begundarshan.mFragments.News;
import com.jayt.begundarshan.mFragments.Videos;
import com.jayt.begundarshan.model.AdsList;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ViewPager vp;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ScrollView mScrollView = (ScrollView) findViewById(R.id.scrollView);

        // get screen size
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        // Whatsapp share
        ImageButton whatsapp = (ImageButton) findViewById(R.id.shareImageButton);


        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PackageManager pm=getPackageManager();

                try {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    String whatsAppMessage = "Download Begun news App from https://play.google.com/store/apps/details?id==com.jayt.begundarshan";
                    sendIntent.setType("text/plain");
                    sendIntent.putExtra(Intent.EXTRA_TEXT, whatsAppMessage);

                    // Do not forget to add this to open whatsApp App specifically
                    PackageInfo info=pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
                    sendIntent.setPackage("com.whatsapp");
                    startActivity(Intent.createChooser(sendIntent, "Share with"));
                } catch (PackageManager.NameNotFoundException e) {
                    Toast.makeText(MainActivity.this, "WhatsApp not Installed", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });

        //TAB LAYOUT
        tabLayout= (TabLayout) findViewById(R.id.tablayoutid);


        //VIEWPAGER
        vp= (ViewPager) findViewById(R.id.viewpager_id);
        this.addPages();

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(vp);
    }

    private void addPages()
    {
        ViewPagerAdapter pagerAdapter=new ViewPagerAdapter(this.getSupportFragmentManager());
        pagerAdapter.addFragment(new News(), "समाचार");
        pagerAdapter.addFragment(new AdsScreen(), "विज्ञापन");
        pagerAdapter.addFragment(new Videos(), "वीडियो");
        pagerAdapter.addFragment(new Editorial(), "संपादकीय");
//        pagerAdapter.addFragment(new BuySell(), "ख़रीदे बेचे");
//        pagerAdapter.addFragment(new AdsScreen(), "राशिफ़ल");

        //SET ADAPTER TO VP
        vp.setAdapter(pagerAdapter);
    }

}
