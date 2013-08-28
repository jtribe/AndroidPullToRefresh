package au.com.jtribe.pulltorefreshlib;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by tosa on 8/14/13.
 */
public abstract class PullToRefreshAnimation {

    private static final int IDLE_DISTANCE = 5;
    private static final float PULL_RESISTANCE = 1.1f;

    protected final ImageView mAnimationView;
    protected final Context mContext;
    protected final PullToRefreshCallback mRefreshCallback;
    private final float mDensityFactor;

    protected int mCurrentIndex;
    protected PullToRefreshAnimationCallback mAnimationCallback;

    protected final View mHeaderView;
    private boolean mRefreshStarted;
    private State state = State.PULL_TO_REFRESH;
    private float mTotalDistance;
    private float mPreviousY;
    private float mScrollStartY;
	private float mPreviousX;

    protected abstract void loadAnimation();

    protected abstract void setMarginForAnimationView(int i);

    protected abstract void setAnimationForFrame(int mCurrentIndex);

    protected abstract float getMarginStart();

    protected abstract float getAnimationHeight();

    protected abstract float getAnimationEndHeight();

    protected abstract int getAnimationFrames();

    protected abstract float getAnimationStart();

    protected abstract void animateRefresh();

    protected abstract void animateBackAndReset();

    protected abstract void resetHeader();

    public abstract boolean onDown(MotionEvent event);

    public PullToRefreshAnimation(Context context, int layoutId, int animationViewId, int offset, PullToRefreshCallback callback) {
        this.mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mHeaderView = inflater.inflate(layoutId, null, false);
        mAnimationView = (ImageView) mHeaderView.findViewById(animationViewId);
        Resources r = context.getResources();
        mDensityFactor = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1,
                r.getDisplayMetrics());
        mRefreshCallback = callback;
    }

    public View getHeader() {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mAnimationView
                .getLayoutParams();
        params.setMargins(0, (int) -getAnimationEndHeight(), 0, 0);
        mAnimationView.setLayoutParams(params);
        return mHeaderView;
    }

    public void setPullToRefreshAnimationCallback(PullToRefreshAnimationCallback callback) {
        mAnimationCallback = callback;
    }

    public boolean onDownIntercept(MotionEvent event) {
        boolean intercept = false;
        mTotalDistance = 0;
        if (mAnimationCallback.getFirstVisiblePosition() == 0) {
            mPreviousY = event.getY();
            mPreviousX = event.getX();
        } else {
            mPreviousY = -1;
        }
        // Remember where have we started
        mScrollStartY = event.getY();
        return intercept;
    }

    public boolean onMoveIntercept(MotionEvent event) {
    	float yMovment = event.getY() - mPreviousY;
    	float xMovment = event.getX() - mPreviousX;
        if ((mPreviousY != -1
                && mAnimationCallback.getFirstVisiblePosition() == 0 &&
                		event.getY() - mScrollStartY > IDLE_DISTANCE) && Math.abs(yMovment) > Math.abs(xMovment)) {
            return true; // intercept this so we can do the animation
        }
        return false;
    }

    public boolean onMove(MotionEvent event) {
        if ((mPreviousY != -1
                && mAnimationCallback.getFirstVisiblePosition() == 0
                && event.getY() - mScrollStartY > IDLE_DISTANCE) || mRefreshStarted) {

            float y = event.getY();
            float diff = y - mPreviousY;

            if (diff > 0) diff /= PULL_RESISTANCE;


            float newTotalDistance = mTotalDistance + diff;
            if (newTotalDistance != mTotalDistance) {

                mRefreshStarted = true;
                if (mTotalDistance <= getAnimationStart()) {
                    mTotalDistance += (y - mPreviousY) * (4.2/mDensityFactor);
                } else {
                    mTotalDistance = newTotalDistance;
                }

                if (mTotalDistance <= getAnimationEndHeight() + getMarginStart()) {
                    setMarginForAnimationView((int) (mTotalDistance - getAnimationEndHeight()));
                }

                Log.d("PULLTOREFRESH", "TOTAL: " + mTotalDistance);
                if (mTotalDistance <= getAnimationEndHeight()) {
                    float range = (getAnimationEndHeight() - getAnimationStart() - getMarginStart())
                            / getAnimationFrames();

                    int image_index = (int) ((mTotalDistance - getAnimationStart() - getMarginStart()) / range);
                    int chosen_index = image_index;

                    if (image_index >= 0
                            && image_index < getAnimationFrames()) {
                        mCurrentIndex = chosen_index;
                        setAnimationForFrame(mCurrentIndex);
                    } else if (image_index > 0) {
                        setAnimationForFrame(getAnimationFrames() - 1);
                    } else {
                        setAnimationForFrame(0);
                    }
                    if (chosen_index > getAnimationFrames() * 0.75
                            && state != State.REFRESHING) {
                        state = State.RELEASE_TO_REFRESH;
                    } else {
                        state = State.PULL_TO_REFRESH;
                    }
                }
            }
            mPreviousY = y;
            return true;
        }
        return false;
    }

    public boolean onUp(MotionEvent event) {
        boolean intercept = false;
        if (mPreviousY != -1
                && (state == State.RELEASE_TO_REFRESH || (mAnimationCallback.getFirstVisiblePosition() == 0))) {
            switch (state) {
                case RELEASE_TO_REFRESH:
                    state = State.REFRESHING;
                    animateRefresh();
                    break;

                case PULL_TO_REFRESH:
                    animateBackAndReset();
                    break;
            }
        }
        if (mRefreshStarted) {
            mRefreshStarted = false;
            intercept = true;
        }
        return intercept;
    }

    public boolean onCancel(MotionEvent event) {
        resetHeader();
        return false;
    }

    public void refreshComplete() {
        state = State.PULL_TO_REFRESH;
        resetHeader();
    }

    private static enum State {
        PULL_TO_REFRESH, RELEASE_TO_REFRESH, REFRESHING
    }

}
