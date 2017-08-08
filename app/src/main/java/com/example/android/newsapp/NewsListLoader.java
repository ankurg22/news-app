package com.example.android.newsapp;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

/**
 * Created by Ankur Gupta on 7/8/17.
 * guptaankur.gupta0@gmail.com
 */

public class NewsListLoader extends AsyncTaskLoader<List<News>> {
    private String mUrl;

    public NewsListLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<News> loadInBackground() {
        return QueryUtils.fetchNewsData(mUrl);
    }
}
