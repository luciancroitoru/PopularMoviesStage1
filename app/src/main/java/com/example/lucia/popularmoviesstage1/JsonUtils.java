package com.example.lucia.popularmoviesstage1;

import android.net.Uri;
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

import okhttp3.OkHttpClient;

/**
 * This class manages all the Json String changes, from the moment that the request is formed until data is parsed
 * and fetched from the Json String.
 */

public class JsonUtils {

    private static final String LOG_TAG = JsonUtils.class.getName();

    //Constants that will help to parse the Json String
    private static final String ROOT_JSON = "results";
    private static final String RATE = "vote_average";
    private static final String IMAGE = "poster_path";
    private static final String ORIGINAL_TITLE = "original_title";
    private static final String OVERVIEW = "overview";
    private static final String RELEASE_DATE = "release_date";
    private static final String POPULARITY = "popularity";

    //Constants for creating the URL
    private static final String BASE_URL
            = "https://api.themoviedb.org/3/movie";
    private static final String PARAM_KEY = "api_key";
    private static final String API_KEY = ""; // <- Please add your API key :)
    private OkHttpClient client;


    /**
     * This method creates the URL that sends the makeHttpRequest method
     *
     * @param sortBy is the parameter that will be chosen by user from preferences
     * @return the URL concatenated in this method
     */
    public static URL createUrl(String sortBy) {
        Uri buildUri = Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(sortBy)
                .appendQueryParameter(PARAM_KEY, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(buildUri.toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * This method creates a connection to the movie database server and verifies the response
     *
     * @param url is the url created in the createUrl method
     * @return the String response from the server; if the url (from the createUrl method) is
     * null then the response is an empty string.
     */
    public static String makeHttpRequest(URL url) throws IOException {

        String json = "";
        //Create connection
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            json = readStream(in);
        } finally {
            urlConnection.disconnect();
        }

        return json;
    }

    /**
     * This method takes the response received from the server and reads it
     *
     * @param in the response received from the server
     * @return the String that is transformed from the received data from the server
     */
    private static String readStream(InputStream in) throws IOException {
        StringBuilder receivedAnswer = new StringBuilder();
        if (in != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(in, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();

            while (line != null) {
                receivedAnswer.append(line);
                line = reader.readLine();
            }
        }
        return receivedAnswer.toString();
    }

    /**
     * This method parses the Json String received from the makeHttpRequest method,
     * with all movie info.
     *
     * @param json is the string received from the makeHttpRequest
     * @return a Movie list that has all needed parameters that are used to populate the UI
     * in the main layout.
     */
    public static List<Movie> parseMovieJson(String json) {
        //Create an empty arrayList of Movies to which we can start adding objects
        List<Movie> movieList = new ArrayList<>();
        //The Json string parsing.
        try {
            //Create JsonObject from json String.
            JSONObject rootJsonObject = new JSONObject(json);
            //Extract "results" Array that has all information that we need to parse
            JSONArray rootArray = rootJsonObject.getJSONArray(ROOT_JSON);
            //Extract from each element of array information needed to create a movie List and
            // for each movie object all the details needed.
            for (int i = 0; i < rootArray.length(); i++) {
                JSONObject movieJsonObject = rootArray.getJSONObject(i);
                String originalTitle = movieJsonObject.getString(ORIGINAL_TITLE);

                String moviePoster = movieJsonObject.getString(IMAGE);

                String overview = movieJsonObject.getString(OVERVIEW);

                double rating = movieJsonObject.getDouble(RATE);

                double popularity = movieJsonObject.getDouble(POPULARITY);

                String releaseDate = movieJsonObject.getString(RELEASE_DATE);

                movieList.add(new Movie(originalTitle, moviePoster, overview, rating, popularity, releaseDate));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movieList;
    }

    /**
     * The method helps to fetch data
     *
     * @param requestUrl
     * @return
     */
    public static List<Movie> fetchMoviesData(String requestUrl) {

        //With the url variable the app makes a makeHttpRequest in order to obtain the json String
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(new URL(requestUrl));
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making HTTP request: ", e);
        }

        return parseMovieJson(jsonResponse);
    }
}
