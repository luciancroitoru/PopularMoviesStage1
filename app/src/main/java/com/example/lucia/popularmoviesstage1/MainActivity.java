package com.example.lucia.popularmoviesstage1;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.valueOf;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks
        <List<Movie>>, NavigationView.OnNavigationItemSelectedListener {

    private static final int MOVIE_LOADER_ID = 1;
    public String sortBy = "top_rated";
    public View noConnectionView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private MovieAdapter movieAdapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout_main);

        progressBar = findViewById(R.id.progress_bar);

        //Setup the navigation drawer
        drawerLayout = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        //setup of the recycler view that hosts the movie list
        RecyclerView recyclerView = findViewById(R.id.movie_list);
        noConnectionView = findViewById(R.id.error_loading_movie_list);

        //setup of the movie list
        int gridRows = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 4 : 2;
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(recyclerView.getContext(),
                gridRows);
        recyclerView.setLayoutManager(layoutManager);

        movieAdapter = new MovieAdapter(this, new ArrayList<Movie>());
        recyclerView.setAdapter(movieAdapter);

        //set up an error message if there is no connection to the database
        if (isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(MOVIE_LOADER_ID, null, this);
        } else {
            noConnectionView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * This method verifies if the device has internet connection
     *
     * @return true if there is connection and false if connection cannot be made
     */
    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean hasConnection = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            hasConnection = true;
        }
        return hasConnection;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.sort_by_most_popular) {
            // sets order for the movie list by most popular
            sortBy = getString(R.string.popular);
            drawerLayout.closeDrawer(GravityCompat.START);
            if (isConnected()) {
                getLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
                noConnectionView.setVisibility(View.GONE);
            }
        } else if (id == R.id.sort_by_top_rated) {
            // sets order for the movie list by top rated
            sortBy = getString(R.string.rated);
            drawerLayout.closeDrawer(GravityCompat.START);
            if (isConnected()) {
                getLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
                noConnectionView.setVisibility(View.GONE);
            }
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return actionBarDrawerToggle.onOptionsItemSelected(item)|| super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle bundle) {
        URL requestUrl = JsonUtils.createUrl(sortBy);
        return new MovieLoader(this, requestUrl.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> movieObjectList) {
        progressBar.setVisibility(View.GONE);
        movieAdapter.addAll(movieObjectList);
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        movieAdapter.clearAll();
    }

    /**
     * When the back button is clicked and the user has the navigationDrawer opened, this method
     * closes the drawer
     */
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}