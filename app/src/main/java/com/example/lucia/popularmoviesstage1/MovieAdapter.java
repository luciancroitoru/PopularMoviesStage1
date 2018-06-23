package com.example.lucia.popularmoviesstage1;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import static java.lang.String.valueOf;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    //the list of movies that fills the recyclerView;
    private List<Movie> moviesList;
    private Context mContext;

    /**
     * MovieAdapter constructor that takes the movieList to display within context
     *
     * @param context    the context within will be displayed in the moviesList
     * @param moviesList the list of movie that will be displayed
     */
    public MovieAdapter(Context context, List<Movie> moviesList) {
        this.mContext = context;
        this.moviesList = moviesList;
    }

    /**
     * This method helps to create the Url for the movie poster
     *
     * @param moviePosterString
     * @return
     */
    public static String buildPosterUrl(String moviePosterString) {
        final String BASE_URL_POSTER = "https://image.tmdb.org/t/p/";
        final String IMAGE_SIZE = "w185";

        Uri buildUri = Uri.parse(BASE_URL_POSTER).buildUpon()
                .appendPath(IMAGE_SIZE)
                .appendEncodedPath(moviePosterString)
                .build();
        return buildUri.toString();
    }

    /**
     * This method creates Views each time when the RecyclerView needs it
     *
     * @param parent   the ViewGroup that will host the ViewHolder
     * @param viewType
     * @return a new MovieViewHolder that has the View for each item
     */
    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout list_item.xml
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new MovieViewHolder(itemView);
    }

    /**
     * This method is called to display information on a specific position and to register
     * a clickListener that opens the detail layout for each movie item from the list.
     *
     * @param holder   this MovieViewHolder should be updated
     * @param position the position of the item within the adapter
     */
    @Override
    public void onBindViewHolder(MovieViewHolder holder, final int position) {
        //get the position
        final Movie movieItem = moviesList.get(position);

        ImageView moviePosterImageView = holder.moviePoster;
        String moviePosterUrlString = buildPosterUrl(movieItem.getMoviePoster());
        final TextView errorLoadMessage = holder.errorMessage;
        final TextView ratingValueTextView = holder.ratingValue;
        ratingValueTextView.setVisibility(View.INVISIBLE);
        final String ratingString = valueOf(movieItem.getRating());


        Picasso.with(mContext).load(moviePosterUrlString).into(moviePosterImageView,
                new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        ratingValueTextView.setText(ratingString);
                        ratingValueTextView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError() {
                        errorLoadMessage.setVisibility(View.VISIBLE);
                    }
                });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra("Movie", movieItem);
                mContext.startActivity(intent);
            }
        });
    }

    /**
     * this method is counting the number of items in the list
     *
     * @return the number of items available
     */
    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    /**
     * This method adds new objects to the listVew
     */
    public void addAll(List<Movie> moviesList) {
        this.moviesList.clear();
        this.moviesList.addAll(moviesList);
        notifyDataSetChanged();
    }

    /**
     * This method clears the movies list
     */
    public void clearAll() {
        this.moviesList.clear();
        notifyDataSetChanged();
    }

    /**
     * This class helps to hold the views
     */
    class MovieViewHolder extends RecyclerView.ViewHolder {
        //Variables that help to set the information needed in the RecyclerView that display the
        // movies list in the drawer_layout_main.xml layout.
        ImageView moviePoster;
        TextView errorMessage;
        TextView ratingValue;

        //ViewHolder's constructor
        public MovieViewHolder(View itemView) {
            super(itemView);

            //Find Views that display each item
            moviePoster = itemView.findViewById(R.id.movie_poster_list_item);
            errorMessage = itemView.findViewById(R.id.error_loading_movie_list);
            ratingValue = itemView.findViewById(R.id.rating_value_list_item);
        }
    }
}

