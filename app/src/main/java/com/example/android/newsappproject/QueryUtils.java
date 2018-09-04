package com.example.android.newsappproject;

import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods related to requesting and receiving news data from theguardianopenplatform.
 */
public final class QueryUtils extends AppCompatActivity {

    //Tag for the log messages.
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    public static List<News> fetchNewsData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        // Extract relevant fields from the JSON response and create a list of {@link News}
        List<News> newsItem = extractFeatureFromJson(jsonResponse);

        // Return the list of {@link News}
        return newsItem;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            /* If the request was successful (response code 200),
             * then read the input stream and parse the response.
             * When done, close the connection and input stream.
             */
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the news JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset
                    .forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link News} objects that has been built up from
     * parsing the given JSON response.
     */
    private static List<News> extractFeatureFromJson(String newsJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        // Create an empty ArrayList where the news will be added.
        List<News> newsItem = new ArrayList<>();

        // Look for the news data we need using a loop, then add them to ArrayList.
        try {
            JSONObject baseJsonResponse = new JSONObject(newsJSON);
            JSONObject newsResponse = baseJsonResponse.getJSONObject("response");
            JSONArray newsArray = newsResponse.getJSONArray("results");
            for (int i = 0; i < newsArray.length(); i++) {
                JSONObject currentNewsItem = newsArray.getJSONObject(i);
                String section = currentNewsItem.getString("sectionName");
                String title = currentNewsItem.getString("webTitle");
                String time = currentNewsItem.getString("webPublicationDate");
                JSONArray tags = currentNewsItem.getJSONArray("tags");
                JSONObject tagsDetails = tags.getJSONObject(0);
                String author = tagsDetails.getString("webTitle");
                String url = currentNewsItem.getString("webUrl");

                News news = new News(section, title, time, author, url);
                newsItem.add(news);
            }
        } catch (JSONException e) {
            /* If an error is thrown when executing any of the above statements in the "try" block,
             * catch the exception here, so the app doesn't crash.
             * Print a log message with the message from the exception.
             */
            Log.e("QueryUtils", "Problem parsing the news JSON results", e);
        }
        // Return the list of news (to be displayed in the MainActivity ListView).
        return newsItem;
    }
}
