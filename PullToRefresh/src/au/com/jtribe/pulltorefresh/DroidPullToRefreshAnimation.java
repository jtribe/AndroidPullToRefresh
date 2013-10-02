package au.com.jtribe.pulltorefresh;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.IntEvaluator;
import com.nineoldandroids.animation.ValueAnimator;

import au.com.jtribe.pulltorefreshlib.PullToRefreshAnimation;
import au.com.jtribe.pulltorefreshlib.PullToRefreshCallback;

/**
 * Created by tosa on 8/15/13.
 */
public class DroidPullToRefreshAnimation extends PullToRefreshAnimation {

    public static final int ANIMATION_FRAMES = 55;
    private final float mAnimHeight;
    private final float mAnimHeightEnd;
    private final float mAnimStart;
    private final float mMarginStart;
    private Drawable[] mDrawables;


    public DroidPullToRefreshAnimation(Context context, int layoutId, int animationViewId, int offset, PullToRefreshCallback callback) {
        super(context, layoutId, animationViewId, offset, callback);
        Resources r = context.getResources();
        mAnimHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 162,
                r.getDisplayMetrics());
        mAnimHeightEnd = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                270, r.getDisplayMetrics());
        mAnimStart = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 109,
                r.getDisplayMetrics());
        mMarginStart = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 110,
                r.getDisplayMetrics()) + offset;
    }

    @Override
    public void loadAnimation() {
        new LoadDrawablesInBackgroundTask().execute();
    }

    @Override
    protected void setMarginForAnimationView(int i) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) this.mAnimationView.getLayoutParams();
        params.topMargin = i;
        this.mAnimationView.setLayoutParams(params);
    }

    @Override
    protected void setAnimationForFrame(int currentIndex) {
        if (mDrawables != null && mDrawables.length > currentIndex)
        this.mAnimationView.setImageDrawable(mDrawables[currentIndex]);
    }
    @Override
    protected float getMarginStart() {
        return mMarginStart;
    }

    @Override
    protected float getAnimationStart() {
        return mAnimStart;
    }

    @Override
    public boolean onDown(MotionEvent event) {
        return false;
    }

    @Override
    protected float getAnimationHeight() {
        return mAnimHeight;
    }

    @Override
    protected float getAnimationEndHeight() {
        return mAnimHeightEnd;
    }

    @Override
    protected int getAnimationFrames() {
        return ANIMATION_FRAMES;
    }

    @Override
    protected void animateRefresh() {
        ValueAnimator animator = ValueAnimator.ofInt(mCurrentIndex,0);
        final float range = (getAnimationEndHeight() - getAnimationStart() - getMarginStart())
                / getAnimationFrames();
        animator.setDuration(400);
        animator.setEvaluator(new IntEvaluator() {

            @Override
            public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
                int num = (Integer)super.evaluate(fraction, startValue, endValue);
                mAnimationView.setImageDrawable(mDrawables[num]);
                return num;
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mRefreshCallback.onRefresh();
                mCurrentIndex = 0;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentIndex = 0;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }

    @Override
    protected void animateBackAndReset() {
        ValueAnimator animator = ValueAnimator.ofInt(mCurrentIndex,0);
        final float range = (getAnimationEndHeight() - getAnimationStart() - getMarginStart())
                / getAnimationFrames();
        animator.setDuration(400);
        animator.setEvaluator(new IntEvaluator() {

            @Override
            public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
                int num = (Integer)super.evaluate(fraction, startValue, endValue);
                mAnimationView.setImageDrawable(mDrawables[num]);
                return num;
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                resetHeader();
                mCurrentIndex = 0;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentIndex = 0;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }

    @Override
    public boolean onCancel(MotionEvent event) {
        return false;
    }

    @Override
    protected void resetHeader() {
        final ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mAnimationView.getLayoutParams();
        ValueAnimator animator = ValueAnimator.ofInt(params.topMargin,(int) - getAnimationEndHeight());

        animator.setEvaluator(new IntEvaluator() {

            @Override
            public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
                int num = (Integer)super.evaluate(fraction, startValue, endValue);
                params.setMargins(0,num, 0, 0);
                mAnimationView.setLayoutParams(params);
                return num;
            }
        });
        animator.setDuration(200);
        animator.start();
    }


    class LoadDrawablesInBackgroundTask extends AsyncTask<Void,Void,Drawable[]> {
        @Override
        protected Drawable[] doInBackground(Void... params) {
            Resources res = mContext.getResources();
            Drawable[] drawables = new Drawable[ANIMATION_FRAMES];
            drawables[0] = res.getDrawable(R.drawable.android_anim_0);
            drawables[1] = res.getDrawable(R.drawable.android_anim_1);
            drawables[2] = res.getDrawable(R.drawable.android_anim_2);
            drawables[3] = res.getDrawable(R.drawable.android_anim_3);
            drawables[4] = res.getDrawable(R.drawable.android_anim_4);
            drawables[5] = res.getDrawable(R.drawable.android_anim_5);
            drawables[6] = res.getDrawable(R.drawable.android_anim_6);
            drawables[7] = res.getDrawable(R.drawable.android_anim_7);
            drawables[8] = res.getDrawable(R.drawable.android_anim_8);
            drawables[9] = res.getDrawable(R.drawable.android_anim_9);
            drawables[10] = res.getDrawable(R.drawable.android_anim_10);
            drawables[11] = res.getDrawable(R.drawable.android_anim_11);
            drawables[12] = res.getDrawable(R.drawable.android_anim_12);
            drawables[13] = res.getDrawable(R.drawable.android_anim_13);
            drawables[14] = res.getDrawable(R.drawable.android_anim_14);
            drawables[15] = res.getDrawable(R.drawable.android_anim_15);
            drawables[16] = res.getDrawable(R.drawable.android_anim_16);
            drawables[17] = res.getDrawable(R.drawable.android_anim_17);
            drawables[18] = res.getDrawable(R.drawable.android_anim_18);
            drawables[19] = res.getDrawable(R.drawable.android_anim_19);
            drawables[20] = res.getDrawable(R.drawable.android_anim_20);
            drawables[21] = res.getDrawable(R.drawable.android_anim_21);
            drawables[22] = res.getDrawable(R.drawable.android_anim_22);
            drawables[23] = res.getDrawable(R.drawable.android_anim_23);
            drawables[24] = res.getDrawable(R.drawable.android_anim_24);
            drawables[25] = res.getDrawable(R.drawable.android_anim_25);
            drawables[26] = res.getDrawable(R.drawable.android_anim_26);
            drawables[27] = res.getDrawable(R.drawable.android_anim_27);
            drawables[28] = res.getDrawable(R.drawable.android_anim_28);
            drawables[29] = res.getDrawable(R.drawable.android_anim_29);
            drawables[30] = res.getDrawable(R.drawable.android_anim_30);
            drawables[31] = res.getDrawable(R.drawable.android_anim_31);
            drawables[32] = res.getDrawable(R.drawable.android_anim_32);
            drawables[33] = res.getDrawable(R.drawable.android_anim_33);
            drawables[34] = res.getDrawable(R.drawable.android_anim_34);
            drawables[35] = res.getDrawable(R.drawable.android_anim_35);
            drawables[36] = res.getDrawable(R.drawable.android_anim_36);
            drawables[37] = res.getDrawable(R.drawable.android_anim_37);
            drawables[38] = res.getDrawable(R.drawable.android_anim_38);
            drawables[39] = res.getDrawable(R.drawable.android_anim_39);
            drawables[40] = res.getDrawable(R.drawable.android_anim_40);
            drawables[41] = res.getDrawable(R.drawable.android_anim_41);
            drawables[42] = res.getDrawable(R.drawable.android_anim_42);
            drawables[43] = res.getDrawable(R.drawable.android_anim_43);
            drawables[44] = res.getDrawable(R.drawable.android_anim_44);
            drawables[45] = res.getDrawable(R.drawable.android_anim_45);
            drawables[46] = res.getDrawable(R.drawable.android_anim_46);
            drawables[47] = res.getDrawable(R.drawable.android_anim_47);
            drawables[48] = res.getDrawable(R.drawable.android_anim_48);
            drawables[49] = res.getDrawable(R.drawable.android_anim_49);
            drawables[50] = res.getDrawable(R.drawable.android_anim_50);
            drawables[51] = res.getDrawable(R.drawable.android_anim_51);
            drawables[52] = res.getDrawable(R.drawable.android_anim_52);
            drawables[53] = res.getDrawable(R.drawable.android_anim_53);
            drawables[54] = res.getDrawable(R.drawable.android_anim_54);
            return drawables;
        }

        @Override
        protected void onPostExecute(Drawable[] drawables) {
            mDrawables = drawables;
            mAnimationCallback.onLoaded();
        }
    }
}
