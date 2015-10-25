package com.ghettoapps.nce;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kajajuh on 17.10.2015.
 */
public class Presenter implements SearchPresenter, ModelCallbacks {

    private static final String TAG = "Presenter";
    private SearchModel mModel = null;
    private PresenterCallbacks mView = null;

    Presenter(PresenterCallbacks view, Context context) {
        Log.d(TAG, "Presenter");
        mView = view;
        mModel = new Model(this, context);
    }

    @Override
    public void query(String query) {
        // TODO skip model queries until pause in typing
        Log.d(TAG, "query(" + query + ")");
        query = query.trim();
        if (!query.equals("")) {
            mModel.query(query);
        } else {
            Log.e(TAG, "Empty string");
        }
    }

    @Override
    public void pause() {
        Log.d(TAG, "pause");
        mModel.stop();
    }

    @Override
    public void resume() {
        Log.d(TAG, "resume");
        mModel.start();
    }

    @Override
    public void onQueryResult(final String result) {
        Log.d(TAG, "onQueryResult");
        Log.d(TAG, result);
        try {
            mView.onResult(getResultList(result));
        } catch (JSONException je) {
            // View keeps showing previous results and
            // informs user about a problem.
            // Any problem with server response causes the same
            // error message for now.
            Log.e(TAG, "Response could not be parsed", je);
            mView.onError("Bad response from service");
        }
    }

    @Override
    public void onQueryError(final String error) {
        Log.d(TAG, "onQueryError");
        Log.d(TAG, error);
        mView.onError(error);
    }

    /**
     * Return empty list if no results
     * Throw exception if response cannot be parsed, like in case of
     * non-success response type.
     *
     * This is a parser for JSON data. Not sure if it belongs to Presenter or Model.
     * */
    private List<SearchResult> getResultList(String dataString) throws JSONException {
        // TODO this needs refactoring into smaller testable pieces
        JSONObject data = new JSONObject(dataString);
        JSONObject response = data.getJSONObject("response");
        JSONArray groups = response.getJSONArray("groups");
        List<SearchResult> results = new ArrayList<>();
        for (int h = 0; h < groups.length(); h++) {
            JSONObject group = groups.getJSONObject(h);
            JSONArray items = group.getJSONArray("items");
            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                JSONObject venue = item.getJSONObject("venue");
                String name = venue.getString("name");
                JSONObject location = venue.getJSONObject("location");
                String address = null;
                try {
                    address = location.getString("address");
                }
                catch (JSONException je) {
                    Log.d(TAG, "No address for " + name);
                }
                String distance = location.getString("distance") + "m";
                results.add(new SearchResult(name, address, distance));
                Log.d(TAG, "venue: " + name);
            }
        }
        return results;
    }

}
