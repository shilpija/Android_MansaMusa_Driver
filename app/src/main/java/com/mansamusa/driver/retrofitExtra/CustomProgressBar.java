package com.mansamusa.driver.retrofitExtra;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.airbnb.lottie.LottieAnimationView;
import com.mansamusa.driver.R;

import androidx.annotation.NonNull;


/**
 * Created by Mahipal Singh  mahisingh1@Outlook.com on 6/12/18.
 */
public class CustomProgressBar extends Dialog {


    public CustomProgressBar(@NonNull Context context, int theme) {
        super(context);
    }

    public static CustomProgressBar show(Context context, boolean indeterminate, boolean cancelable) {

        final CustomProgressBar dialog = new CustomProgressBar(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable (android.graphics.Color.TRANSPARENT));


//        CustomProgressBar dialog = new CustomProgressBar(context,R.style.Theme_Black_NoTitleBar_Fullscreen);
//        dialog.setTitle("");


        dialog.setContentView(R.layout.layout_progress);
        LottieAnimationView lottieAnimationView= (LottieAnimationView)dialog.findViewById(R.id.lottieAnimationView);

        lottieAnimationView.setAnimation("6670-loading.json");
        lottieAnimationView.playAnimation();
        lottieAnimationView.loop(true);

//        ProgressView progressView= (ProgressView)dialog.findViewById(R.id.progress_pv_circular_inout_colors);
//        progressView.start();

        dialog.setCancelable(cancelable);
        //dialog.setOnCancelListener(cancelListener);
        dialog.getWindow().getAttributes().gravity= Gravity.CENTER;
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount=0.2f;
        dialog.getWindow().setAttributes(lp);
        //dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        dialog.show();
        return dialog;
    }
}
