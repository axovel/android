package com.axovel.ecommerce.sweetschoice.util;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.axovel.ecommerce.sweetschoice.ECommerceApplication;
import com.axovel.ecommerce.sweetschoice.R;
import com.axovel.ecommerce.sweetschoice.model.ECommRequest;
import com.axovel.ecommerce.sweetschoice.model.RequestResponse;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by Umesh Chauhan on 04-01-2016.
 * Axovel Private Limited
 */
public class ServerRequest {

    private Handler mHandler;
    private Activity act = null;
    /**
     *
     */
    public ServerRequest(Activity act, Handler mHandler) {
        if (mHandler != null) {
            this.mHandler = mHandler;
        }
        this.act = act;
    }

    public void sendRequest(RequestResponse params, String url, final String requestName, String requestTag) {
        // Making JSON String
        ECommRequest mECommRequest = new ECommRequest();
        mECommRequest.setTimestamp(String.valueOf(new Date().getTime() / 1000));
        mECommRequest.setParameter(params);
        String requestString = null;
        try {
            requestString = ECommerceApplication.mapper.writeValueAsString(mECommRequest);
        }catch (Exception e){
            e.printStackTrace();
        }
        Log.i("Request String:","Time: "+mECommRequest.getTimestamp()+" Request: "+requestString);
        JsonRequest<String> mRequest = new JsonRequest<String>(Request.Method.POST, url, requestString, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("Response-Server", "Request Name: " + requestName);
                Log.i("Response-Server", "Response: " + response);
                if (requestName.equals("checkVersion")) {
                    // TODO SAME
                    Message msg = new Message();
                    msg.obj = response;
                    mHandler.sendMessage(msg);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String str = new String(response.data);
                Log.i("Response Network Parser", "" + str);
                return Response.success(str,
                        HttpHeaderParser.parseIgnoreCacheHeaders(response));
            }
            public java.util.Map<String, String> getHeaders()
                    throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            };
        };
        final int MY_SOCKET_TIMEOUT_MS = 5000;
        final int MY_MAX_RETRIES = 3;
        mRequest.setRetryPolicy(new DefaultRetryPolicy(MY_SOCKET_TIMEOUT_MS,
                MY_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if (General.isOnline(act)) {
            // Adding request to request queue
            mRequest.setShouldCache(true);
            ECommerceApplication.getInstance().addToRequestQueue(mRequest,
                    requestTag);
        } else {
            Toast.makeText(act, R.string.no_internet, Toast.LENGTH_SHORT)
                    .show();
        }
    }
}
