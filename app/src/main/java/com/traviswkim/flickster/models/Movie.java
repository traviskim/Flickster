package com.traviswkim.flickster.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by traviswkim on 7/18/16.
 */
public class Movie {
    public String getPosterPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", posterPath);
    }

    public String getBackdropPath() {
        return String.format("https://image.tmdb.org/t/p/w780/%s", backdropPath);
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    String posterPath;
    String backdropPath;
    String originalTitle;
    String overview;
    String releaseDate;

    public Movie(JSONObject jsonObj) throws JSONException{
        this.posterPath = jsonObj.getString("poster_path");
        this.backdropPath = jsonObj.getString("backdrop_path");
        this.originalTitle = jsonObj.getString("original_title");
        this.overview = jsonObj.getString("overview");
        this.releaseDate = jsonObj.getString("release_date");

    }

    public static ArrayList<Movie> fromJSONArray(JSONArray jsonArray){
        ArrayList<Movie> result = new ArrayList<>();
        for(int i=0; i<jsonArray.length(); i++){
            try {
                result.add(new Movie(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
