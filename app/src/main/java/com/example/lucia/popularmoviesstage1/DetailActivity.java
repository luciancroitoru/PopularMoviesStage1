package com.example.lucia.popularmoviesstage1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import static java.lang.String.valueOf;

/**
 * This class will populate the details activity layout with movies.
 */
public class DetailActivity extends AppCompatActivity {

    TextView dReleaseDate, dRating, dPlotSynopsis, dTitle;
    ImageView dMoviePoster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        dMoviePoster = findViewById(R.id.detail_movie_poster_details_view);

        Intent intent = getIntent();
        Movie currentMovie = intent.getParcelableExtra("Movie");

        String moviePosterUrlString = MovieAdapter.buildPosterUrl(currentMovie.getMoviePoster());
        Picasso.with(this).load(moviePosterUrlString).into(dMoviePoster);
        displayMovieUI(currentMovie);

        setTitle(currentMovie.getTitle());
    }

    /**
     * This method takes information from movie objects and displays it
     * in the drawer_layout_main.xml layout
     *
     * @param movie the Movie object that contains all information needed
     */
    private void displayMovieUI(Movie movie) {

        dTitle = findViewById(R.id.detail_movie_title);
        dTitle.setText(movie.getTitle());

        dReleaseDate = findViewById(R.id.detail_movie_release_date);
        dReleaseDate.setText(movie.getReleaseDate());

        dRating = findViewById(R.id.detail_movie_vote_average);
        dRating.setText(valueOf(movie.getRating()));

        dPlotSynopsis = findViewById(R.id.detail_movie_plot_synopsis);
        dPlotSynopsis.setText(movie.getPlotSynopsis());

    }
}

