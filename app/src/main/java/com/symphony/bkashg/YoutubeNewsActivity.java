package com.symphony.bkashg;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.squareup.picasso.Picasso;
import com.symphony.bkashg.config.Yconfig;
import com.symphony.bkashg.util.ExoPlayerManager;

import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;

public class YoutubeNewsActivity extends AppCompatActivity {

    private Button ytbtntextView238, btnSeeDetail;
    private YouTubePlayerView  ytcoverPhotoImageView;
    private final String TAG = YoutubeNewsActivity.class.getSimpleName();

//    private YouTubePlayer.OnInitializedListener ytOnInitializedListener;

    public static final String sytbtntextView238 = "com.symphony.bkashg.sytbtntextView238";
    public static final String sytcoverPhotoImageView = "com.symphony.bkashg.sytcoverPhotoImageView";

    public static final String youtubeString = "com.symphony.bkashg.youtubeString";
    public static final String details_url = "com.symphony.bkashg.details_url";
    public static final String video_url = "com.symphony.bkashg.video_url";
    public static final String intent_type = "com.symphony.bkashg.intent_type";
    public static final String adss = "com.symphony.bkashg.adss";
    public static final String details_string = "com.symphony.bkashg.details_string";
    public static final String youtube_play_string = "com.symphony.bkashg.youtube_play_string";

    public static final String stextView239_title = "com.symphony.bkashg.stextView239_title";
    public static final String stextView242_first = "com.symphony.bkashg.stextView242_first";
    public static final String stextView242_second = "com.symphony.bkashg.stextView242_second";
    public static final String stextView242_third = "com.symphony.bkashg.stextView242_third";

    public static final String scontentImage1 = "com.symphony.bkashg.scontentImage1";
    public static final String scontentImage2 = "com.symphony.bkashg.scontentImage2";
    private String link, action_type, activity, youtubeurl;

    private TextView textView239_title, textView242_first, textView242_second, textView242_third ;
    private ImageView coverPhotoImageView, contentImage1, contentImage2 ;
    public Intent intent;
    //    private AdView adView;
    public AdRequest adRequest;
    public AdView youtube_news_banner_ad;

    private NativeAdLayout nativeAdLayout;
    private LinearLayout adView;
    private NativeAd nativeAd;
    private TextView nativeAdTitle;
    private MediaView nativeAdMedia;
    private Button nativeAdCallToAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_news);

        intent = getIntent();

        ytbtntextView238 = findViewById(R.id.ytbtntextView238);
        youtube_news_banner_ad = findViewById(R.id.youtube_news_banner_ad);
        btnSeeDetail = findViewById(R.id.btnSeeDetail);
         ytcoverPhotoImageView = findViewById(R.id.ytcoverPhotoImageView);

        textView239_title = findViewById(R.id.textView239_title);
        textView242_first = findViewById(R.id.textView242_first);
        textView242_second = findViewById(R.id.textView242_second);
        textView242_third = findViewById(R.id.textView242_third);

        contentImage1 = findViewById(R.id.contentImage1);
        contentImage2 = findViewById(R.id.contentImage2);

        loadDataFromIntent();

//        ytOnInitializedListener = new YouTubePlayer.OnInitializedListener() {
//            @Override
//            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
//                if(!b) {
//                    Log.d(TAG, "youtube player initialized done");
//                    youTubePlayer.loadVideo(youtubeurl);
//                    youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
//                }
//            }
//
//            @Override
//            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
//
//            }
//        };

        extractYoutubeUrl();
    }

    @Override
    protected void onResume() {
        super.onResume();




    }

//    public void playYouTubeVideo(View v){
//        Log.d(TAG,"onclick: initializing youtube player");
//        ytcoverPhotoImageView.initialize(Yconfig.getApiKey(), ytOnInitializedListener);
//        Log.d(TAG,"youtube player running");
//    }

    public void seeDetailAction(View v){
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

    public void loadDataFromIntent(){

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
            youtubeurl = intent.getStringExtra(youtubeString);
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
            youtube_news_banner_ad.setVisibility(View.GONE);
        }

        if(intent.getStringExtra(details_string)!= null && !intent.getStringExtra(details_string).trim().isEmpty()){
            btnSeeDetail.setText(intent.getStringExtra(details_string));
        }

        if(intent.getStringExtra(youtube_play_string)!= null && !intent.getStringExtra(youtube_play_string).trim().isEmpty()){
            ytbtntextView238.setText(intent.getStringExtra(youtube_play_string));
        }

    }

    private void extractYoutubeUrl() {
        @SuppressLint("StaticFieldLeak") YouTubeExtractor mExtractor = new YouTubeExtractor(this) {
            @Override
            protected void onExtractionComplete(SparseArray<YtFile> sparseArray, VideoMeta videoMeta) {
                if (sparseArray != null) {
                    playVideo(sparseArray.get(17).getUrl());
                }
            }
        };
        mExtractor.extract(youtubeurl, true, true);
    }

    private void playVideo(String downloadUrl) {
//        PlayerView mPlayerView = findViewById(R.id.mPlayerView);
        String temp = downloadUrl;
        ytcoverPhotoImageView.setPlayer(ExoPlayerManager.getSharedInstance(this).getPlayerView().getPlayer());
        ExoPlayerManager.getSharedInstance(this).playStream(downloadUrl);
    }

}
