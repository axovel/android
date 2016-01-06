package com.axovel.ecommerce.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.axovel.ecommerce.R;
import com.axovel.ecommerce.model.NavDrawer;
import com.axovel.ecommerce.util.ImageLoadTask;

import java.util.ArrayList;

/**
 * Created by Umesh Chauhan on 29-12-2015.
 * Axovel Private Limited
 */
public class NavDrawerListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<NavDrawer> navDrawer;

    public NavDrawerListAdapter(Context context, ArrayList<NavDrawer> navDrawer){
        this.context = context;
        this.navDrawer = navDrawer;
    }

    @Override
    public int getCount() {
        return navDrawer.size();
    }

    @Override
    public Object getItem(int position) {
        return navDrawer.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.drawer_list_header_item, null);
        }

        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
        TextView txtCount = (TextView) convertView.findViewById(R.id.counter);
        /*// get Images from URL
        new ImageLoadTask(navDrawer.get(position).getIconUrl(), imgIcon, position).execute();
        txtTitle.setText(navDrawer.get(position).getTitle());

        // displaying count
        // check whether it set visible or not
        if(navDrawer.get(position).getCounterVisibility()){
            txtCount.setText(navDrawer.get(position).getCount());
        }else{
            // hide the counter view
            txtCount.setVisibility(View.GONE);
        }*/
        return convertView;
    }
}
