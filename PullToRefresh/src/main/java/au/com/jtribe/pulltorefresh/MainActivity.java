package au.com.jtribe.pulltorefresh;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import au.com.jtribe.pulltorefreshlib.ListViewPlus;
import au.com.jtribe.pulltorefreshlib.PullToRefreshCallback;

public class MainActivity extends Activity implements PullToRefreshCallback {

    private ListViewPlus mListView;
    private DroidPullToRefreshAnimation mDroidPullToRefreshAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListViewPlus) findViewById(R.id.listview);
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < 50; i++) {
            list.add("List item " + i + 1);
        }
        SimpleAdapter adapter = new SimpleAdapter(this, list);
        mDroidPullToRefreshAnimation = new DroidPullToRefreshAnimation(getBaseContext(), R.layout.list_header_animated, R.id.animationView, 0, this);
        mListView.setPullToRefreshWithAdapter(mDroidPullToRefreshAnimation);
        mListView.setAdapter(adapter);
    }

    @Override
    public void onRefresh() {
        new GetNewItemsFromServer().execute();
    }

    class GetNewItemsFromServer extends AsyncTask<Void, Void, List<String>> {

        @Override
        protected List<String> doInBackground(Void... params) {
            List<String> list = new ArrayList<String>();
            for (int i = 0; i < 50; i++) {
                list.add("List item " + i + 1);
            }

            try {
                Thread.sleep(1000l);
            } catch (InterruptedException e) {
                // nothing really, just keep going
            }

            return list;
        }

        @Override
        protected void onPostExecute(List<String> strings) {
            SimpleAdapter adapter = new SimpleAdapter(MainActivity.this, strings);
            mListView.setAdapter(adapter);
        }
    }
}
