package com.jayt.begundarshan;

/* COPYRIGHT
   BegunDarshan
 */

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.jayt.begundarshan.adapter.ViewPagerAdapter;
import com.jayt.begundarshan.common.Function;
import com.jayt.begundarshan.mFragments.AdsScreen;
import com.jayt.begundarshan.mFragments.Information;
import com.jayt.begundarshan.mFragments.Editorial;
import com.jayt.begundarshan.mFragments.News;
import com.jayt.begundarshan.mFragments.Videos;

public class MainActivity extends AppCompatActivity {

    ViewPager vp;
    TabLayout tabLayout;
    AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Whatsapp share and question
        ImageButton whatsapp = (ImageButton) findViewById(R.id.shareImageButton);
        ImageButton question = (ImageButton) findViewById(R.id.questionMark);

        // Check if internet is available
        if (!Function.isNetworkAvailable(this)){
            Toast.makeText(MainActivity.this, "Check your internet connection", Toast.LENGTH_SHORT)
                    .show();
        }

        // add admob banner ads
        mAdView = MainActivity.this.findViewById(R.id.mainBannerAd);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PackageManager pm = getPackageManager();

                try {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    String whatsAppMessage = "बेगूं क्षेत्र की ख़बरों के लिए इंस्टाल करें बेगूं दर्शन  https://play.google.com/store/apps/details?id=com.jayt.begundarshan";
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

        question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInfoPopup();
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
        pagerAdapter.addFragment(new Information(), "जानकारी");


        //SET ADAPTER TO VP
        vp.setAdapter(pagerAdapter);
    }

    private void showInfoPopup(){
        try{
            //We need to get the instance of the LayoutInflater, use the context of this activity
            LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.info_popup,null);

            //Get the devices screen density to calculate correct pixel sizes
            float density = getBaseContext().getResources().getDisplayMetrics().density;

            // create a focusable PopupWindow with the given layout and correct size
            final PopupWindow pw = new PopupWindow(layout, (int)density*450, (int)density*450, true);
            pw.setContentView(layout);
            pw.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
            pw.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);

            //Button to close the pop-up
            ((Button) layout.findViewById(R.id.popupClose)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    pw.dismiss();
                }
            });

            ((ImageButton) layout.findViewById(R.id.topClose)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    pw.dismiss();
                }
            });

            //Set up touch closing outside of pop-up
            pw.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            pw.setTouchInterceptor(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                        pw.dismiss();
                        return true;
                    }
                    return false;
                }
            });
            pw.setOutsideTouchable(true);
            // display the pop-up in the center
            pw.showAtLocation(layout, Gravity.CENTER, 0, 0);

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

}
