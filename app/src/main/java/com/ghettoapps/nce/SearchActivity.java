package com.ghettoapps.nce;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mPresenter = new Presenter(this, this);

        // Initialize Empty result list
        // TODO should we abstract list data and adapter usage from activity?
        mSearchResults = new ArrayList<>();
        mListAdapter = new SearchListAdapter(this, R.layout.search_list_item, mSearchResults);
        ListView searchListView = (ListView) findViewById(R.id.search_list_view);
        searchListView.setAdapter(mListAdapter);
    }

    @Override
    protected void onNewIntent (Intent intent) {
        Log.d(TAG, "onNewIntent");
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

    /**
     * Called when the user submits the query. This could be due to a key press on the
     * keyboard or due to pressing a submit button.
     * The listener can override the standard behavior by returning true
     * to indicate that it has handled the submit request. Otherwise return false to
     * let the SearchView handle the submission by launching any associated intent.
     *
     * @param query the query text that is to be submitted
     * @return true if the query has been handled by the listener, false to let the
     * SearchView perform the default action.
     */
    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.d(TAG, "onQueryTextSubmit");
        Log.d(TAG, query);
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
