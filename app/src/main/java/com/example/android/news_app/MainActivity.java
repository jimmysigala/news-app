package com.example.android.news_app;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Article>> {

    private ArticleArrayAdapter adapter;
    private View progress;
    private TextView mEmptyStateTextView;
    private String articleUrl = "http://content.guardianapis.com/search?order-by=newest&page-size=30&api-key=test&show-fields=thumbnail";
    private List<Article> mArticles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progress = findViewById(R.id.loading_spinner);
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_text_view);


        final ListView articleListView = (ListView) findViewById(R.id.list);
        adapter = new ArticleArrayAdapter(this, new ArrayList<Article>());
        articleListView.setAdapter(adapter);

        articleListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                if (mArticles != null && !mArticles.isEmpty()) {
                    Intent i = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(mArticles.get(position).getmWebURL()));
                    startActivity(i);

                }
            }
        });

        // Check the state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // Initialize loader and fetch data if there is a network connection, otherwise display error
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {

                        ConnectivityManager connMgr = (ConnectivityManager)
                                getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                        // Initialize loader and fetch data if there is a network connection, otherwise display error
                        if (networkInfo != null && networkInfo.isConnected()) {
                            View loadingIndicator = findViewById(R.id.loading_spinner);
                            loadingIndicator.setVisibility(View.VISIBLE);
                            articleUrl = "http://content.guardianapis.com/search?order-by=relevance&page-size=30&q=" + query + "&api-key=test&show-fields=thumbnail";
                            getLoaderManager().restartLoader(1, null, MainActivity.this);
                        } else {
                            mEmptyStateTextView.setText(R.string.no_connection);
                        }
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        return false;
                    }
                });
        return true;
    }

    @Override
    public Loader<List<Article>> onCreateLoader(int id, Bundle args) {
        return new ArticleLoader(this, articleUrl);
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> articles) {
        mArticles = articles;

        // Hide progress spinner, clear adapter of previous data
        progress.setVisibility(View.GONE);
        adapter.clear();

        // Add data to the adapter if not null, otherwise display error message
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
