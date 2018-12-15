package com.example.android.newsapptest;

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
import java.util.Date;
import java.util.List;

public class QueryUtils {

    /**
     * Tag for the log messages
     */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
        //Empty Constructor, so no one can make the object of {@link QueryUtils}
    }

    /**
     * Query the Guardian News and return a list of {@link News} objects.
     */
    public static List<News> fetchNewsData(String requestUrl) {
        // URL object
        URL url = createUrl(requestUrl);

        // HTTP Request to URL and get JSON response
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link News}items
        List<News> newsList = extractFeatureFromJson(jsonResponse);

        // Returning list of {@link News}items
        return newsList;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem for building URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and returning string as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If URL is null, return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200), read Input Stream and parse
            if (urlConnection.getResponseCode() == 200) {
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
                // Closing the input stream may throw an IOException
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream,
                    Charset.forName("UTF-8"));
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
        // If the JSON null, return early.
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        List<News> newsArrayList = new ArrayList<>();
        try {
            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(newsJSON);
            //Create object for response
            JSONObject jsonObjectResponse = baseJsonResponse.getJSONObject("response");
            //JSONArray
            JSONArray newsArray = jsonObjectResponse.getJSONArray("results");

            // For each news Result in the newsArray, create an {@link News} object
            for (int i = 0; i < newsArray.length(); i++) {
                // Get a single news at position i within the list of result
                JSONObject currentNews = newsArray.getJSONObject(i);

                // Extract the value for the key called "webTitle"
                String headline = currentNews.optString("webTitle");
                // Extract the value for the key called "sectionName"
                String type = currentNews.optString("sectionName");
                // Extract the value for the key called "webPublicationDate"
                String date = currentNews.optString("webPublicationDate");
                // Extract the value for the key called "webUrl"
                String url = currentNews.optString("webUrl");
                // Extract the value for the key called "tags" (JSONArray)
                JSONArray arrayTags = currentNews.getJSONArray("tags");
                String authorName = null;

                //Check if Tag is available or not
                if (arrayTags.length() != 0) {
                    for (int t = 0; t < arrayTags.length(); t++) {
                        JSONObject objectTag = arrayTags.getJSONObject(t);
                        authorName = authorName + objectTag.optString("webTitle");
                    }
                }
                // Create a new {@link News} object with the WebTitle, sectionName,
                // webPublicationDate, and webUrl from the JSON response.
                News newsAdd = new News(headline, type, date, url, authorName);
                // Add the new {@link News} to the list of news.
                newsArrayList.add(newsAdd);
            }
        } catch (JSONException e) {
            //Print a log message
            Log.e("QueryUtils", "Problem parsing the Guardian JSON results", e);
        }
        // Return the list of news
        return newsArrayList;
    }
}
