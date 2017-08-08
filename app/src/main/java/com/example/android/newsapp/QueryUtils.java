package com.example.android.newsapp;

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
 * Created by Ankur Gupta on 7/8/17.
 * guptaankur.gupta0@gmail.com
 */

public class QueryUtils {
    private static final String TAG = QueryUtils.class.getSimpleName();

    private static final String KEY_RESPONSE = "response";
    private static final String KEY_RESULTS = "results";
    private static final String KEY_WEB_TITLE = "webTitle";
    private static final String KEY_WEB_URL = "webUrl";
    private static final String KEY_WEB_DATE = "webPublicationDate";
    private static final String KEY_SECTION_NAME = "sectionName";

    private QueryUtils() {

    }

    /**
     * To query Guardian API
     *
     * @param requestUrl A string url
     * @return List of data type News
     */
    public static List<News> fetchNewsData(String requestUrl) {
        //Create URL object
        URL url = createUrl(requestUrl);

        //Perform HTTP request to the URL
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(TAG, "Problem while HTTP request", e);
        }

        List<News> newsList = extractNews(jsonResponse);

        return newsList;
    }

    /**
     * Method to convert String url to URL object
     *
     * @param stringUrl String
     * @return URL object
     */
    public static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(TAG, "Problem while building URL", e);
        }
        return url;
    }

    /**
     * Make an HTTP request
     *
     * @param url URL object to make HTTP request on
     * @return String JSON response
     * @throws IOException Closing inputStream can generate this exception
     */
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

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(TAG, "Problem retrieving JSON results.", e);
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
     * Covert InputStream to String
     *
     * @param inputStream
     * @return String which contains whole JSON response from server
     * @throws IOException
     */
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

    private static List<News> extractNews(String newsJson) {
        if (TextUtils.isEmpty(newsJson)) {
            return null;
        }

        List<News> newsList = new ArrayList<>();

        try {
            JSONObject baseJsonResponse = new JSONObject(newsJson);
            JSONObject responseObject = baseJsonResponse.getJSONObject(KEY_RESPONSE);
            JSONArray resultsArray = responseObject.getJSONArray(KEY_RESULTS);

            for (int i = 0; i < resultsArray.length(); i++) {

                JSONObject newsObject = resultsArray.getJSONObject(i);
                String section = newsObject.getString(KEY_SECTION_NAME);
                String date = newsObject.getString(KEY_WEB_DATE);
                String title = newsObject.getString(KEY_WEB_TITLE);
                String url = newsObject.getString(KEY_WEB_URL);

                News news = new News(section, date, title, url);
                newsList.add(news);
            }
        } catch (JSONException e) {
            Log.e(TAG, "Problem parsing JSON", e);
        }
        return newsList;
    }
}
