package com.ghettoapps.nce;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;

import java.io.Closeable;
import java.text.DateFormat;
import java.util.Date;

/**
 * Created by kajajuh on 16.10.2015.
 */
public class GmsClient implements Closeable, ConnectionCallbacks, OnConnectionFailedListener, LocationListener {

    private static final String TAG = "GmsClient";
    private GoogleApiClient mGoogleApiClient = null;
    private Location mLastLocation = null;

    private String mLatitudeText = null;
    private String mLongitudeText = null;
    private LocationRequest mLocationRequest = null;
    private boolean mLocating = false;

    public GmsClient(Context context) {
        Log.d(TAG, "GmsClient");
        buildGoogleApiClient(context);
    }

    private synchronized void buildGoogleApiClient(Context context) {
        Log.d(TAG, "buildGoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        // TODO inform owner?
        Log.d(TAG, "onConnected");
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            mLatitudeText = String.valueOf(mLastLocation.getLatitude());
            mLongitudeText = String.valueOf(mLastLocation.getLongitude());
            Log.d(TAG, "Lat: " + mLatitudeText + " Lon: " + mLongitudeText);
        }
        if(mLocating) startLocationUpdates();
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged");
        mLastLocation = location;
        mLatitudeText = String.valueOf(mLastLocation.getLatitude());
        mLongitudeText = String.valueOf(mLastLocation.getLongitude());
        Log.d(TAG, "Timestamp: " + DateFormat.getTimeInstance().format(new Date()) +
                " Lat: " + mLatitudeText +
                " Lon: " + mLongitudeText);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended");
        if(mLocating) stopLocationUpdates();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.d(TAG, "onConnectionFailed");
        if(mLocating) stopLocationUpdates();
    }

    public String getLatitudeText() {
        return mLatitudeText;
    }

    public String getLongitudeText() {
        return mLongitudeText;
    }

    public void start() {
        mGoogleApiClient.connect();
        mLocating = true;
    }

    public void stop() {
        Log.d(TAG, "stop");
        stopInternal();
    }

    @Override
    public void close() {
        Log.d(TAG, "close");
        stopInternal();
    }

    private void stopInternal() {
        Log.d(TAG, "stopInternal");
        mLocating = false;
        stopLocationUpdates();
        mGoogleApiClient.disconnect();
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void startLocationUpdates() {
        if(mLocationRequest == null) createLocationRequest();
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    private void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }
}
