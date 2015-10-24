package com.ghettoapps.nce;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements PresenterCallbacks, OnQueryTextListener {

    private static final String TAG = "SearchActivity";
    private SearchPresenter mPresenter = null;
    private SearchListAdapter mListAdapter = null;
    private List<SearchResult> mSearchResults = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        setStrictMode();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Activity owns Presenter
        mPresenter = new Presenter(this, this);

        // Initialize Empty result list
        // TODO should we abstract list data and adapter usage from activity?
        mSearchResults = new ArrayList<>();
        mListAdapter = new SearchListAdapter(this, R.layout.search_list_item, mSearchResults);
        ListView searchListView = (ListView) findViewById(R.id.search_list_view);
        searchListView.setAdapter(mListAdapter);
    }

    private void setStrictMode() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectNetwork()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedClosableObjects()
                .build());
    }

    @Override
    protected void onPause () {
        super.onPause();
        Log.d(TAG, "onPause");
        mPresenter.pause();
    }

    @Override
    protected void onResume () {
        super.onResume();
        Log.d(TAG, "onResume");
        mPresenter.resume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu");
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        // Do not iconify the widget; expand it by default
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.d(TAG, "onQueryTextSubmit");
        Log.d(TAG, query);
        // Do nothing
        return true;
    }

    /**
     * Called when the query text is changed by the user.
     *
     * @param newText the new content of the query text field.
     * @return false if the SearchView should perform the default action of showing any
     * suggestions if available, true if the action was handled by the listener.
     */
    @Override
    public boolean onQueryTextChange(String newText) {
        Log.d(TAG, "onQueryTextChange");
        Log.d(TAG, newText);
        mPresenter.query(newText);
        return true;
    }

    @Override
    public void onResult(List<SearchResult> results) {
        // It is activity's responsibility to show the result list
        Log.d(TAG, "onResult " + results.size());
        mListAdapter.clear();
        mSearchResults.addAll(results);
        mListAdapter.notifyDataSetChanged();
    }

}
