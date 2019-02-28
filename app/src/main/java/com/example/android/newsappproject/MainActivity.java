package com.example.android.newsappproject;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<News>> {

    private TextView emptyStateTextView;
    private NewsAdapter adapter;
    private ProgressBar spinner;
    private static final int NEWS_LOADER_ID = 1;

    //URL for news data from the guardian open platform.
    private static final String GUARDIAN_REQUEST_URL = "https://content.guardianapis.com/search";

    /**
     * Please put your API key here.
     **/
    private static final String API_KEY = BuildConfig.THE_GUARDIAN_API_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set up the spinner to show that data are being loaded, then hide it temporarily.
        spinner = (ProgressBar) findViewById(R.id.loading_spinner);
        spinner.setVisibility(View.GONE);

        //Set up the empty TextView that is displayed when the list is empty.
        emptyStateTextView = (TextView) findViewById(R.id.empty_view);

        /* Check if the device is connected to the internet.
         * If there is no connection, set the empty TextView to display the message "No internet
         * connection" .
         * If the device has an internet connection, set up the ListView and its adapter,
         * to prepare to receive the news data.
         */
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (isConnected == false) {
            emptyStateTextView.setText(R.string.no_internet_connection);
        } else {
            ListView newsListView = (ListView) findViewById(R.id.list);
            newsListView.setEmptyView(emptyStateTextView);
            adapter = new NewsAdapter(this, new ArrayList<News>());
            newsListView.setAdapter(adapter);

            /* Make the list of news article clickable.
             * When the user clicks on the item, it uses an intent to open the article in the
             * user's browser.
             */
            newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position,
                                        long l) {
                    News currentNews = adapter.getItem(position);
                    Uri newsUri = Uri.parse(currentNews.getUrl());
                    Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);
                    startActivity(websiteIntent);
                }
            });
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            /* Initialize the loader. Pass in the int ID constant defined above and pass in null
             *  for the bundle. Pass in this activity for the LoaderCallbacks parameter.
             */
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);

            //Make the spinner appear to show the user the data are loading.
            spinner.setVisibility(View.VISIBLE);
        }
    }

    // Create a new loader for the given URL
    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        // Get the String values for the preference menu.
        String orderBy = sharedPrefs.getString(getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default));

        String articleNumber =
                sharedPrefs.getString(getString(R.string.settings_article_number_key),
                        getString(R.string.settings_article_number_default));

        String searchContent = sharedPrefs.getString(getString(R.string.settings_edit_text_key),
                getString(R.string.settings_edit_text_default));
        //If the topic preference is left empty, the default topic is Android.
        if (searchContent.replaceAll(" ", "").isEmpty()) {
            searchContent = getString(R.string.settings_edit_text_default);
        }
        ;

        // Parse the URL for the news data, to prepare for the URI builder method.
        Uri baseUri = Uri.parse(GUARDIAN_REQUEST_URL);

        // Use the URI builder method to add parameters to the URL.
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("q", searchContent);
        uriBuilder.appendQueryParameter("tag", "technology/technology");
        uriBuilder.appendQueryParameter("order-by", orderBy);
        uriBuilder.appendQueryParameter("page-size", articleNumber);
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("api-key", API_KEY);

        // Return the final URL and load the information required from it.
        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> newsItem) {
        //Clear the adapter of previous data.
        adapter.clear();

        /* Set empty TextView to display "No news found.". It will be covered by the news data if
         * they exist. If there is no news data, it will stay visible.
         */
        emptyStateTextView.setText(R.string.no_news);

        /* If there is a valid list of {@link News}, then add them to the adapter's
         * data set. This will trigger the ListView to update.
         */
        if (newsItem != null && !newsItem.isEmpty()) {
            adapter.addAll(newsItem);
        }
        //Make the spinner disappear because the loading is finished.
        spinner.setVisibility(View.GONE);
    }

    // Reset loader, clear out the existing data.
    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        adapter.clear();
    }

    @Override
    // This method initialize the contents of the Activity's options menu.
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the Options Menu we specified in XML
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    // This method is called whenever an item in the options menu is selected.
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}