package com.symphony.bkashg;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.facebook.ads.*;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Picasso;
import com.symphony.bkashg.config.Fconfig;

import java.util.ArrayList;
import java.util.List;

public class NewNewsActivity extends AppCompatActivity {
    private TextView textView239_title, textView242_first, textView242_second, textView242_third ;
    private ImageView coverPhotoImageView, contentImage1, contentImage2 ;
    public Intent intent;
//    private AdView adView;
    public AdRequest adRequest;
    public AdView new_news_banner_ad;
    private final String TAG = NewNewsActivity.class.getSimpleName();
    private NativeAdLayout nativeAdLayout;
    private LinearLayout adView;
    private NativeAd nativeAd;
    private TextView nativeAdTitle;
    private MediaView nativeAdMedia;
    private Button nativeAdCallToAction, likeImageView, btnSeeDetails;

    public static final String stextView239_title = "com.symphony.bkashg.stextView239_title";
    public static final String stextView242_first = "com.symphony.bkashg.stextView242_first";
    public static final String stextView242_second = "com.symphony.bkashg.stextView242_second";
    public static final String stextView242_third = "com.symphony.bkashg.stextView242_third";

    public static final String scoverPhotoImageView = "com.symphony.bkashg.scoverPhotoImageView";
    public static final String scontentImage1 = "com.symphony.bkashg.scontentImage1";
    public static final String scontentImage2 = "com.symphony.bkashg.scontentImage2";

    public static final String youtubeString = "com.symphony.bkashg.youtubeString";
    public static final String details_url = "com.symphony.bkashg.details_url";
    public static final String video_url = "com.symphony.bkashg.video_url";
    public static final String intent_type = "com.symphony.bkashg.intent_type";
    public static final String adss = "com.symphony.bkashg.adss";
    public static final String details_string = "com.symphony.bkashg.details_string";
    private String link, action_type, activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_news);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        loadBanner();

        new_news_banner_ad = findViewById(R.id.new_news_banner_ad);
        textView239_title = findViewById(R.id.textView239_title);
        textView242_first = findViewById(R.id.textView242_first);
        textView242_second = findViewById(R.id.textView242_second);
        textView242_third = findViewById(R.id.textView242_third);

        coverPhotoImageView = findViewById(R.id.coverPhotoImageView);
        contentImage1 = findViewById(R.id.contentImage1);
        contentImage2 = findViewById(R.id.contentImage2);

        likeImageView = findViewById(R.id.likeImageView);
        btnSeeDetails = findViewById(R.id.btnSeeDetails);

        intent = getIntent();

        adRequest = new AdRequest.Builder().build();
        new_news_banner_ad.loadAd(adRequest);

        loadNativeAd();

    }

    @Override
    protected void onResume() {
        super.onResume();


        if(intent.getStringExtra(scoverPhotoImageView)!= null && !intent.getStringExtra(scoverPhotoImageView).trim().isEmpty()){
            Picasso.with(getApplicationContext()).load(intent.getStringExtra(scoverPhotoImageView)).into(coverPhotoImageView);
        }
        if(intent.getStringExtra(scontentImage1)!= null && !intent.getStringExtra(scontentImage1).trim().isEmpty()){
            Picasso.with(getApplicationContext()).load(intent.getStringExtra(scontentImage1)).into(contentImage1);
        }
        if(intent.getStringExtra(scontentImage2)!= null && !intent.getStringExtra(scontentImage2).trim().isEmpty()){
            Picasso.with(getApplicationContext()).load(intent.getStringExtra(scontentImage2)).into(contentImage2);
        }



        if(intent.getStringExtra(stextView239_title)!= null && !intent.getStringExtra(stextView239_title).trim().isEmpty()){
            textView239_title.setVisibility(View.VISIBLE);
            textView239_title.setText(intent.getStringExtra(stextView239_title));
        }
        if(intent.getStringExtra(stextView242_first)!= null && !intent.getStringExtra(stextView242_first).trim().isEmpty()){
            textView242_first.setVisibility(View.VISIBLE);
            textView242_first.setText(intent.getStringExtra(stextView242_first));
        }
        if(intent.getStringExtra(stextView242_second)!= null && !intent.getStringExtra(stextView242_second).trim().isEmpty()){
            textView242_second.setVisibility(View.VISIBLE);
            textView242_second.setText(intent.getStringExtra(stextView242_second));
        }
        if(intent.getStringExtra(stextView242_third)!= null && !intent.getStringExtra(stextView242_third).trim().isEmpty()){
            textView242_third.setVisibility(View.VISIBLE);
            textView242_third.setText(intent.getStringExtra(stextView242_third));
        }

        if(intent.getStringExtra(youtubeString)!= null && !intent.getStringExtra(youtubeString).trim().isEmpty()){

        }

        if(intent.getStringExtra(details_url)!= null && !intent.getStringExtra(details_url).trim().isEmpty()){
            link = intent.getStringExtra(details_url);
        }

        if(intent.getStringExtra(video_url)!= null && !intent.getStringExtra(video_url).trim().isEmpty()){

        }
        if(intent.getStringExtra(intent_type).equals("browser")){
            action_type = "browser";
        }
        else{
            action_type = "activity";
            activity = intent.getStringExtra(intent_type);
        }

        if(intent.getStringExtra(adss).equals("false")){
            new_news_banner_ad.setVisibility(View.GONE);
        }

        if(intent.getStringExtra(details_string)!= null && !intent.getStringExtra(details_string).trim().isEmpty()){
            likeImageView.setText(intent.getStringExtra(details_string));
            btnSeeDetails.setText(intent.getStringExtra(details_string));
        }


    }


//    public void loadBanner(){
//        adView = new AdView(this, Fconfig.getFbBannerAdApiKey(), AdSize.BANNER_HEIGHT_50);
//
//        // Find the Ad Container
//        LinearLayout adContainer = findViewById(R.id.banner_container);
//
//        // Add the ad view to your activity layout
//        adContainer.addView(adView);
//
//        // Request an ad
//        adView.loadAd();
//    }

    private void loadNativeAd() {
        // Instantiate a NativeAd object.
        // NOTE: the placement ID will eventually identify this as your App, you can ignore it for
        // now, while you are testing and replace it later when you have signed up.
        // While you are using this temporary code you will only get test ads and if you release
        // your code like this to the Google Play your users will not receive ads (you will get a no fill error).
        nativeAd = new NativeAd(this, "310191579906622_310812919844488");

        nativeAd.setAdListener(new NativeAdListener() {
            @Override
            public void onMediaDownloaded(Ad ad) {
                // Native ad finished downloading all assets
                Log.e(TAG, "Native ad finished downloading all assets.");
                if (nativeAd == null || nativeAd != ad) {
                    return;
                }

                inflateAd(nativeAd);
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Native ad failed to load
                Log.e(TAG, "Native ad failed to load: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Race condition, load() called again before last ad was displayed
                if (nativeAd == null || nativeAd != ad) {
                    return;
                }
                // Inflate Native Ad into Container
                inflateAd(nativeAd);

                // Create a list of clickable views
                List<View> clickableViews = new ArrayList<>();
                clickableViews.add(nativeAdTitle);
                clickableViews.add(nativeAdCallToAction);

                // Register the Title and CTA button to listen for clicks.
                nativeAd.registerViewForInteraction(
                        adView,
                        nativeAdMedia,

                        clickableViews);
                nativeAd.downloadMedia();

                // Native ad is loaded and ready to be displayed
                Log.d(TAG, "Native ad is loaded and ready to be displayed!");
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Native ad clicked
                Log.d(TAG, "Native ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Native ad impression
                Log.d(TAG, "Native ad impression logged!");
            }
        });

        // Request an ad
        nativeAd.loadAd(NativeAdBase.MediaCacheFlag.NONE);
    }


    private void inflateAd(NativeAd nativeAd) {

        nativeAd.unregisterView();

        // Add the Ad view into the ad container.
        nativeAdLayout = findViewById(R.id.native_ad_container);
        LayoutInflater inflater = LayoutInflater.from(NewNewsActivity.this);
        // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
        adView = (LinearLayout) inflater.inflate(R.layout.native_ad_layout, nativeAdLayout, false);
        nativeAdLayout.addView(adView);

        // Add the AdOptionsView
        LinearLayout adChoicesContainer = findViewById(R.id.ad_choices_container);
        AdOptionsView adOptionsView = new AdOptionsView(NewNewsActivity.this, nativeAd, nativeAdLayout);
        adChoicesContainer.removeAllViews();
        adChoicesContainer.addView(adOptionsView, 0);

        // Create native UI using the ad metadata.
        AdIconView nativeAdIcon = adView.findViewById(R.id.native_ad_icon);
         nativeAdTitle = adView.findViewById(R.id.native_ad_title);
         nativeAdMedia = adView.findViewById(R.id.native_ad_media);
        TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
        TextView nativeAdBody = adView.findViewById(R.id.native_ad_body);
        TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
         nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);

        // Set the Text.
        nativeAdTitle.setText(nativeAd.getAdvertiserName());
        nativeAdBody.setText(nativeAd.getAdBodyText());
        nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
        nativeAdCallToAction.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
        sponsoredLabel.setText(nativeAd.getSponsoredTranslation());

        // Create a list of clickable views
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdTitle);
        clickableViews.add(nativeAdCallToAction);

        // Register the Title and CTA button to listen for clicks.
        nativeAd.registerViewForInteraction(
                adView,
                nativeAdMedia,
                nativeAdIcon,
                clickableViews);
    }

    public void seeDetailsAction(View v){
        Intent i;
        if(action_type.equals("browser")){
            i = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
            startActivity(i);
        }
        else{
            i = new Intent(getApplicationContext(),NewsWebActivity.class);
            i.putExtra("targetUrl", link);
            i.putExtra("SYSTRAY","systray");
            startActivity(i);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i;
        Log.d("BACK", "Back Button pressed");
        i = new Intent(getApplicationContext(),NewsWebActivity.class);
        i.putExtra("targetUrl", link);
        i.putExtra("SYSTRAY","systray");
        startActivity(i);


    }

}
