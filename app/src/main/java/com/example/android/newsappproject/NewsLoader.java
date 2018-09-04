package com.example.android.newsappproject;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Loads a list of news article by using an AsyncTaskLoader to perform the
 * network request to the given URL.
 */
public class NewsLoader extends AsyncTaskLoader<List<News>> {

    //Query URL
    private String url;

    /**
     * Constructs a new {@link NewsLoader}.
     *
     * @param context of the activity
     * @param url     to load data from
     */
    public NewsLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    //Loading on background thread.
    @Override
    public List<News> loadInBackground() {
        if (url == null) {
            return null;
        }
        List<News> result = QueryUtils.fetchNewsData(url);
        return result;
    }
}

