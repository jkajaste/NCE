package com.ghettoapps.nce;

import java.util.List;

/**
 * Created by kajajuh on 18.10.2015.
 */
public interface PresenterCallbacks {
    void onResult(final List<SearchResult> results);
    void onError(final String error);
}
