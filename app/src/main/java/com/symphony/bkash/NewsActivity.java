package com.symphony.bkash;

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


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



//import com.facebook.FacebookSdk;


public class NewsActivity extends AppCompatActivity {
    public TextView title,body, promo_view;
    public String sTitle = "";
    public String sBody = "";

    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    public String Systray;
    private String imgURI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ImageView image_banner = (ImageView)findViewById(R.id.image_banner);
        setSupportActionBar(toolbar);

        title = (TextView)findViewById(R.id.txttitle) ;
        body = (TextView)findViewById(R.id.txtbody) ;
        promo_view = (TextView)findViewById(R.id.txtconfig);

        Bundle bundle = getIntent().getExtras();
        sTitle = bundle.getString("title");
         sBody = bundle.getString("body");
        imgURI = bundle.getString("IMAGEURL");
        Systray = bundle.getString("SYSTRAY");

        title.setText(sTitle);
        body.setText(sBody);
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


        if(bundle.getString("IMAGEURL") != null){
            Picasso.with(getApplicationContext()).load(bundle.getString("IMAGEURL")).into(image_banner);
        }

        body.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(body.getSelectionStart() ==-1 && body.getSelectionEnd() == -1){
                    Intent intent = new Intent(getApplicationContext(), NewsWebActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("targetUrl", extractUrls(sBody));
                    intent.putExtra("SYSTRAY","systray");
                    startActivity(intent);
                }
            }
        });


    }





    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i;
//        if(Systray == null){
//            i = new Intent(getApplicationContext(),NewsListActivity.class);
//        }
//        else{
            i = new Intent(getApplicationContext(),FirstActivity.class);
        //}
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
}
