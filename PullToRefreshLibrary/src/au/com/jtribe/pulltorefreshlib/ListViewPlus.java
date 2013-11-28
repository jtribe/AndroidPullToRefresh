package au.com.jtribe.pulltorefreshlib;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by tosa on 8/14/13.
 */
public class ListViewPlus extends ListView implements PullToRefreshAnimationCallback {


    private PullToRefreshAnimation mPullToRefreshAnimation;

    @Override
    public void onLoaded() {
    }

    public ListViewPlus(Context context) {
        super(context);
    }

    public ListViewPlus(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListViewPlus(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    public void setAdapter(ListAdapter adapter) {
        resetHeader();
        super.setAdapter(adapter);
    }

    public void setPullToRefreshWithAdapter(PullToRefreshAnimation pullToRefreshAnimation) {
        addHeaderView(pullToRefreshAnimation.getHeader(), null, false);
        pullToRefreshAnimation.setPullToRefreshAnimationCallback(this);
        this.mPullToRefreshAnimation = pullToRefreshAnimation;
        this.mPullToRefreshAnimation.loadAnimation();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.mPullToRefreshAnimation != null) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (this.mPullToRefreshAnimation.onDownIntercept(event)) {
                        return true;
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if(this.mPullToRefreshAnimation.onMoveIntercept(event)) {
                        return true;
                    }
                    break;
            }
        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        boolean intercept = false;
        if (this.mPullToRefreshAnimation != null) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_CANCEL:
                    intercept = mPullToRefreshAnimation.onDown(event);
                    break;
                case MotionEvent.ACTION_UP:
                    intercept = mPullToRefreshAnimation.onUp(event);
                    break;
                case MotionEvent.ACTION_MOVE:
                    intercept = mPullToRefreshAnimation.onMove(event);
                    break;
            }
        }
        return intercept || super.onTouchEvent(event);
    }

    private void resetHeader() {
        this.mPullToRefreshAnimation.resetHeader();
    }

    public void resetPullToRefresh() {
        if (mPullToRefreshAnimation != null)
            mPullToRefreshAnimation.resetHeader();

    }

    public void onRefreshComplete() {
        if (mPullToRefreshAnimation != null)
            mPullToRefreshAnimation.refreshComplete();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        try {
            super.dispatchDraw(canvas);
        } catch (IndexOutOfBoundsException e) {
            // samsung error
        }
    }
}
