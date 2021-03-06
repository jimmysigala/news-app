package com.example.android.news_app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jimmy on 3/30/2017.
 */

public class QueryUtils {

    public static final String LOG_TAG = QueryUtils.class.getName();

    private QueryUtils() {
    }

    // Return a list of articles from parsing a JSON response
    public static List<Article> extractArticle(String... REQUEST_URL) {
        URL url = createURL(REQUEST_URL[0]);

        // HTTP request to the URL
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Extract fields from JSON response
        List<Article> articles = extractFeatureFromJson(jsonResponse);
        return articles;
    }

    private static URL createURL(String stringUrl) {

        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

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

            // If request was successful (200) then read the input stream and parse the response
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the JSON result", e);
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

    private static String readFromStream(InputStream inputStream) throws IOException {

        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<Article> extractFeatureFromJson(String articlesJSON) {
        if (TextUtils.isEmpty(articlesJSON)) {
            return null;
        }

        List<Article> articles = new ArrayList<>();

        // Extract values from JSON and add to articles list
        try {
            JSONObject root = new JSONObject(articlesJSON);
            JSONObject response = root.optJSONObject("response");
            JSONArray itemsArray = response.optJSONArray("results");

            if (itemsArray == null) {
                return null;
            } else {

                for (int i = 0; i < itemsArray.length(); i++) {
                    JSONObject currentArticle = itemsArray.getJSONObject(i);
                    JSONObject fields = currentArticle.optJSONObject("fields");

                    if (currentArticle.has("sectionName") && currentArticle.has("webPublicationDate")
                            && currentArticle.has("webTitle") && currentArticle.has("webUrl")) {
                        String sectionName = currentArticle.getString("sectionName");
                        String webPubDate = currentArticle.getString("webPublicationDate");
                        String webTitle = currentArticle.getString("webTitle");
                        String webUrl = currentArticle.getString("webUrl");
                        
                        Bitmap thumbnail = null;
                        if (fields != null){
                            String thumbnailString = fields.getString("thumbnail");
                            thumbnail = getBitmap(thumbnailString);
                        }
                        articles.add(new Article(sectionName, webTitle, webPubDate, webUrl, thumbnail ));
                    }
                }
            }
        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the JSON results", e);
        }
        return articles;
    }

    // This method returns a bitmap created from the JSON string under "thumbnail"
    private static Bitmap getBitmap(String thumbnailString) {

        try {
            URL url = new URL(thumbnailString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            InputStream input = urlConnection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            return null;
        }
    }
}
