package com.axovel.schoolbus.veiw.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.axovel.schoolbus.R;
import com.axovel.schoolbus.util.General;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

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
import java.util.Calendar;
import java.util.List;

public class Dashboard extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;
    private Marker locationMarker = null;
    private GoogleApiClient mGoogleApiClient = null;
    // Request code to use when launching the resolution activity
    private static final int REQUEST_RESOLVE_ERROR = 1001;
    // Unique tag for the error dialog fragment
    private static final String DIALOG_ERROR = "dialog_error";
    // Bool to track whether the app is already resolving an error
    private boolean mResolvingError = false;
    private static final String STATE_RESOLVING_ERROR = "resolving_error";
    // Last Location
    private Location mLastLocation = null;
    private LocationRequest mLocationRequest;
    private FragmentActivity act = null;
    private boolean buttonStatus = true;
    private Polyline mPolyLine = null;
    private List<LatLng> points;
    protected PowerManager.WakeLock mWakeLock;
    private TextView txtDistance;
    private int distance = 0;
    private long mLastUpdateTimeStamp = 0;
    private int backButtonCount=0;
    private long tripStartTime = 0;
    private long tripEndTime = 0;
    private String busID = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        // outState.putBoolean(STATE_RESOLVING_ERROR, mResolvingError);
        mResolvingError = savedInstanceState != null
                && savedInstanceState.getBoolean(STATE_RESOLVING_ERROR, false);
        act=this;
        // Wake Lock
        final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        this.mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
        this.mWakeLock.acquire();
        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .build();
        }
        mGoogleApiClient.connect();

        txtDistance = (TextView) findViewById(R.id.txtDistance);
        final Button btnStrtStop = ((Button) findViewById(R.id.btnStartStop));
        btnStrtStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int sdk = Build.VERSION.SDK_INT;
                if(!buttonStatus){
                    // Stop Trip
                if(sdk < Build.VERSION_CODES.JELLY_BEAN) {
                    v.setBackgroundDrawable( getResources().getDrawable(R.drawable.btn_selector) );
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        v.setBackground( getResources().getDrawable(R.drawable.btn_selector));
                    }else{
                        v.setBackgroundDrawable( getResources().getDrawable(R.drawable.btn_selector) );
                    }
                }
                buttonStatus = true;
                btnStrtStop.setText(R.string.txt_start_trip);
                    // Reset Time
                    tripEndTime = Calendar.getInstance().getTimeInMillis()/1000;
                    // Send End Request with end time to end trip.
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            serverRequest(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()));
                        }
                    }).start();
                }else{
                    // Start Trip
                    if(sdk < Build.VERSION_CODES.JELLY_BEAN) {
                        v.setBackgroundDrawable( getResources().getDrawable(R.drawable.btn_selector_red) );
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            v.setBackground( getResources().getDrawable(R.drawable.btn_selector_red));
                        }else{
                            v.setBackgroundDrawable( getResources().getDrawable(R.drawable.btn_selector_red) );
                        }
                    }
                    // Reset Time
                    tripStartTime = Calendar.getInstance().getTimeInMillis()/1000;
                    tripEndTime = 0;
                    // To remove any old Polyline
                    if(mPolyLine!=null) {
                        mPolyLine.remove();
                    }
                    mPolyLine = mMap.addPolyline(new PolylineOptions().width(5).color(Color.RED));
                    btnStrtStop.setText(R.string.txt_end_trip);
                    buttonStatus = false;
                    points = new ArrayList<LatLng>();
                    distance = 0;
                }

            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Checking Permission status
        int hasFineLoctionPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (hasFineLoctionPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    General.DANGEROUS_APP_PERMISSIONS.MY_PERMISSIONS_REQUEST_FINE_LOCATION.ordinal());
            return;
        }
        //
        int hasCoarseLoctionPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (hasCoarseLoctionPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    General.DANGEROUS_APP_PERMISSIONS.MY_PERMISSIONS_REQUEST_COARSE_LOCATION.ordinal());
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                Toast.makeText(act, "Please wait", Toast.LENGTH_SHORT).show();
                checkGPS();
                return false;
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        if (requestCode == General.DANGEROUS_APP_PERMISSIONS.MY_PERMISSIONS_REQUEST_FINE_LOCATION.ordinal() ||
                requestCode == General.DANGEROUS_APP_PERMISSIONS.MY_PERMISSIONS_REQUEST_COARSE_LOCATION.ordinal() ) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted
                //enable Current location Button
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // showing an explanation if required
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)) {

                        // Show an expanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                            showMessageOKCancel("You need to allow access to Location so that app can work properly.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            requestLocation();
                                        }
                                    });
                            return;
                    } else {
                        // No explanation needed, requesting the permission.
                        requestLocation();
                    }
                } else {
                    mMap.setMyLocationEnabled(true);
                }
            } else {
                // permission denied
                Toast.makeText(this, "Application will not work. Location Permission Denied", Toast.LENGTH_LONG).show();
            }
            return;
        }
        // other 'else if' lines to check for other
        // permissions this app might request
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(Dashboard.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void requestLocation(){
        // Checking Permission status
        int hasFineLoctionPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (hasFineLoctionPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    General.DANGEROUS_APP_PERMISSIONS.MY_PERMISSIONS_REQUEST_FINE_LOCATION.ordinal());
            return;
        }
        //
        int hasCoarseLoctionPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (hasCoarseLoctionPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    General.DANGEROUS_APP_PERMISSIONS.MY_PERMISSIONS_REQUEST_COARSE_LOCATION.ordinal());
            return;
        }
    }

    private void serverRequest(LatLng location){
        JSONObject params = new JSONObject();
        try {
            params.put("bus_id", busID);
            params.put("starttime", tripStartTime);
            params.put("endtime", tripEndTime);
            params.put("ln", location.latitude);
            params.put("lt", location.longitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String paramString = params.toString();
        Log.i("Params", paramString);
        // Request
        String url="http://192.168.1.96:3001/driver"; // Local Test
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
            System.out.println(""+sb.toString());
        }else{
            try {
                System.out.println(con.getResponseMessage());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateLocation(Location mLocationUpdate) {
        final LatLng UPDATE = new LatLng(mLocationUpdate.getLatitude(), mLocationUpdate.getLongitude());
        float speed = 0.0f;
        if(!buttonStatus) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    serverRequest(UPDATE);
                }
            }).start();

            long mCurrentTimeStamp = Calendar.getInstance().getTimeInMillis();
            if(mLastLocation!=null) {
                speed = (mLocationUpdate.distanceTo(mLastLocation)) / (mCurrentTimeStamp - mLastUpdateTimeStamp) / 1000;
                distance += mLocationUpdate.distanceTo(mLastLocation);
            }
            mLastUpdateTimeStamp = mCurrentTimeStamp;
            mLastLocation = mLocationUpdate;
            if(mPolyLine==null){
                points.add(UPDATE);
                mPolyLine.setPoints(points);
            }else{
                points.add(UPDATE);
                mPolyLine.setPoints(points);
            }
        }
        if (locationMarker != null) {
            locationMarker.remove();
        }
        if(distance>1000){
            txtDistance.setText("Approx Distance: "+distance/1000+" KM"+"\nCurrent Speed: "+speed+" M/Sec");
        }else{
            txtDistance.setText("Approx Distance: " + distance + " M"+"\nCurrent Speed: "+speed+" M/Sec");
        }
        locationMarker = mMap.addMarker(new MarkerOptions().position(UPDATE).title("Update Location"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(UPDATE,16.0f));
    }

    @Override
    protected void onDestroy(){
        // Release wake Lock
        this.mWakeLock.release();
        super.onDestroy();
        stopLocationUpdates();
    }

    private void stopLocationUpdates() {
        if(mGoogleApiClient.isConnected())
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    @Override
    public void onConnected(Bundle bundle) {
        // Checking Permission status
        int hasFineLoctionPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (hasFineLoctionPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    General.DANGEROUS_APP_PERMISSIONS.MY_PERMISSIONS_REQUEST_FINE_LOCATION.ordinal());
            return;
        }
        //
        int hasCoarseLoctionPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (hasCoarseLoctionPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    General.DANGEROUS_APP_PERMISSIONS.MY_PERMISSIONS_REQUEST_COARSE_LOCATION.ordinal());
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if(mLastLocation!=null) {
            updateLocation(mLastLocation);
        }
        createLocationRequest();
        startLocationUpdates();
    }

    /** Location Updates **/
    protected void startLocationUpdates() {
        // Checking Permission status
        int hasFineLoctionPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (hasFineLoctionPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    General.DANGEROUS_APP_PERMISSIONS.MY_PERMISSIONS_REQUEST_FINE_LOCATION.ordinal());
            return;
        }
        //
        int hasCoarseLoctionPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (hasCoarseLoctionPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    General.DANGEROUS_APP_PERMISSIONS.MY_PERMISSIONS_REQUEST_COARSE_LOCATION.ordinal());
            return;
        }
        // Checking GPS
        checkGPS();
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    // Check GPS
    private void checkGPS(){
        // Checking GPS
        int off = 0;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                off = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);
            else{
                LocationManager lm = (LocationManager) this
                        .getSystemService(Context.LOCATION_SERVICE);

                if(lm.isProviderEnabled(LocationManager.GPS_PROVIDER))
                    off=1;
            }

        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        if(off==0){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(Dashboard.this);
            // Setting Dialog Title
            alertDialog.setTitle("GPS Disabled");
            // Setting Dialog Message
            alertDialog.setMessage("Please start GPS service");
            // Setting Icon to Dialog
            alertDialog.setIcon(R.mipmap.ic_school_bus);
            // Setting Positive "Yes" Button
            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    Intent onGPS = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(onGPS);
                    dialog.cancel();
                }
            });

            // Setting Negative "NO" Button
            alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            // Showing Alert Message
            alertDialog.show();

        }
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(2000); // 5 sec
        mLocationRequest.setFastestInterval(500); // 2 sec
        mLocationRequest.setSmallestDisplacement(2f); // 2 meter
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onLocationChanged(Location location) {
        updateLocation(location);
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (mResolvingError) {
            // Already attempting to resolve an error.
            return;
        } else if (result.hasResolution()) {
            try {
                mResolvingError = true;
                result.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {
                // There was an error with the resolution intent. Try again.
                mGoogleApiClient.connect();
            }
        } else {
            // Show dialog using GoogleApiAvailability.getErrorDialog()
            showErrorDialog(result.getErrorCode());
            mResolvingError = true;
        }

    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mResolvingError) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
    }

    /* Creates a dialog for an error message */
    private void showErrorDialog(int errorCode) {
        // Create a fragment for the error dialog
        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
        // Pass the error that should be displayed
        Bundle args = new Bundle();
        args.putInt(DIALOG_ERROR, errorCode);
        dialogFragment.setArguments(args);
        dialogFragment.show(getSupportFragmentManager(), "errordialog");
    }

    /* Called from ErrorDialogFragment when the dialog is dismissed. */
    public void onDialogDismissed() {
        mResolvingError = false;
    }

    /* A fragment to display an error dialog */
    public static class ErrorDialogFragment extends DialogFragment {
        public ErrorDialogFragment() { }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Get the error code and retrieve the appropriate dialog
            int errorCode = this.getArguments().getInt(DIALOG_ERROR);
            return GoogleApiAvailability.getInstance().getErrorDialog(
                    this.getActivity(), errorCode, REQUEST_RESOLVE_ERROR);
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            ((Dashboard) getActivity()).onDialogDismissed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_RESOLVE_ERROR) {
            mResolvingError = false;
            if (resultCode == RESULT_OK) {
                // Checking app is not already connected or attempting to connect
                if (!mGoogleApiClient.isConnecting() &&
                        !mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.connect();
                }
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

