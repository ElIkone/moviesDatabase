package com.example.benja.moviesdatabase;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class Network {
    private final static String baseURL = "https://api.themoviedb.org/3/movie/";
    private final static String paramKey = "api_key";
    private final static String keyValue = "";
    ///Replace KeyValue with your key from the movie Db
    private final static String popular = "popular";
    private final static String rated = "top_rated";

    private final static String TAG = "Network";

    public static URL buildUrl(String type, String id) {
        Uri buildUrl = null;

        switch (type) {
            case "popular":
                buildUrl = Uri.parse(baseURL + popular).buildUpon()
                        .appendQueryParameter(paramKey, keyValue)
                        .build();
                break;
            case "rated":
                buildUrl = Uri.parse(baseURL + rated).buildUpon()
                        .appendQueryParameter(paramKey, keyValue)
                        .build();
                break;
        }
        URL url = null;
        try {
            url = new URL(buildUrl.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    private byte[] getResponseFromHttpUrl(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = urlConnection.getInputStream();

            if (urlConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(urlConnection.getResponseMessage() +
                        ": with" + url.toString());
            }

            int bytesRead;
            byte[] buffer = new byte[1024];

            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();

            return out.toByteArray();
        } finally {
            urlConnection.disconnect();
        }
    }

    public ArrayList<Movie> getPopularMovies() {
        String url = buildUrl("popular", "").toString();
        return downloadGalleryItems(url);
    }

    public ArrayList<Movie> getTopMovies() {
        String url = buildUrl("rated", "").toString();
        return downloadGalleryItems(url);
    }


    private String getUrlString(String urlSpec) throws IOException {
        return new String(getResponseFromHttpUrl(urlSpec));
    }

    private ArrayList<Movie> downloadGalleryItems(String url) {
        ArrayList<Movie> items = new ArrayList<>();
        try {
            String jsonString = getUrlString(url);
            JSONObject jsonBody = new JSONObject(jsonString);
            parseItems(items, jsonBody);
        } catch (JSONException je) {
            Log.e(TAG, "Failed", je);
        } catch (IOException ioe) {
            Log.i(TAG, "Failed to fetch items" + ioe);
        }
        return items;
    }

    private void parseItems(List<Movie> items, JSONObject jsonBody)
            throws IOException, JSONException {

        JSONArray questionsJsonObject = jsonBody.getJSONArray("results");

        for (int i = 0; i < questionsJsonObject.length(); i++) {
            JSONObject questionJsonObject = questionsJsonObject.getJSONObject(i);

            Movie item = new Movie();
            item.setTitle(questionJsonObject.getString("title"));
            item.setPoster(questionJsonObject.getString("poster_path"));
            item.setDescription(questionJsonObject.getString("overview"));
            item.setRelease_date(questionJsonObject.getString("release_date"));
            item.setAverage(questionJsonObject.getString("vote_average"));
            Log.d(TAG, questionJsonObject.getString("title"));
            items.add(item);
        }
    }
}
