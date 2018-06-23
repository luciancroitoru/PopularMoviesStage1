package com.example.lucia.popularmoviesstage1;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class MovieLoader extends AsyncTaskLoader<List<Movie>> {
    private String murl;

    public MovieLoader(Context context, String url) {
        super(context);
        murl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Movie> loadInBackground() {
        if (murl == null) return null;
        return JsonUtils.fetchMoviesData(murl);
    }
}