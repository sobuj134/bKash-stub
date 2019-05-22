package com.symphony.bkashg;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.squareup.picasso.Picasso;
import com.symphony.bkashg.util.RemoteConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


//import com.facebook.FacebookSdk;


public class NewsActivity extends AppCompatActivity {
    public TextView title,body, promo_view;
    public String sTitle = "";
    public String sBody = "";
    public Bundle bundle;
    public ImageView image_banner;

    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private RemoteConfig remoteConfig;
    private boolean ADS = true;
    public AdRequest adRequest;
    public String Systray;
    private String imgURI;
    public AdView news_banner_ad;
    private InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        image_banner = findViewById(R.id.image_banner);
//        setSupportActionBar(toolbar);
        remoteConfig = new RemoteConfig();
        mFirebaseRemoteConfig = remoteConfig.getmFirebaseRemoteConfig();
        loadADS();




        title = (TextView)findViewById(R.id.txttitle) ;
        body = (TextView)findViewById(R.id.txtbody) ;
        promo_view = (TextView)findViewById(R.id.txtconfig);

        bundle = getIntent().getExtras();
        sTitle = bundle.getString("title");
        sBody = bundle.getString("body");
        imgURI = bundle.getString("IMAGEURL");
        Systray = bundle.getString("SYSTRAY");



        news_banner_ad = findViewById(R.id.news_banner_ad);









      /*  FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/


        // mAdView.setAdSize(AdSize.BANNER);
        //mAdView.setAdUnitId("ca-app-pub-4365083222822400/8672759776");




//        body.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(body.getSelectionStart() ==-1 && body.getSelectionEnd() == -1){
//                    Intent intent = new Intent(getContext(), NewsWebActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intent.putExtra("targetUrl", extractUrls(sBody));
//                    intent.putExtra("SYSTRAY","systray");
//                    startActivity(intent);
//                }
//            }
//        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        title.setText(sTitle);
        body.setText(sBody);
        if(bundle.getString("IMAGEURL") != null){
            Picasso.with(getApplicationContext()).load(bundle.getString("IMAGEURL")).into(image_banner);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i;
        Log.d("BACK", "Back Button pressed");
//        if(Systray == null){
//            i = new Intent(getApplicationContext(),NewsListActivity.class);
//        }
//        else{
        i = new Intent(getApplicationContext(),FirstActivity.class);

            startActivity(i);



    }

    public void simpleshare(View v){
        List<Intent> shareIntentsLists = new ArrayList<Intent>();
        Intent sendIntent = new Intent();

        sendIntent.setAction(Intent.ACTION_SEND);

        sendIntent.setType("*/*");
        List<ResolveInfo> resolveInfos = getPackageManager().queryIntentActivities(sendIntent,0);
        if(!resolveInfos.isEmpty()){
            for(ResolveInfo resInfo : resolveInfos){
                String packageName = resInfo.activityInfo.packageName;
                if(!packageName.contains("com.facebook.katana")){
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName(packageName, resInfo.activityInfo.name));
                    intent.setAction(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_SUBJECT, sTitle);
                    intent.putExtra(Intent.EXTRA_TEXT, sBody + "\n" + getString(R.string.sent_from) + "   "+ getString(R.string.invitation_deep_link));
                    intent.setType("image/*");
                    intent.setPackage(packageName);
                    shareIntentsLists.add(intent);
                }
            }
            if(!shareIntentsLists.isEmpty()){

                Intent chooserIntent = Intent.createChooser(shareIntentsLists.remove(0), "Choose app to share");

                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, shareIntentsLists.toArray(new Parcelable[]{}));

                startActivity(chooserIntent);

            }
            else{
                Log.e("Error", "No Apps can perform your task");
            }
        }

    }

    public static String extractUrls(String text)
    {
        String containedUrls = "";
        String urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
        Matcher urlMatcher = pattern.matcher(text);

        while (urlMatcher.find())
        {
            containedUrls = text.substring(urlMatcher.start(0),
                    urlMatcher.end(0));
        }

        return containedUrls;
    }

    public void loadADS() {

        long cacheExpiration = 3600;
        if(mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()){
            cacheExpiration = 0;
        }
        mFirebaseRemoteConfig.fetch(cacheExpiration).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    mFirebaseRemoteConfig.activateFetched();
                    Log.d("ADSREMOTECONFIG", "Successfull");
                    ADS = mFirebaseRemoteConfig.getBoolean("ADS");


                } else {
                    Log.d("ADSREMOTECONFIG", "Failed");
                }

                if(ADS){
                    adRequest = new AdRequest.Builder().build();
                    news_banner_ad.loadAd(adRequest);
                }

            }
        });
    }
}
