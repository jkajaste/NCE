package com.ghettoapps.nce;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by kajajuh on 21.10.2015.
 */
public class SearchListAdapter extends ArrayAdapter<SearchResult> {

    private static final String TAG = "SearchListAdapter";
    private final List<SearchResult> mData;
    private final Context mContext;
    private final int mListItemLayoutId;

    public SearchListAdapter(Context context, int listItemLayoutId,
                              List<SearchResult> objects) {
        super(context, listItemLayoutId, objects);
        mContext = context;
        mData = objects;
        mListItemLayoutId = listItemLayoutId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            Log.d(TAG, "null");
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mListItemLayoutId, parent, false);
            holder.nameTv = (TextView) convertView.findViewById(R.id.venue_name);
            holder.addressTv = (TextView) convertView.findViewById(R.id.venue_address);
            holder.distanceTv = (TextView) convertView.findViewById(R.id.venue_distance);
            convertView.setTag(holder);
        } else {
            Log.d(TAG, "ready");
            holder = (ViewHolder) convertView.getTag();
        }

        holder.nameTv.setText(mData.get(position).getName());
        holder.distanceTv.setText(mData.get(position).getDistance());

        // We shouldn't really create objects in getView..
        String address = mData.get(position).getAddress();
        if (address != null) {
            holder.addressTv.setText(address);
        } else {
            holder.addressTv.setVisibility(View.GONE);
        }

        return convertView;
    }

    static class ViewHolder {
        TextView nameTv;
        TextView distanceTv;
        TextView addressTv;
    }
}
