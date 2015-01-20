package org.nunocky.applist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.nunocky.applist.apploader.AppEntry;

import java.util.List;

public class AppListAdapter extends ArrayAdapter<AppEntry> {
    private final LayoutInflater mInflater;

    public AppListAdapter(Context context) {
        super(context, R.layout.applist);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    void setData(List<AppEntry> data) {
        clear();
        if (data != null) {
            for (AppEntry ent : data) {
                add(ent); // MEMO : method addAll(data) doesn't exist in support lib.
            }
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.applist, parent, false);
        }

        AppEntry item = getItem(position);

        ImageView iv = (ImageView) convertView.findViewById(R.id.icon);
        iv.setImageDrawable(item.getIcon());

        TextView tv = (TextView) convertView.findViewById(R.id.text);
        tv.setText(item.getLabel());

        return convertView;
    }
}
