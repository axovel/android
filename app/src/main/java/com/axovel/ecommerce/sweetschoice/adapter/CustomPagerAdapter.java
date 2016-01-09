package com.axovel.ecommerce.sweetschoice.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.support.v4.view.PagerAdapter;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.axovel.ecommerce.sweetschoice.R;
import com.axovel.ecommerce.sweetschoice.model.RequestResponse;
import com.axovel.ecommerce.sweetschoice.util.ImageLoadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Umesh Chauhan on 29-12-2015.
 * Axovel Private Limited
 */
public class CustomPagerAdapter extends PagerAdapter {

    Activity mContext;
    LayoutInflater mLayoutInflater;
    ArrayList<RequestResponse> bannerUrl;

    public CustomPagerAdapter(Activity context, ArrayList<RequestResponse> bannerUrl) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.bannerUrl = bannerUrl;
    }

    @Override
    public int getCount() {
        return bannerUrl.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.layout_banner, container, false);

        ImageView imgBanner = (ImageView) itemView.findViewById(R.id.imgBanner);
        Picasso.with(mContext).load("http://i.imgur.com/DvpvklR.png")
                .error(R.mipmap.ic_launcher)
                .placeholder(R.drawable.placeholder)
                .fit()
                .into(imgBanner);

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
