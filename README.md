AndroidPullToRefresh
====================

Keyframe animated pull to refresh, easy to extend your own PullToRefreshAnimation.

User ListViewPlus in your project:

    SimpleAdapter adapter = new SimpleAdapter(this, list);
    mDroidPullToRefreshAnimation = new DroidPullToRefreshAnimation(getBaseContext(), R.layout.list_header_animated, R.id.animationView, 0, this);
    mListView.setPullToRefreshWithAdapter(mDroidPullToRefreshAnimation);
    mListView.setAdapter(adapter);

Extend PullToRefreshAnimation to create a keyframe animation yourself.

    public class DroidPullToRefreshAnimation extends PullToRefreshAnimation {
    }


I used synf studio to create the keyframe animation. [www.synfig.org](www.synfig.org)

Let me know if you have any troubles at tobias@jtribe.com.au