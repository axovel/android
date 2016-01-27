package com.axovel.schoolbus.veiw.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.axovel.schoolbus.R;
import com.axovel.schoolbus.model.RequestResponse;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.LogRecord;

public class Parent extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker locationMarker = null;
    private Polyline mPolyLine = null;
    private List<LatLng> points;
    private AppCompatActivity act = null;
    private int backButtonCount=0;
    private String busID = "1";
    Handler mParentRequest;
    TimerTask t;
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent);
        act = this;
        mParentRequest = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message inputMessage) {
                String response = (String) inputMessage.obj;
                if(response!=null && response!=""){
                    try {
                        JSONArray mResponse = new JSONArray(response);
                        setPoints(mResponse);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        timer = new Timer();
        startRequest();
        timer.schedule(t, 5000, 5000);

    }

    private void startRequest(){
        t = new TimerTask() {
            @Override
            public void run() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        serverRequest(mParentRequest);
                    }
                }).start();

            }
        };
        t.run();
    }

    @Override
     public void onResume(){
        super.onResume();
        //Start the timer, if it's not null
        if (timer != null) {
            timer.schedule(t, 0, 5000);
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if (timer != null) {
            timer.purge();
            timer=null;
        }
    }

    private void setPoints(JSONArray responseJSON){
        points = new ArrayList<LatLng>();
        for(int i=0;i<responseJSON.length();i++){
            try {
                points.add(new LatLng(Double.valueOf(responseJSON.getJSONObject(i).getString("ln")),Double.valueOf(responseJSON.getJSONObject(i).getString("lt"))));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        mPolyLine = mMap.addPolyline(new PolylineOptions().width(5).color(Color.RED));
        mPolyLine.setPoints(points);
        int size = points.size();
        locationMarker = mMap.addMarker(new MarkerOptions().position(points.get(size-1)).title("Last Location of BUS"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(points.get(size-1), 16.0f));
    }

    private void serverRequest(final Handler mParentRequest){
        JSONObject params = new JSONObject();
        try {
            params.put("bus_id", busID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String paramString = params.toString();
        Log.i("Params", paramString);
        // Request
        String url="http://192.168.1.96:3001/parent"; // Local Test
        URL object= null;
        try {
            object = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) object.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        con.setDoOutput(true);
        con.setDoInput(true);
        con.setRequestProperty("Accept", "application/json");
        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        try {
            con.setRequestMethod("POST");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }

        try {
            OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
            wr.write(paramString);
            wr.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Response
        StringBuilder sb = new StringBuilder();
        int HttpResult = 0;
        try {
            HttpResult = con.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(HttpResult == HttpURLConnection.HTTP_OK){
            BufferedReader br = null;
            try {
                br = new BufferedReader(new InputStreamReader(con.getInputStream(),"utf-8"));
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.i("Response",""+sb.toString());
            Message m = new Message();
            m.obj = sb.toString();
            mParentRequest.sendMessage(m);
        }else{
            try {
                System.out.println(con.getResponseMessage());
            } catch (IOException e) {
                e.printStackTrace();
            }
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
}
