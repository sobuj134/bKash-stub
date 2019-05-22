package com.symphony.bkashg;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.symphony.bkashg.util.RemoteConfig;



import im.delight.android.webview.AdvancedWebView;

public class NewsWebActivity extends AppCompatActivity  implements AdvancedWebView.Listener{

    public AdvancedWebView vv;
    private ProgressBarHandler mProgressBarHandler;

    String Systray;
    String targetURL , textData;

    private boolean fb;

    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private RemoteConfig remoteConfig;
    private boolean ADS = true;
    private AdRequest adRequest;


    public AdView newsweb_banner_ad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_web);
        ADS = Boolean.parseBoolean(getString(R.string.ADS));


        fb = false;
        remoteConfig = new RemoteConfig();
        mFirebaseRemoteConfig = remoteConfig.getmFirebaseRemoteConfig();
        loadADS();
        Bundle bundle = getIntent().getExtras();
        targetURL = bundle.getString("targetUrl");
        textData = bundle.getString("textData");
        Systray = bundle.getString("SYSTRAY");

        mProgressBarHandler = new ProgressBarHandler(this);

        vv = findViewById(R.id.veinview);

        vv.setListener(this, this);
        vv.loadUrl(targetURL);
        mProgressBarHandler.show();
        newsweb_banner_ad = findViewById(R.id.newsweb_banner_ad);



    }


    @SuppressLint("NewApi")
    @Override
    protected void onResume() {
        super.onResume();
        vv.onResume();



        mProgressBarHandler.hide();
        // ...
    }

    @SuppressLint("NewApi")
    @Override
    protected void onPause() {
        vv.onPause();
        // ...
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        vv.onDestroy();
        // ...
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        vv.onActivityResult(requestCode, resultCode, intent);
        // ...
    }

//    @Override
//    public void onBackPressed() {
//        if (!vv.onBackPressed()) { return; }
//        // ...
//        super.onBackPressed();
//    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {
        mProgressBarHandler.show();
    }

    @Override
    public void onPageFinished(String url) {
        mProgressBarHandler.hide();
    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) { }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) { }

    @Override
    public void onExternalPageRequest(String url) { }






    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (!vv.onBackPressed()) { return; }
        Intent i;
        if(Systray == null){
            i = new Intent(getApplicationContext(),FirstActivity.class);
        }
        else{
            i = new Intent(getApplicationContext(),FirstActivity.class);
        }
        startActivity(i);
    }

    public void promoshare(View v){

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_newsweb, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_share_new_web){
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,targetURL );
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to_news)));
        }
        return super.onOptionsItemSelected(item);
    }


    public void loadADS() {

        long cacheExpiration = 600;
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

                    adRequest = new AdRequest.Builder().build();
                    newsweb_banner_ad.loadAd(adRequest);




            }
        });
    }


}
