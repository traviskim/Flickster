package com.traviswkim.flickster.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by traviswkim on 7/18/16.
 */
public class Movie {

    public enum MoviePopularities{
        POPULAR, LESS_POPULAR;
    }

    public String getId() { return id; }

    public String getPosterPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", posterPath);
    }

    public String getBackdropPath(int size) {
        return String.format("https://image.tmdb.org/t/p/w%s/%s",size, backdropPath);
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

    public MoviePopularities getPopular() { return popular; }

    public String getVoteAverage() { return voteAverage; }

    String id;
    String posterPath;
    String backdropPath;
    String originalTitle;
    String overview;
    String releaseDate;
    String voteAverage;
    MoviePopularities popular;

    private String getJSONObjValue(JSONObject jsonObj, String aKey) throws JSONException{
        String value = "";
        if(jsonObj.has(aKey)) {
            value = jsonObj.getString(aKey);
        }
        return value;
    }

    public Movie(JSONObject jsonObj) throws JSONException{
        this.id = getJSONObjValue(jsonObj, "id");
        this.posterPath = getJSONObjValue(jsonObj, "poster_path");
        this.backdropPath = getJSONObjValue(jsonObj, "backdrop_path");
        this.originalTitle = getJSONObjValue(jsonObj, "original_title");
        this.overview = getJSONObjValue(jsonObj, "overview");
        this.releaseDate = getJSONObjValue(jsonObj, "release_date");
        this.voteAverage = getJSONObjValue(jsonObj, "vote_average");
        //Default
        this.popular = MoviePopularities.LESS_POPULAR;
        if(!this.voteAverage.isEmpty() && Float.valueOf(voteAverage) >= 5){
            this.popular = MoviePopularities.POPULAR;
        }

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
