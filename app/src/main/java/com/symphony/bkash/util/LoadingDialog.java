package com.symphony.bkash.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.symphony.bkash.R;
import com.wang.avi.AVLoadingIndicatorView;

/**
 * Created by monir.sobuj on 7/29/18.
 */

public class LoadingDialog extends Dialog {

    private AVLoadingIndicatorView avi;
    private TextView messagetv;
    private RelativeLayout loadingbg;

    private ImageView ivTick;

    public static LoadingDialog newInstance(Context context, String message){
        LoadingDialog loadingDialog = new LoadingDialog(context);
        loadingDialog.setMessage(message);
        loadingDialog.setCancelable(false);
        return loadingDialog;
    }

    public LoadingDialog(Context context) {
        super(context);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.loading_dialog_layout);
        loadingbg = (RelativeLayout) findViewById(R.id.layoutLoading);
        avi = (AVLoadingIndicatorView) findViewById(R.id.avi);
        messagetv = (TextView) findViewById(R.id.message);
        ivTick = (ImageView) findViewById(R.id.ivTick);

        avi.setVisibility(View.VISIBLE);
        ivTick.setVisibility(View.GONE);
    }

    public LoadingDialog setMessage(String message) {
        messagetv.setText(message);
        return this;
    }

    @Override
    public void show() {
        super.show();
        avi.smoothToShow();
    }

    public LoadingDialog setLoadingBg(int Colorbg) {
        loadingbg.setBackgroundColor(Colorbg);
        return this;
    }

    public LoadingDialog setTick() {
        avi.setVisibility(View.GONE);
        ivTick.setVisibility(View.VISIBLE);
        return this;
    }


    @Override
    public void dismiss() {
        super.dismiss();
        avi.smoothToHide();
    }

}
