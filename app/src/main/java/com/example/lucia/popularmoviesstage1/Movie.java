package com.example.lucia.popularmoviesstage1;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    //This variable stores the title of the movie
    private String mTitle;
    //This variable stores the release date of the movie;
    private String mReleaseDate;
    //This variable stores the address of the movie poster image
    private String mMoviePoster;
    //This variable stores the plot synopsis of the movie
    private String mPlotSynopsis;
    //This variable stores the average rating of the movie
    private double mRating;
    //This variable stores the popularity variable of the movie
    private double mPopularity;

    /**
     * Construct a new {@link Movie} object
     *
     * @param title        stores the title of the movie
     * @param poster       stores the string with the movie's poster
     * @param plotSynopsis stores the movie's plot synopsis
     * @param rating       stores the movie's rating
     * @param popularity   stores the movie's popularity variable
     * @param releaseDate  stores the movie's release date
     */
    public Movie(String title, String poster, String plotSynopsis, double rating, double popularity,
                 String releaseDate) {
        mTitle = title;
        mMoviePoster = poster;
        mPlotSynopsis = plotSynopsis;
        mRating = rating;
        mPopularity = popularity;
        mReleaseDate = releaseDate;
    }

    protected Movie(Parcel in) {
        mTitle = in.readString();
        mMoviePoster = in.readString();
        mPlotSynopsis = in.readString();
        mRating = in.readDouble();
        mPopularity = in.readDouble();
        mReleaseDate = in.readString();
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getMoviePoster() {
        return mMoviePoster;
    }

    public void setMoviePoster(String moviePoster) {
        mMoviePoster = moviePoster;
    }

    public String getPlotSynopsis() {
        return mPlotSynopsis;
    }

    public void setPlotSynopsis(String plotSynopsis) {
        mPlotSynopsis = plotSynopsis;
    }

    public double getRating() {
        return mRating;
    }

    public void setRating(double rating) {
        mRating = rating;
    }

    public double getPopularity() {
        return mPopularity;
    }

    public void setPopularity(double popularity) {
        mPopularity = popularity;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        mReleaseDate = releaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeString(mMoviePoster);
        dest.writeString(mPlotSynopsis);
        dest.writeDouble(mRating);
        dest.writeDouble(mPopularity);
        dest.writeString(mReleaseDate);
    }
}
