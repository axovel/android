package com.axovel.ecommerce;

import android.app.Application;
import android.app.ProgressDialog;
import android.text.TextUtils;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.axovel.ecommerce.util.General;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * Created by Umesh Chauhan on 24-12-2015.
 * Axovel Private Limited
 */
public class ECommerceApplication extends Application{

    public static ObjectMapper mapper = new ObjectMapper();
    public static General mGeneral = new General();
    public static final String TAG = ECommerceApplication.class
            .getSimpleName();
    // Network Request Queue
    private RequestQueue mRequestQueue;
    private static ECommerceApplication mInstance;
    public static ProgressDialog mAppProgressDialog;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized ECommerceApplication getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024 * 25); // 25 MB cap
            Network network = new BasicNetwork(new HurlStack());
            mRequestQueue = new RequestQueue(cache, network);
            mRequestQueue.start();
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
