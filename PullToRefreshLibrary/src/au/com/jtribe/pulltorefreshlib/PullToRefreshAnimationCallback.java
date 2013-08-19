package au.com.jtribe.pulltorefreshlib;

/**
 * Created by tosa on 8/15/13.
 */
public interface PullToRefreshAnimationCallback {
    void onLoaded();
    int getFirstVisiblePosition();
}
