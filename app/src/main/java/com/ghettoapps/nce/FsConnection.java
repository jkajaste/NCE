package com.ghettoapps.nce;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.text.format.DateFormat;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;

/**
 * Created by kajajuh on 18.10.2015.
 */
public class FsConnection {

    private static final String TAG = "FsConnection";
    private final FsResultListener mListener;
    private final ConnectivityManager mConnMgr;
    private final Calendar mCalendar;

    FsConnection(FsResultListener listener, Context context) {
        mListener = listener;
        mConnMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        mCalendar = Calendar.getInstance();
    }

    public void query(String query, String lat, String lon) {
        Log.d(TAG, "query ");
        query = encodeUrl(query);
        // Query string may be null or there is no connection, just skip.
        if(query != null && isConnected()) {
            new FsQueryTask().execute("https://api.foursquare.com/v2/venues/explore" +
                    "?query=" + query +
                    "&ll=" + lat + "," + lon +
                    "&v=" + DateFormat.format("yyyyMMdd", mCalendar) +
                    "&client_id=" + Credential.ID +
                    "&client_secret=" + Credential.SECRET);
            Log.d(TAG, "FsQueryTask started");
        }
    }

    private class FsQueryTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... queries) {
            Log.d(TAG, "FsQueryTask: doInBackground");
            try {
                return downloadUrl(queries[0]);
            } catch (IOException e) {
                return "Unable to retrieve data. URL may be invalid.";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d(TAG, "FsQueryTask: onPostExecute");
            mListener.onFsResult(result);
        }
    }

    /**
     * Must be called from background thread
     */
    private String downloadUrl(final String url) throws IOException {
        Log.d(TAG, "downloadUrl");
        Log.d(TAG, url);
        InputStream is = null;
        // TODO separate download from InputStream to String conversion
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            int responseCode = conn.getResponseCode();
            Log.d(TAG, "Response code: " + responseCode);
            is = conn.getInputStream();
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            StringBuilder responseStrBuilder = new StringBuilder();
            String inputStr;
            while ((inputStr = streamReader.readLine()) != null)
                responseStrBuilder.append(inputStr);
            return responseStrBuilder.toString();
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    private boolean isConnected() {
        NetworkInfo networkInfo = mConnMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private String encodeUrl(String text) {
        try {
            return URLEncoder.encode(text, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

}
