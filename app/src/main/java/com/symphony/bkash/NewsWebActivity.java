package com.symphony.bkash;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import java.util.Arrays;
import java.util.List;

public class NewsWebActivity extends AppCompatActivity {
//    public WebView webView;
//    public ProgressBar progressBar;
//    private FirebaseRemoteConfig mFirebaseRemoteConfig;
//    private RemoteConfig remoteConfig;
//    String Systray;
//    String targetURL , textData;
//    private PackageManager pkm ;
//    private ApplicationInfo applicationInfo;
//    private boolean fb;
//    private ProgressBarHandler mProgressBarHandler;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_news_web);
//        remoteConfig = new RemoteConfig();
//        fb = false;
//
//        Bundle bundle = getIntent().getExtras();
//        targetURL = bundle.getString("targetUrl");
//        textData = bundle.getString("textData");
//        Systray = bundle.getString("SYSTRAY");
//
//        mProgressBarHandler = new ProgressBarHandler(this);
//
//        webView = (WebView)findViewById(R.id.webView);
//        //  progressBar = (ProgressBar)findViewById(R.id.webprogressbar) ;
//
//
//// Enable responsive layout
//        webView.getSettings().setUseWideViewPort(true);
//// Zoom out if the content width is greater than the width of the veiwport
//        webView.getSettings().setLoadWithOverviewMode(true);
//        webView.getSettings().setLoadsImagesAutomatically(true);
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
//
//
//        mFirebaseRemoteConfig = remoteConfig.getmFirebaseRemoteConfig();
//        fetchRemoteConfig();
//
//        webView.setWebViewClient(new MyBrowser());
//        webView.loadUrl(targetURL);
//
//    }
//
//
//
//    // Manages the behavior when URLs are loaded
//    private class MyBrowser extends WebViewClient {
//        @SuppressWarnings("deprecation")
//        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            view.loadUrl(url);
//            return true;
//        }
//
//        @TargetApi(Build.VERSION_CODES.N)
//        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//            view.loadUrl(request.getUrl().toString());
//            return true;
//        }
//
//        @Override
//        public void onPageStarted(WebView view, String url, Bitmap favicon) {
//            super.onPageStarted(view, url, favicon);
//            //progressBar.setVisibility(View.VISIBLE);
//            mProgressBarHandler.show();
//        }
//
//        @Override
//        public void onPageFinished(WebView view, String url) {
//            super.onPageFinished(view, url);
//            //progressBar.setVisibility(View.GONE);
//            mProgressBarHandler.hide();
//        }
//    }
//
//    private void fetchRemoteConfig() {
//        long cacheExpiration = 3600;
//        if(mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()){
//            cacheExpiration = 0;
//        }
//        mFirebaseRemoteConfig.fetch(cacheExpiration).addOnCompleteListener(this, new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if(task.isSuccessful()){
//
//                    mFirebaseRemoteConfig.activateFetched();
//
//                }
//                else{
//
//                }
//                loadAdvertige();
//            }
//        });
//    }
//
//    private void loadAdvertige() {
//        boolean modelExists = false;
//        boolean isAdmobOn = mFirebaseRemoteConfig.getBoolean("is_admob_on");
//        String restrictedDevices = mFirebaseRemoteConfig.getString("disable_admob_for");
//        List<String> restricted_device_list = Arrays.asList(restrictedDevices.split("\\s*,\\s*"));
//        if(isAdmobOn){
//            modelExists = restricted_device_list.contains(remoteConfig.getModelName());
//            if(modelExists){
//                return;
//            }
//            else{
//                AdRequest adRequest = new AdRequest.Builder().build();
//                mAdView.loadAd(adRequest);
//            }
//        }
//
//        else{
//            return;
//        }
//
//    }
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Intent i;
//        if(Systray == null){
//            i = new Intent(getApplicationContext(),MainActivity.class);
//        }
//        else{
//            i = new Intent(getApplicationContext(),MainActivity.class);
//        }
//        startActivity(i);
//    }
//
//    public void promoshare(View v){
//
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_newsweb, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if(id == R.id.action_share_new_web){
//            Intent sendIntent = new Intent();
//            sendIntent.setAction(Intent.ACTION_SEND);
//            sendIntent.putExtra(Intent.EXTRA_TEXT,targetURL );
//            sendIntent.setType("text/plain");
//            startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to_news)));
//        }
//        return super.onOptionsItemSelected(item);
//    }
}
