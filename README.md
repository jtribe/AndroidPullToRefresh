AndroidPullToRefresh
====================

![image](https://raw.github.com/jtribe/AndroidPullToRefresh/master/pullToRefreshAnimation.gif)


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

License
-------


Copyright 2013 JTRIBE HOLDINGS PTY

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
