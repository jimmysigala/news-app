package com.example.android.news_app;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by Jimmy on 3/30/2017.
 */

public class ArticleLoader extends AsyncTaskLoader<List<Article>> {

    private String eURL;

    public ArticleLoader(Context context, String url) {
        super(context);
        eURL = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Article> loadInBackground() {
        if (eURL == null) {
            return null;
        }
        List<Article> result = QueryUtils.extractArticle(eURL);
        return result;
    }


}
