package com.traviswkim.flickster;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.traviswkim.flickster.adapters.MovieArrayAdapter;
import com.traviswkim.flickster.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MovieActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeContainer;
    AsyncHttpClient client = new AsyncHttpClient();
    ArrayList<Movie> movies;
    MovieArrayAdapter movieAdapter;
    ListView lvItems;
    String url = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    String pKey = "a07e22bc18f5cb106bfe4cc1f83ad8ed";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        lvItems = (ListView)findViewById(R.id.lvMovies);
        movies = new ArrayList<>();
        movieAdapter = new MovieArrayAdapter(this, movies);
        lvItems.setAdapter(movieAdapter);

        //getting movie data
        fetchTimelineAsync(0, false);

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchTimelineAsync(0, true);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        setupListViewListener();
    }

    private void setupListViewListener(){
        lvItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    public void onItemClick(AdapterView<?> adapter, View item, int pos, long id){
                        Intent i = new Intent(MovieActivity.this, MovieDetailActivity.class);
                        i.putExtra("id", movies.get(pos).getId());
                        i.putExtra("pkey", pKey);
                        startActivity(i);
                    }
                });
    }

    public void fetchTimelineAsync(int page, final boolean isRefresh) {
        // Send the network request to fetch the updated data
        // `client` here is an instance of Android Async HTTP
        client.get(url, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray movieJsonResults = null;
                try {
                    if(response.optJSONArray("results") != null) {
                        movieJsonResults = response.getJSONArray("results");
                        movies.clear();
                        movieAdapter.clear();
                        movies.addAll(Movie.fromJSONArray(movieJsonResults));
                        movieAdapter.notifyDataSetChanged();
                        // Now we call setRefreshing(false) to signal refresh has finished
                        Log.d("DEBUG", movieJsonResults.toString());
                        if(isRefresh == true) {
                            swipeContainer.setRefreshing(false);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("DEBUG", "Fail to refresh movie list");
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

}
