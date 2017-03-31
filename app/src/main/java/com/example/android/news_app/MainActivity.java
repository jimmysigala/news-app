package com.example.android.news_app;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.LoaderManager;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Article>> {

    private ArticleArrayAdapter adapter;
    private View progress;
    private TextView mEmptyStateTextView;
    private String articleUrl = "http://content.guardianapis.com/search?q=lakers&api-key=test";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progress = findViewById(R.id.loading_spinner);
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_text_view);


        ListView articleListView = (ListView) findViewById(R.id.list);
        adapter = new ArticleArrayAdapter(this, new ArrayList<Article>());
        articleListView.setAdapter(adapter);


        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(1, null, this);
        } else {
            View loadingIndicator = findViewById(R.id.loading_spinner);
            loadingIndicator.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_connection);
        }


    }


    @Override
    public Loader<List<Article>> onCreateLoader(int id, Bundle args) {
        return new ArticleLoader(this, articleUrl);
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> articles) {

        progress.setVisibility(View.GONE);
        adapter.clear();

        if (articles != null && !articles.isEmpty()) {
            adapter.addAll(articles);
        } else {
            mEmptyStateTextView.setText(R.string.no_data);
        }


    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
        adapter.clear();

    }
}
