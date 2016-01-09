package com.axovel.ecommerce.sweetschoice.view.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.axovel.ecommerce.sweetschoice.ECommerceApplication;
import com.axovel.ecommerce.sweetschoice.R;
import com.axovel.ecommerce.sweetschoice.adapter.CustomPagerAdapter;
import com.axovel.ecommerce.sweetschoice.adapter.RecyclerAdapter;
import com.axovel.ecommerce.sweetschoice.holder.NavDrawerItemHolder;
import com.axovel.ecommerce.sweetschoice.model.NavDrawer;
import com.axovel.ecommerce.sweetschoice.model.RequestResponse;
import com.axovel.ecommerce.sweetschoice.util.OnPageChangeListenerForInfiniteIndicator;
import com.axovel.ecommerce.sweetschoice.util.ServerRequest;
import com.fasterxml.jackson.core.JsonParser;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Umesh Chauhan on 28-12-2015.
 * Axovel Private Limited
 */
public class Activity_Ecomm_Base extends AppCompatActivity {

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    // used to store app title
    private CharSequence mTitle;
    // nav drawer title
    private CharSequence mDrawerTitle;
    private ArrayList<NavDrawer> mNavDrawerItems;
    private Bundle savedInstanceState;
    CustomPagerAdapter mCustomPagerAdapter;
    ViewPager mViewPager;
    private int backButtonCount = 0;
    ViewGroup containerView;
    private AndroidTreeView tView;
    static ServerRequest mServerRequest = null;
    Handler mHandlerECommBase = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        setContentView(R.layout.activity_ecomm_base);
        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        containerView = (ViewGroup) findViewById(R.id.navDrawerContainer);
        // Handler to receive response
        mHandlerECommBase = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message inputMessage) {
                String response = (String) inputMessage.obj;
                // To Distinguish Different Requests
                int type = inputMessage.what;

            }
        };
        mServerRequest = new ServerRequest(this, mHandlerECommBase);
        //
        /*setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
        }*/

        // CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        // collapsingToolbar.setTitle(getString(R.string.app_name));
        //
        ECommerceApplication.mAppProgressDialog = new ProgressDialog(this);
        ECommerceApplication.mAppProgressDialog.show();
        // Test
        setNavDrawerItems(null);
        setCategory(null);
        setBanner(null);
        //

        // Network Request get Nav Drawer Item


        // Setting Banner


    }

    private void setNavDrawerItems(String response) {
        // Testing
        response = "[{\"category\":\"A\",\"subCategory\":[{\"subCategoryName\":\"A-1\"},{\"subCategoryName\":\"A-2\"},{\"subCategoryName\":\"A-3\",\"subCategory\":[{\"subCategoryName\":\"A-3-1\"},{\"subCategoryName\":\"A-3-2\",\"subCategory\":[{\"subCategoryName\":\"A-3-2-1\"},{\"subCategoryName\":\"A-3-2-2\",\"subCategory\":[{\"subCategoryName\":\"A-3-2-2-1\"},{\"subCategoryName\":\"A-3-2-2-2\"}]}]}]}]},\n" +
                "{\"category\":\"A\",\"subCategory\":[{\"subCategoryName\":\"A-1\"},{\"subCategoryName\":\"A-2\"},{\"subCategoryName\":\"A-3\",\"subCategory\":[{\"subCategoryName\":\"A-3-1\"},{\"subCategoryName\":\"A-3-2\",\"subCategory\":[{\"subCategoryName\":\"A-3-2-1\"},{\"subCategoryName\":\"A-3-2-2\"}]}]}]},\n" +
                "{\"category\":\"A\",\"subCategory\":[{\"subCategoryName\":\"A-1\"},{\"subCategoryName\":\"A-2\"},{\"subCategoryName\":\"A-3\",\"subCategory\":[{\"subCategoryName\":\"A-3-1\"},{\"subCategoryName\":\"A-3-2\",\"subCategory\":[{\"subCategoryName\":\"A-3-2-1\"},{\"subCategoryName\":\"A-3-2-2\"}]}]}]},\n" +
                "{\"category\":\"A\",\"subCategory\":[{\"subCategoryName\":\"A-1\"},{\"subCategoryName\":\"A-2\"},{\"subCategoryName\":\"A-3\",\"subCategory\":[{\"subCategoryName\":\"A-3-1\"},{\"subCategoryName\":\"A-3-2\",\"subCategory\":[{\"subCategoryName\":\"A-3-2-1\"},{\"subCategoryName\":\"A-3-2-2\"}]}]}]},\n" +
                "{\"category\":\"A\",\"subCategory\":[{\"subCategoryName\":\"A-1\"},{\"subCategoryName\":\"A-2\"},{\"subCategoryName\":\"A-3\",\"subCategory\":[{\"subCategoryName\":\"A-3-1\"},{\"subCategoryName\":\"A-3-2\",\"subCategory\":[{\"subCategoryName\":\"A-3-2-1\"},{\"subCategoryName\":\"A-3-2-2\"}]}]}]},\n" +
                "{\"category\":\"A\",\"subCategory\":[{\"subCategoryName\":\"A-1\"},{\"subCategoryName\":\"A-2\"},{\"subCategoryName\":\"A-3\",\"subCategory\":[{\"subCategoryName\":\"A-3-1\"},{\"subCategoryName\":\"A-3-2\",\"subCategory\":[{\"subCategoryName\":\"A-3-2-1\"},{\"subCategoryName\":\"A-3-2-2\"}]}]}]},\n" +
                "{\"category\":\"A\",\"subCategory\":[{\"subCategoryName\":\"A-1\"},{\"subCategoryName\":\"A-2\"},{\"subCategoryName\":\"A-3\",\"subCategory\":[{\"subCategoryName\":\"A-3-1\"},{\"subCategoryName\":\"A-3-2\",\"subCategory\":[{\"subCategoryName\":\"A-3-2-1\"},{\"subCategoryName\":\"A-3-2-2\"}]}]}]},\n" +
                "{\"category\":\"A\",\"subCategory\":[{\"subCategoryName\":\"A-1\"},{\"subCategoryName\":\"A-2\"},{\"subCategoryName\":\"A-3\",\"subCategory\":[{\"subCategoryName\":\"A-3-1\"},{\"subCategoryName\":\"A-3-2\",\"subCategory\":[{\"subCategoryName\":\"A-3-2-1\"},{\"subCategoryName\":\"A-3-2-2\"}]}]}]},\n" +
                "{\"category\":\"A\",\"subCategory\":[{\"subCategoryName\":\"A-1\"},{\"subCategoryName\":\"A-2\"},{\"subCategoryName\":\"A-3\",\"subCategory\":[{\"subCategoryName\":\"A-3-1\"},{\"subCategoryName\":\"A-3-2\",\"subCategory\":[{\"subCategoryName\":\"A-3-2-1\"},{\"subCategoryName\":\"A-3-2-2\"}]}]}]}]";
        ECommerceApplication.mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        //
        try {
            mNavDrawerItems = ECommerceApplication.mapper.readValue(
                    response,
                    ECommerceApplication.mapper.getTypeFactory().constructCollectionType(
                            ArrayList.class, NavDrawer.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
        TreeNode root = TreeNode.root();
        for (int i = 0; i < mNavDrawerItems.size(); i++) {
            TreeNode rootElement = new TreeNode(new NavDrawerItemHolder.IconTreeItem(null, mNavDrawerItems.get(i).getCategory()));
            if (mNavDrawerItems.get(i).getSubCategory() != null && !mNavDrawerItems.get(i).getSubCategory().isEmpty()) {
                ArrayList<NavDrawer> subCategory = mNavDrawerItems.get(i).getSubCategory();
                ArrayList<TreeNode> mSubCategoryTree = fetchSubCategory(subCategory);
                for (int j = 0; j < mSubCategoryTree.size(); j++) {
                    rootElement.addChild(mSubCategoryTree.get(j));
                }
                root.addChild(rootElement);
            } else {
                root.addChild(rootElement);
            }
        }

        tView = new AndroidTreeView(this, root);
        tView.setDefaultAnimation(true);
        tView.setDefaultContainerStyle(R.style.TreeNodeStyleCustom);
        tView.setDefaultViewHolder(NavDrawerItemHolder.class);
        tView.setDefaultNodeClickListener(nodeClickListener);
        tView.setDefaultNodeLongClickListener(nodeLongClickListener);

        containerView.addView(tView.getView());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        // Setup Drawer
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(R.string.category);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            // on first time display view for first nav item
            displayView(0);
        } else {
            String state = savedInstanceState.getString("tState");
            if (!TextUtils.isEmpty(state)) {
                tView.restoreState(state);
            }
        }
    }

    private TreeNode.TreeNodeClickListener nodeClickListener = new TreeNode.TreeNodeClickListener() {
        @Override
        public void onClick(TreeNode node, Object value) {
            NavDrawerItemHolder.IconTreeItem item = (NavDrawerItemHolder.IconTreeItem) value;

        }
    };

    private TreeNode.TreeNodeLongClickListener nodeLongClickListener = new TreeNode.TreeNodeLongClickListener() {
        @Override
        public boolean onLongClick(TreeNode node, Object value) {
            NavDrawerItemHolder.IconTreeItem item = (NavDrawerItemHolder.IconTreeItem) value;
            return true;
        }
    };

    private ArrayList<TreeNode> fetchSubCategory(ArrayList<NavDrawer> subCategory) {
        /** Stores SudTrees **/
        ArrayList<TreeNode> subCategoryTree = new ArrayList<TreeNode>();
        /** Stores Current Tree **/
        TreeNode rootElement;
        for (int i = 0; i < subCategory.size(); i++) {
            rootElement = new TreeNode(new NavDrawerItemHolder.IconTreeItem(null, subCategory.get(i).getSubCategoryName()));
            if (subCategory.get(i).getSubCategory() != null && !subCategory.get(i).getSubCategory().isEmpty()) {
                ArrayList<NavDrawer> subSubCategory = subCategory.get(i).getSubCategory();
                ArrayList<TreeNode> mfetchSubCategory = fetchSubCategory(subSubCategory);
                for (int j = 0; j < mfetchSubCategory.size(); j++) {
                    rootElement.addChild(mfetchSubCategory.get(j));
                }
                subCategoryTree.add(rootElement);
            } else {
                subCategoryTree.add(rootElement);
            }
        }
        return subCategoryTree;
    }

    private void setBanner(String response) {
        //
        response ="[{},{},{},{}]";
        //
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
        if (id == R.id.action_overflow) {
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
        getSupportActionBar().setTitle(mTitle);
    }

    /**
     * Slide menu item click listener
     */
    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }

    private void displayView(int position) {
        if (ECommerceApplication.mAppProgressDialog != null && ECommerceApplication.mAppProgressDialog.isShowing()) {
            ECommerceApplication.mAppProgressDialog.dismiss();
        }
        if (position == 0) {

        } else {
            // mNavDrawerItems.get(position).getId();
        }
    }

    @Override
    public void onBackPressed() {
        // Handling Back Press to Close App
        if (backButtonCount >= 1) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            this.finish();
        } else {
            Toast.makeText(
                    this,
                    R.string.app_close_confirmation,
                    Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("tState", tView.getSaveState());
    }

    private void setCategory(String response) {
        // Test
        response = "[{\"category\":\"Popular\",\"products\":[{\"productName\":\"ABC\",\"cost\":\"100\",\"shortDesc\":\"ABC product belongs to ABC Corporation\"},{\"productName\":\"ABC\",\"cost\":\"100\",\"shortDesc\":\"ABC product belongs to ABC Corporation\"},{\"productName\":\"ABC\",\"cost\":\"100\",\"shortDesc\":\"ABC product belongs to ABC Corporation\"},{\"productName\":\"ABC\",\"cost\":\"100\",\"shortDesc\":\"ABC product belongs to ABC Corporation\"}]},{\"category\":\"Popular\",\"products\":[{\"productName\":\"ABC\",\"cost\":\"100\",\"shortDesc\":\"ABC product belongs to ABC Corporation\"},{\"productName\":\"ABC\",\"cost\":\"100\",\"shortDesc\":\"ABC product belongs to ABC Corporation\"},{\"productName\":\"ABC\",\"cost\":\"100\",\"shortDesc\":\"ABC product belongs to ABC Corporation\"},{\"productName\":\"ABC\",\"cost\":\"100\",\"shortDesc\":\"ABC product belongs to ABC Corporation\"}]},{\"category\":\"Popular\",\"products\":[{\"productName\":\"ABC\",\"cost\":\"100\",\"shortDesc\":\"ABC product belongs to ABC Corporation\"},{\"productName\":\"ABC\",\"cost\":\"100\",\"shortDesc\":\"ABC product belongs to ABC Corporation\"},{\"productName\":\"ABC\",\"cost\":\"100\",\"shortDesc\":\"ABC product belongs to ABC Corporation\"},{\"productName\":\"ABC\",\"cost\":\"100\",\"shortDesc\":\"ABC product belongs to ABC Corporation\"}]},{\"category\":\"Popular\",\"products\":[{\"productName\":\"ABC\",\"cost\":\"100\",\"shortDesc\":\"ABC product belongs to ABC Corporation\"},{\"productName\":\"ABC\",\"cost\":\"100\",\"shortDesc\":\"ABC product belongs to ABC Corporation\"},{\"productName\":\"ABC\",\"cost\":\"100\",\"shortDesc\":\"ABC product belongs to ABC Corporation\"},{\"productName\":\"ABC\",\"cost\":\"101\",\"shortDesc\":\"ABC product belongs to ABC Corporation\"}]}]";
        //
        ArrayList<RequestResponse> mProductList = null;
        try {
            mProductList= ECommerceApplication.mapper.readValue(
                    response,
                    ECommerceApplication.mapper.getTypeFactory().constructCollectionType(
                            ArrayList.class, RequestResponse.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(mProductList!=null) {
            ViewGroup insertPoint = (ViewGroup) findViewById(R.id.ll_container_home);
            for (int i = 0; i < mProductList.size(); i++) {
                LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = vi.inflate(R.layout.layout_recycler_veiw, null);

                // fill in any details dynamically here
                TextView textView = (TextView) v.findViewById(R.id.txtCategoryHeader);
                textView.setText(mProductList.get(i).getCategory());
                RecyclerView rvProducts = (RecyclerView) v.findViewById(R.id.recycler_view_productlist);
                rvProducts.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                ArrayList<RequestResponse> productList = new ArrayList<RequestResponse>();
                productList = mProductList.get(i).getProducts();
                RecyclerAdapter adapter = new RecyclerAdapter(this, productList);
                rvProducts.setAdapter(adapter);
                // insert into main view
                insertPoint.addView(v, i, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
        }
    }
}
