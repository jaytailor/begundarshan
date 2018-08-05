package com.jayt.begundarshan;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;


import java.util.Vector;

import com.jayt.begundarshan.adapter.VideoAdapter;
import com.jayt.begundarshan.adapter.ViewPagerAdapter;
import com.jayt.begundarshan.common.Function;
import com.jayt.begundarshan.mFragments.AdsScreen;
import com.jayt.begundarshan.mFragments.BuySell;
import com.jayt.begundarshan.mFragments.Editorial;
import com.jayt.begundarshan.mFragments.News;
import com.jayt.begundarshan.mFragments.Videos;
import com.jayt.begundarshan.model.YoutubeVideo;

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
        int width = size.x;
        int height = size.y;

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

        //TABLAYOUT
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

    public void onTabSelected(TabLayout.Tab tab) {
        vp.setCurrentItem(tab.getPosition());
    }

//    private TabLayout tabLayout;
//    private AppBarLayout appBarLayout;
//    private ViewPager viewPager;
//    public static AppCompatActivity act;
//    ProgressBar loader;
//    public static ListView listNews;
//

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        tabLayout = (TabLayout) findViewById(R.id.tablayoutid);
//        appBarLayout = (AppBarLayout) findViewById(R.id.appbarid);
//        viewPager = (ViewPager) findViewById(R.id.viewpager_id);
//        listNews = (ListView) findViewById(R.id.listNews);
//        loader = (ProgressBar) findViewById(R.id.loader);
//        listNews.setEmptyView(loader);
//
//        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
//
//        // Adding the fragments
//        viewPagerAdapter.AddFragment(new News(), "Read News");
//        viewPagerAdapter.AddFragment(new BuySell(), "Buy or Sell");
//        viewPagerAdapter.AddFragment(new AdsScreen(), "See an Ad");
//        viewPagerAdapter.AddFragment(new Videos(), "Watch Videos");
//
//        // Adapter setup
//        viewPager.setAdapter(viewPagerAdapter);
//        tabLayout.setupWithViewPager(viewPager, true);
//    }


}
