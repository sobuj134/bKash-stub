package com.symphony.bkash.receiver;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.symphony.bkash.listener.AppUpdateListener;
import com.symphony.bkash.util.LoadingDialog;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by monir.sobuj on 12/23/2018.
 */

public class VersionChecker extends AsyncTask<String, String, Boolean> {
    private String newVersion;
    private String packagename;
    private Context mContext;
    private String installVersion = "";
    LoadingDialog loadingDialog;
    AppUpdateListener appUpdateListener;

    public VersionChecker(Context mContext, String packagename, AppUpdateListener appUpdateListener){
        this.mContext = mContext;
        this.packagename = packagename;
        this.appUpdateListener = appUpdateListener;
    }

    @Override
    protected Boolean doInBackground(String... params) {


        try {
            Document document = Jsoup.connect("https://play.google.com/store/apps/details?id=com.bKash.customerapp")
                    .timeout(30000)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .get();
            if (document != null) {
                Elements element = document.getElementsContainingOwnText("Current Version");
                for (Element ele : element) {
                    if (ele.siblingElements() != null) {
                        Elements sibElemets = ele.siblingElements();
                        for (Element sibElemet : sibElemets) {
                            newVersion = sibElemet.text();
                            Log.e("Version Checker", "doInBackground: version"+newVersion);
                        }
                    }
                }
                try{
                    PackageInfo packageInfo = mContext.getPackageManager().getPackageInfo(packagename, PackageManager.GET_PERMISSIONS);
                    installVersion = packageInfo.versionName;
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return installVersion.equals(newVersion);
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        loadingDialog.dismiss();
        if(!aBoolean){
            appUpdateListener.onUpdate();
        }

    }

    @Override
    protected void onPreExecute() {
        loadingDialog = LoadingDialog.newInstance(mContext, "Please Wait!");
        loadingDialog.show();
    }
}