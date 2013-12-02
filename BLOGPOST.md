# Android pull to refresh animation
We recently did the Movember android app and we wanted to do a fancy pull to refresh animation. The idea was to have a snake being stretched when you pull down the list. After coming up with a solution I thought it might be worth sharing it as a little library.

Most of the time there are pretty good and customisable solutions around, but this time it seemed like all the pull to refresh libraries out there were not sufficient for our needs.
I had a look at a few libraries, for example https://github.com/chrisbanes/Android-PullToRefresh and https://github.com/johannilsson/android-pulltorefresh
most of them had one animation build in which wasn't easy to replace.

It was important for us to be able to navigate back and forth through they keyframes in the animation. So if you pull down the list and the then push it back up it should actually animate back.

Here is what we achieved:

![image](https://raw.github.com/jtribe/AndroidPullToRefresh/master/movember_pulltorefresh.gif)

After having a look into the existing code base of the libraries mentioned above, I got a good understanding of how they work. I started out by just using a simple onTouchListener using **setOnTouchListener** and a header view on the list view. One big problem with that approach was that the list view did intercept certain events. For instance clickable views inside the list rows would intercept the motion events if you just have a touch listener on the list view.

So the final approach was to extend the ListView and overwrite the onInterceptTouchEvent of the list view, so we would have all the power of the view group.

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

On **onInterceptTouchEvent** we initiate the pull to refresh, but only if the first visible item position is **0**.
If so we want to intercept the **ACTION_DOWN** event. As we move the list up and down we want go through the keyframes without scrolling the list. This is  achieved by the overwriting **onTouch** which will return true to intercept events when we are doing a pull to refresh.

To display the correct keyframe we need to calculate the range we want to animate in and divide it into equal parts for which we set a certain keyframe. All is done in the PullToRefreshAnimation class, which should be fairly easy to extend and modify as you can see in the sample project.

	int image_index = (int) ((mTotalDistance - getAnimationStart() - getMarginStart()) / range);
	int chosen_index = image_index;
	if (image_index >= 0 && image_index < getAnimationFrames()) {
		mCurrentIndex = chosen_index;
        setAnimationForFrame(mCurrentIndex);
   	}

I created a [sample project](https://github.com/jtribe/AndroidPullToRefresh/tree/master/PullToRefresh) which demonstrates a keyframe animation of the android figure below, the pull to refresh functionality is extracted into a library project.[http://github.com:jtribe/AndroidPullToRefresh.git](http://github.com:jtribe/AndroidPullToRefresh.git)

![image](https://raw.github.com/jtribe/AndroidPullToRefresh/master/PullToRefresh/res/drawable-xhdpi/android_anim_0.png)
![image](https://raw.github.com/jtribe/AndroidPullToRefresh/master/PullToRefresh/res/drawable-xhdpi/android_anim_30.png)
)
![image](https://raw.github.com/jtribe/AndroidPullToRefresh/master/PullToRefresh/res/drawable-xhdpi/android_anim_40.png)
![image](https://raw.github.com/jtribe/AndroidPullToRefresh/master/PullToRefresh/res/drawable-xhdpi/android_anim_54.png)

I used sync studio to create a key frame animation of the android vector graphic image [www.synfig.org](www.synfig.org). The user interface of that tool isn't very intuitive, but its easy to get started. You can find my .sifz file in the sample project assets folder.

Checkout the project on github, feel free to fork it and send pull requests. [http://github.com:jtribe/AndroidPullToRefresh.git](http://github.com:jtribe/AndroidPullToRefresh.git)

Or shoot me an email at tobias@jtribe.com.au or via [@tosa](https://twitter.com/intent/user?screen_name=tosa) or on [google plus](https://www.google.com/+TobiasS)
