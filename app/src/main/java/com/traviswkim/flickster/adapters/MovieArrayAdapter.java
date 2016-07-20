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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        Movie movie = getItem(position);
        //Check the existing view being used
        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_movie, parent, false);
            viewHolder.ivImage = (ImageView) convertView.findViewById(R.id.ivMovieImage);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            viewHolder.tvOverview = (TextView) convertView.findViewById(R.id.tvOverview);
            viewHolder.tvReleaseDate = (TextView) convertView.findViewById(R.id.tvReleaseDate);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //populate data
        int orientation = convertView.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            Picasso.with(getContext())
                    .load(movie.getPosterPath())
                    .resize(342, 0)
                    .placeholder(R.drawable.poster_placeholder)
                    .error(R.drawable.poster_placeholder)
                    .transform(new RoundedCornersTransformation(10, 10))
                    .into(viewHolder.ivImage);
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Picasso.with(getContext())
                    .load(movie.getBackdropPath())
                    .resize(780, 0)
                    .placeholder(R.drawable.poster_placeholder)
                    .error(R.drawable.poster_placeholder)
                    .transform(new RoundedCornersTransformation(10, 10))
                    .into(viewHolder.ivImage);
        }
        viewHolder.tvTitle.setText(movie.getOriginalTitle());
        viewHolder.tvOverview.setText(movie.getOverview());
        viewHolder.tvReleaseDate.setText(movie.getReleaseDate());

        return convertView;
    }
}
