package whcs.wohui.zz.myview;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import whcs.wohui.zz.whcouldsupermarket.R;

/**
 * 说明：自定义的ProgressDialog
 * 作者：陈杰宇
 * 时间： 2016/1/22 11:24
 * 版本：V1.0
 * 修改历史：
 */
public class MyProgressDialog {

    private ProgressDialog mProgressDialog;
    private float mDimAmount;
    private int mWindowColor;
    private float mCornerRadius;
    private Context mContext;

    private int mAnimateSpeed;

    private int mMaxProgress;
    private boolean mIsAutoDismiss;

    public MyProgressDialog(Context context) {
        mContext = context;
        mProgressDialog = new ProgressDialog(context);
        mDimAmount = 0;
        //noinspection deprecation
        mWindowColor = context.getResources().getColor(R.color.MyProgressDialog_default_color);
        mAnimateSpeed = 1;
        mCornerRadius = 10;
        mIsAutoDismiss = true;
        mProgressDialog.setView(new SpinView(mContext));
    }

    /**
     * Create a new HUD. Have the same effect as the constructor.
     * For convenient only.
     * @param context Activity context that the HUD bound to
     * @return An unique HUD instance
     */
    public static MyProgressDialog create(Context context) {
        return new MyProgressDialog(context);
    }

    /**
     * Specify the dim area around the HUD, like in Dialog
     * @param dimAmount May take value from 0 to 1.
     *                  0 means no dimming, 1 mean darkness
     * @return Current HUD
     */
    public MyProgressDialog setDimAmount(float dimAmount) {
        if (dimAmount >= 0 && dimAmount <= 1) {
            mDimAmount = dimAmount;
        }
        return this;
    }

    /**
     * Set HUD size. If not the HUD view will use WRAP_CONTENT instead
     * @param width in dp
     * @param height in dp
     * @return Current HUD
     */
    public MyProgressDialog setSize(int width, int height) {
        mProgressDialog.setSize(width, height);
        return this;
    }

    /**
     * Specify the HUD background color
     * @param color ARGB color
     * @return Current HUD
     */
    public MyProgressDialog setWindowColor(int color) {
        mWindowColor = color;
        return this;
    }

    /**
     * Specify corner radius of the HUD (default is 10)
     * @param radius Corner radius in dp
     * @return Current HUD
     */
    public MyProgressDialog setCornerRadius(float radius) {
        mCornerRadius = radius;
        return this;
    }

    /**
     * Change animate speed relative to default. Only have effect when use with indeterminate style
     * @param scale 1 is default, 2 means double speed, 0.5 means half speed..etc.
     * @return Current HUD
     */
    public MyProgressDialog setAnimationSpeed(int scale) {
        mAnimateSpeed = scale;
        return this;
    }

    /**
     * Optional label to be displayed on the HUD
     * @return Current HUD
     */
    public MyProgressDialog setLabel(String label) {
        mProgressDialog.setLabel(label);
        return this;
    }

    /**
     * Optional detail description to be displayed on the HUD
     * @return Current HUD
     */
    public MyProgressDialog setDetailsLabel(String detailsLabel) {
        mProgressDialog.setDetailsLabel(detailsLabel);
        return this;
    }

    /**
     * Max value for use in one of the determinate styles
     * @return Current HUD
     */
    public MyProgressDialog setMaxProgress(int maxProgress) {
        mMaxProgress = maxProgress;
        return this;
    }

    /**
     * Provide a custom view to be displayed.
     * @param view Must not be null
     * @return Current HUD
     */
    public MyProgressDialog setCustomView(View view) {
        if (view != null) {
            mProgressDialog.setView(view);
        } else {
            throw new RuntimeException("Custom view must not be null!");
        }
        return this;
    }

    /**
     * Specify whether this HUD can be cancelled by using back button (default is false)
     * @return Current HUD
     */
    public MyProgressDialog setCancellable(boolean isCancellable) {
        mProgressDialog.setCancelable(isCancellable);
        return this;
    }

    /**
     * Specify whether this HUD closes itself if progress reaches max. Default is true.
     * @return Current HUD
     */
    public MyProgressDialog setAutoDismiss(boolean isAutoDismiss) {
        mIsAutoDismiss = isAutoDismiss;
        return this;
    }

    public MyProgressDialog show() {
        if (!isShowing()) {
            mProgressDialog.show();
        }
        return this;
    }

    public boolean isShowing() {
        return mProgressDialog != null && mProgressDialog.isShowing();
    }

    public void dismiss() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    private class SpinView extends ImageView{

        private float mRotateDegrees;
        private int mFrameTime;
        private boolean mNeedToUpdateView;
        private Runnable mUpdateViewRunnable;

        public SpinView(Context context) {
            super(context);
            init();
        }

        public SpinView(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        private void init() {
            setImageResource(R.drawable.myprogressdialog_spinner);
            mFrameTime = 1000 / 12;
            mUpdateViewRunnable = new Runnable() {
                @Override
                public void run() {
                    mRotateDegrees += 30;
                    mRotateDegrees = mRotateDegrees < 360 ? mRotateDegrees : mRotateDegrees - 360;
                    invalidate();
                    if (mNeedToUpdateView) {
                        postDelayed(this, mFrameTime);
                    }
                }
            };
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.rotate(mRotateDegrees, getWidth() / 2, getHeight() / 2);
            super.onDraw(canvas);
        }

        @Override
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            mNeedToUpdateView = true;
            post(mUpdateViewRunnable);
        }

        @Override
        protected void onDetachedFromWindow() {
            mNeedToUpdateView = false;
            super.onDetachedFromWindow();
        }
    }

    private class ProgressDialog extends Dialog {


        private View mView;
        private TextView mLabelText;
        private TextView mDetailsText;
        private String mLabel;
        private String mDetailsLabel;
        private FrameLayout mCustomViewContainer;
        private LinearLayout mBackgroundLayout;
        private int mWidth, mHeight;

        public ProgressDialog(Context context) {
            super(context);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.myprogressdialog_hud);
            Window window = getWindow();
            window.setBackgroundDrawable(new ColorDrawable(0));
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.dimAmount = mDimAmount;
            window.setAttributes(layoutParams);
            setCanceledOnTouchOutside(false);
            initViews();
        }

        private void initBackground(int color, float cornerRadius) {
            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(GradientDrawable.RECTANGLE);
            drawable.setColor(color);
            drawable.setCornerRadius(cornerRadius);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mBackgroundLayout.setBackground(drawable);
            } else {
                //noinspection deprecation
                mBackgroundLayout.setBackgroundDrawable(drawable);
            }
        }

        private void initViews() {
            mBackgroundLayout = (LinearLayout) findViewById(R.id.background);
            initBackground(mWindowColor,mCornerRadius);
            mCustomViewContainer = (FrameLayout) findViewById(R.id.container);
            addViewToFrame(mView);
        }

        private void addViewToFrame(View view) {
            if (view == null) return;
            int wrapParam = ViewGroup.LayoutParams.WRAP_CONTENT;
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(wrapParam, wrapParam);
            mCustomViewContainer.addView(view, params);
        }

        public void setView(View view) {
            if (view != null) {
                mView = view;
                if (isShowing()) {
                    mCustomViewContainer.removeAllViews();
                    addViewToFrame(view);
                }
            }
        }

        public void setLabel(String label) {
            mLabel = label;
            if (mLabelText != null) {
                if (label != null) {
                    mLabelText.setText(label);
                    mLabelText.setVisibility(View.VISIBLE);
                } else {
                    mLabelText.setVisibility(View.GONE);
                }
            }
        }

        public void setDetailsLabel(String detailsLabel) {
            mDetailsLabel = detailsLabel;
            if (mDetailsText != null) {
                if (detailsLabel != null) {
                    mDetailsText.setText(detailsLabel);
                    mDetailsText.setVisibility(View.VISIBLE);
                } else {
                    mDetailsText.setVisibility(View.GONE);
                }
            }
        }

        public void setSize(int width, int height) {
            mWidth = width;
            mHeight = height;
        }
    }
}
