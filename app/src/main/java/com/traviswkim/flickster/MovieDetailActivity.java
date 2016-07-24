package com.traviswkim.flickster;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;
import com.traviswkim.flickster.databinding.ActivityMovieDetailBinding;
import com.traviswkim.flickster.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MovieDetailActivity extends AppCompatActivity {

    private ActivityMovieDetailBinding binding;
    TextView tvTitle;
    TextView tvReleaseDate;
    TextView tvVoteAvg;
    TextView tvOverview;
    ImageView ivMovieImage;
    AsyncHttpClient client = new AsyncHttpClient();
    String url = "http://api.themoviedb.org/3/movie/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail);
        //Enable back button
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String id = getIntent().getStringExtra("id");
        String pkey = getIntent().getStringExtra("pkey");
        if(id.length() > 0 && pkey.length() > 0) {
            url = url + id + "?api_key=" + pkey;
        }else{
            //Toast.makeText(MovieDetailActivity.this, R.string.not_available_movie, Toast.LENGTH_SHORT);
            startActivity(new Intent(MovieDetailActivity.this, MovieActivity.class));
        }

        tvTitle = binding.tvTitle;
        tvReleaseDate = binding.tvReleaseDate;
        tvVoteAvg = binding.tvVoteAvg;
        tvOverview = binding.tvOverview;
        ivMovieImage = binding.ivMovieImage;
        fetchAMovieData(0, false);

    }

    public void fetchAMovieData(int page, final boolean isRefresh) {
        // Send the network request to fetch the updated data
        // `client` here is an instance of Android Async HTTP
        client.get(url, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray movieJsonResults = null;
                try {
                    if(response != null) {
                        Movie aMovie = new Movie(response);
                        Picasso.with(MovieDetailActivity.this)
                                .load(aMovie.getBackdropPath(780))
                                .placeholder(R.drawable.poster_placeholder)
                                .error(R.drawable.poster_placeholder)
                                .into(ivMovieImage);
                        tvTitle.setText(aMovie.getOriginalTitle());
                        tvReleaseDate.setText(aMovie.getReleaseDate());
                        tvVoteAvg.setText(aMovie.getVoteAverage());
                        tvOverview.setText(aMovie.getOverview());

                        setTitle(aMovie.getOriginalTitle());

                        // Now we call setRefreshing(false) to signal refresh has finished
                        Log.d("DEBUG", response.toString());
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
