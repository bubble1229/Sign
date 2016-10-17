package com.highersun.sign.ui.dialog;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.highersun.sign.R;
import com.highersun.sign.ui.sign1.Sign1Presenter;
import com.highersun.sign.utils.RenderScriptBlurHelper;

/**
 * Author: ShenDanLai on 2016/9/13.
 * Email: 17721129316@163.com
 */
public abstract class BlurDialog extends Dialog {

    private Activity mOwnerActivity; // 这个Dialog依附的Activity
    private ImageView blurImage; // 显示模糊的图片
    private ImageView blurAlpha; // 显示透明度
    private FrameLayout showView; // 要往上面添加布局的父控件
    private AlphaAnimation alphaAnimation; // 透明变化
    private Animation dialogInAnim; // Dialog进入动画
    private Bitmap bitmap;
    Sign1Presenter sign1Presenter;
    String userId;
    String signEquipment;
    String ibeanconid;
//    private Button btnCheckIn, btnCheckOut;
    private RelativeLayout rlCheckIn, rlCheckOut, rlCheckInButton, rlCheckOutButton;

    public BlurDialog(Activity activity,Sign1Presenter sign1Presenter,String userId,String signEquipment,String ibeanconid) {
        super(activity, R.style.Transparent);
        this.mOwnerActivity = activity;
        this.sign1Presenter = sign1Presenter;
        this.userId = userId;
        this.signEquipment = signEquipment;
        this.ibeanconid = ibeanconid;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_blur_bg_layout);
        blurImage = (ImageView) findViewById(R.id.iv_blur_show);
        blurAlpha = (ImageView) findViewById(R.id.iv_blur_alpha);
        showView = (FrameLayout) findViewById(R.id.fl_add_views);
//        btnCheckIn = (Button) findViewById(R.id.btn_check_in);
//        btnCheckOut = (Button) findViewById(R.id.btn_check_out);
        rlCheckIn = (RelativeLayout) findViewById(R.id.rl_check_in);
        rlCheckOut = (RelativeLayout) findViewById(R.id.rl_check_out);
        rlCheckInButton = (RelativeLayout) findViewById(R.id.rl_button_check_in);
        rlCheckOutButton = (RelativeLayout) findViewById(R.id.rl_button_check_out);
        rlCheckOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonAnimation(rlCheckOutButton, 1.5f); //放大
                buttonAnimation(rlCheckInButton, 0.5f); //放大
                sign1Presenter.postSign(userId,signEquipment,"1",ibeanconid);
//                dismiss();
            }
        });
        rlCheckInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonAnimation(rlCheckInButton, 1.5f); //放大
                buttonAnimation(rlCheckOutButton, 0.5f); //放大
                sign1Presenter.postSign(userId,signEquipment,"0",ibeanconid);
//                dismiss();
            }
        });


//
//        btnCheckOut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                buttonAnimation(btnCheckOut, 1.5f); //放大
//                buttonAnimation(btnCheckIn, 0.5f); //放大
////                dismiss();
//            }
//        });
//        btnCheckIn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                buttonAnimation(btnCheckIn, 1.5f); //放大
//                buttonAnimation(btnCheckOut, 0.5f); //放大
////                dismiss();
//            }
//        });


        // 默认设置透明颜色为半透明黑色
        blurAlpha.setBackgroundColor(0x77000000);

        // 背景透明动画
        alphaAnimation = new AlphaAnimation(0f, 1f);
        alphaAnimation.setDuration(500);
        alphaAnimation.setFillAfter(true);

        // 创建Dialog
        togetherRun(rlCheckIn, 1000);
        togetherRun(rlCheckOut, 1000);
        onCreateDialog();
    }

    protected void setShowInAnimation(Animation dialogInAnim) {
        this.dialogInAnim = dialogInAnim;
    }

    /**
     * 用这个方法来代替之前的onCreate()方法
     */
    protected abstract void onCreateDialog();

    /**
     * 设置高斯模糊的前景颜色
     *
     * @param color 前景的颜色（使用ARGB来设置）
     */
    protected void setFilterColor(int color) {
        blurAlpha.setBackgroundColor(color);
    }

    /**
     * 此方法用来添加要在透明背景上显示的布局
     *
     * @param layoutResId
     */
    protected void setDialogView(int layoutResId) {
        showView.addView(View.inflate(getContext(), layoutResId, null));
    }

    public Activity getDialogActivity() {
        return mOwnerActivity;
    }

    /**
     * 显示Dialog类，同时进行动画播放
     */
    @Override
    public void show() {
        super.show();
        // 开始截屏并进行高斯模糊
        new BlurAsyncTask().execute();
        // 背景开始渐变
        if (alphaAnimation != null) {
            blurAlpha.startAnimation(alphaAnimation);
        }
        // 框弹出的动画
        if (dialogInAnim != null) {
            showView.startAnimation(dialogInAnim);
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        // 要设置下面这个，不然下次截图会返回上次的画面，不能实时更新
        mOwnerActivity.getWindow().getDecorView().setDrawingCacheEnabled(false);
        // 对bitmap进行回收
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
    }

    /**
     * 实现高斯模糊的任务
     */
    private class BlurAsyncTask extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // 截图
            mOwnerActivity.getWindow().getDecorView().setDrawingCacheEnabled(true);
            bitmap = mOwnerActivity.getWindow().getDecorView().getDrawingCache();
//            BitmapUtils.saveBitmapToFile(bitmap, "/sdcard/xinglian/my.jpg", Integer.MAX_VALUE);
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        @Override
        protected Object doInBackground(Object[] params) {
            // 进行高斯模糊
            if (bitmap != null) {
                bitmap = RenderScriptBlurHelper.doBlur(bitmap, 25, false, mOwnerActivity);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if (bitmap != null) {
                blurImage.setImageBitmap(bitmap);
            }
        }
    }

    public void togetherRun(View view, int dist) {

        float curTranslationY = view.getTranslationY();
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(view, "translationY",
                dist, curTranslationY);
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(view, "alpha",
                0f, 1f);
        AnimatorSet animSet = new AnimatorSet();
        animSet.setInterpolator(new BounceInterpolator());
        animSet.setDuration(1500);
//        animSet.setInterpolator(new LinearInterpolator());
        //两个动画同时执行
        animSet.playTogether(anim1, anim2);
        animSet.start();
    }

    public void buttonAnimation(View view, float scale) {
        final ScaleAnimation animation = new ScaleAnimation(1f, scale, 1f, scale,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(500);//设置动画持续时间
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                dismiss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(animation);
    }

}

