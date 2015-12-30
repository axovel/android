package com.axovel.ecommerce.util;

import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.axovel.ecommerce.R;
import com.axovel.ecommerce.model.RequestResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Umesh Chauhan on 29-12-2015.
 * Axovel Private Limited
 */
public class OnPageChangeListenerForInfiniteIndicator implements ViewPager.OnPageChangeListener {
    private Activity activity;
    private List<ImageView> pageIndicatorList = new ArrayList<ImageView>();
    private ArrayList<RequestResponse> bannerList;
    private LinearLayout containerIndicator;
    private int viewPagerActivePosition;
    private int positionToUse = 0;
    private int actualPosition;

    public OnPageChangeListenerForInfiniteIndicator(Activity activity, ArrayList<RequestResponse> bannerList, int currentItem) {
        this.activity = activity;
        this.bannerList = bannerList;
        this.actualPosition = currentItem;
        this.viewPagerActivePosition = currentItem;
        loadIndicators();
    }

    private void loadIndicators() {
        containerIndicator = (LinearLayout) activity.findViewById(R.id.layout_banner_indicator);
        if (pageIndicatorList.size() < 1) {
            for (RequestResponse banner : bannerList) {
                ImageView imageView = new ImageView(activity);
                imageView.setImageResource(R.mipmap.ic_banner_normal);// normal indicator image
                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                imageView.setLayoutParams(new ViewGroup.LayoutParams(activity.getResources().getDimensionPixelOffset(R.dimen.home_banner_indicator_width),
                        ViewGroup.LayoutParams.MATCH_PARENT));
                pageIndicatorList.add(imageView);
            }
        }
        containerIndicator.removeAllViews();
        for (int x = 0; x < pageIndicatorList.size(); x++) {
            ImageView imageView = pageIndicatorList.get(x);
            imageView.setImageResource(x == positionToUse ? R.mipmap.ic_banner_selecteed :
                    R.mipmap.ic_banner_normal); // active and notactive indicator
            containerIndicator.addView(imageView);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        actualPosition = position;
        int positionToUseOld = positionToUse;
        if (actualPosition < viewPagerActivePosition && positionOffset < 0.5f) {
            positionToUse = actualPosition % bannerList.size();
        } else {
            if (positionOffset > 0.5f) {
                positionToUse = (actualPosition + 1) % bannerList.size();
            } else {
                positionToUse = actualPosition % bannerList.size();
            }
        }
        if (positionToUseOld != positionToUse) {
            loadIndicators();
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == 0) {
            viewPagerActivePosition = actualPosition;
            positionToUse = viewPagerActivePosition % bannerList.size();
            loadIndicators();
        }
    }
}
