package com.axovel.ecommerce.view.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.axovel.ecommerce.ECommerceApplication;
import com.axovel.ecommerce.R;
import com.axovel.ecommerce.adapter.CustomPagerAdapter;
import com.axovel.ecommerce.adapter.NavDrawerListAdapter;
import com.axovel.ecommerce.model.NavDrawer;
import com.axovel.ecommerce.model.RequestResponse;
import com.axovel.ecommerce.util.OnPageChangeListenerForInfiniteIndicator;

import java.util.ArrayList;

/**
 * Created by Umesh Chauhan on 28-12-2015.
 * Axovel Private Limited
 */
public class Activity_Ecomm_Base extends Activity{

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    // used to store app title
    private CharSequence mTitle;
    // nav drawer title
    private CharSequence mDrawerTitle;
    private ArrayList<NavDrawer> mNavDrawerItems;
    private NavDrawerListAdapter adapter;
    private Bundle savedInstanceState;
    CustomPagerAdapter mCustomPagerAdapter;
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        setContentView(R.layout.activity_ecomm_base);
        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

        ECommerceApplication.mAppProgressDialog = new ProgressDialog(this);
        ECommerceApplication.mAppProgressDialog.show();
        // Network Request get Nav Drawer Item


        // Setting Banner


    }

    private void setNavDrawerItems(String response){
        try {
            mNavDrawerItems = ECommerceApplication.mapper.readValue(
                    response,
                    ECommerceApplication.mapper.getTypeFactory().constructCollectionType(
                            ArrayList.class, NavDrawer.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(),
                mNavDrawerItems);
        mDrawerList.setAdapter(adapter);

        // Setup Drawer
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle(R.string.category);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            // on first time display view for first nav item
            displayView(0);
        }

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
    }

    private void setBanner(String response){
        ArrayList<RequestResponse> mBannerUrl = null;
        try {
            mBannerUrl = ECommerceApplication.mapper.readValue(
                    response,
                    ECommerceApplication.mapper.getTypeFactory().constructCollectionType(
                            ArrayList.class, RequestResponse.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
        mCustomPagerAdapter = new CustomPagerAdapter(this, mBannerUrl);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mCustomPagerAdapter);
        mViewPager.setOnPageChangeListener(new OnPageChangeListenerForInfiniteIndicator(this, mBannerUrl, mViewPager.getCurrentItem()));
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ecomm, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * Slide menu item click listener
     * */
    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }

    private void displayView(int position){
        if(ECommerceApplication.mAppProgressDialog!=null && ECommerceApplication.mAppProgressDialog.isShowing()){
            ECommerceApplication.mAppProgressDialog.dismiss();
        }
        if(position==0){

        }else{
            mNavDrawerItems.get(position).getId();
        }
    }

}
