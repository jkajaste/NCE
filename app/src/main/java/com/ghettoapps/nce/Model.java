package com.ghettoapps.nce;

import android.content.Context;
import android.util.Log;

import java.io.Closeable;

/**
 * Created by kajajuh on 17.10.2015.
 */
public class Model implements Closeable, SearchModel, FsResultListener {

    private static final String TAG = "Model";
    private GmsClient mGmsClient = null;
    private FsConnection mFsConnection = null;
    private ModelCallbacks mCallbacks = null;

    Model(ModelCallbacks callbacks, Context context){
        Log.d(TAG, "Model");
        mCallbacks = callbacks;
        mGmsClient = new GmsClient(context);
        mFsConnection = new FsConnection(this, context);
    }

    @Override
    public void start() {
        Log.d(TAG, "start");
        mGmsClient.start();
    }

    @Override
    public void stop() {
        Log.d(TAG, "stop");
        mGmsClient.stop();
    }

    @Override
    public void close() {
        Log.d(TAG, "close");
        mGmsClient.close();
    }

    @Override
    public void query(String query) {
        Log.d(TAG, "query(" + query + ")");
        mFsConnection.query(query, mGmsClient.getLatitudeText(), mGmsClient.getLongitudeText());
    }

    @Override
    public void onFsResult(final String result) {
        Log.d(TAG, "onFsResult");
        mCallbacks.onQueryResult(result);
    }

    @Override
    public void onFsError(final String error) {
        Log.d(TAG, "onFsError");
        mCallbacks.onQueryError(error);
    }

}
