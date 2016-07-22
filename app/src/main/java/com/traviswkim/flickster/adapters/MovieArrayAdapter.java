package com.traviswkim.flickster.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.traviswkim.flickster.R;
import com.traviswkim.flickster.models.Movie;

import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by traviswkim on 7/19/16.
 */
public class MovieArrayAdapter extends ArrayAdapter<Movie> {
    private static class ViewHolder{
        ImageView ivImage;
        TextView tvTitle;
        TextView tvOverview;
        TextView tvReleaseDate;
    }

    public MovieArrayAdapter(Context context, List<Movie> movies){
        super(context, android.R.layout.simple_list_item_1, movies);
    }

    // Return an integer representing the type by fetching the enum type ordinal
    @Override
    public int getItemViewType(int position) {
        return getItem(position).getPopular().ordinal();
    }
    // Total number of types is the number of enum values
    @Override
    public int getViewTypeCount() {
        return Movie.MoviePopularities.values().length;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        Movie movie = getItem(position);
        int type = getItemViewType(position);

        //Check the existing view being used
        if(convertView == null){
            viewHolder = new ViewHolder();
            //LayoutInflater inflater = LayoutInflater.from(getContext());
            //convertView = inflater.inflate(R.layout.item_movie, parent, false);

            convertView = getInflatedLayoutForType(type);
            if(convertView == null){
                return null;
            }
            viewHolder.ivImage = (ImageView) convertView.findViewById(R.id.ivMovieImage);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            viewHolder.tvOverview = null;
            viewHolder.tvReleaseDate = null;
            if(type != Movie.MoviePopularities.POPULAR.ordinal()) {
                viewHolder.tvOverview = (TextView) convertView.findViewById(R.id.tvOverview);
                viewHolder.tvReleaseDate = (TextView) convertView.findViewById(R.id.tvReleaseDate);
            }
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //populate data
        int orientation = convertView.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE || type == Movie.MoviePopularities.POPULAR.ordinal()) {
            int size = 780;
            if(type == Movie.MoviePopularities.POPULAR.ordinal()){
                size = 1280;
            }
            Picasso.with(getContext())
                    .load(movie.getBackdropPath(size))
                    .placeholder(R.drawable.poster_placeholder)
                    .error(R.drawable.poster_placeholder)
                    .transform(new RoundedCornersTransformation(10, 10))
                    .into(viewHolder.ivImage);
        }else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            Picasso.with(getContext())
                    .load(movie.getPosterPath())
                    .resize(342, 0)
                    .placeholder(R.drawable.poster_placeholder)
                    .error(R.drawable.poster_placeholder)
                    .transform(new RoundedCornersTransformation(10, 10))
                    .into(viewHolder.ivImage);
        }

        viewHolder.tvTitle.setText(movie.getOriginalTitle());
        if(type != Movie.MoviePopularities.POPULAR.ordinal()) {
            viewHolder.tvOverview.setText(movie.getOverview());
            viewHolder.tvReleaseDate.setText(movie.getReleaseDate());
        }

        return convertView;
    }

    // Given the item type, responsible for returning the correct inflated XML layout file
    private View getInflatedLayoutForType(int type) {
        if (type == Movie.MoviePopularities.POPULAR.ordinal()) {
            return LayoutInflater.from(getContext()).inflate(R.layout.item_movie_land, null);
        } else if (type == Movie.MoviePopularities.LESS_POPULAR.ordinal()) {
            return LayoutInflater.from(getContext()).inflate(R.layout.item_movie, null);
        } else {
            return null;
        }
    }
}
