package au.com.jtribe.pulltorefresh;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by tosa on 8/14/13.
 */
public class SimpleAdapter extends BaseAdapter {

    private final List<String> mList;
    private final LayoutInflater mInflator;

    public SimpleAdapter(Context context, List<String> list) {
        this.mList = list;
        this.mInflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            v = mInflator.inflate(R.layout.list_item,null,false);
        }
        Holder holder = (Holder) v.getTag();
        if (holder == null) {
            holder = new Holder();
            holder.text = (TextView) v.findViewById(R.id.text);
            holder.image = (ImageView) v.findViewById(R.id.dummyImage); // clickable image to test interaction with it
            v.setTag(holder);
        }
        final String itemString = (String) getItem(position);
        holder.text.setText(itemString);
        holder.image.setClickable(true);
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mInflator.getContext(), "TEST click " + itemString, Toast.LENGTH_SHORT).show();
            }
        });
        return v;
    }

    class Holder {
        TextView text;
        ImageView image;
    }
}
