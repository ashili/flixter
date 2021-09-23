package com.example.flixter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixter.models.Movie;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

public class DetailActivity extends YouTubeBaseActivity {
    public final static String YOUTUBE_KEY = "AIzaSyDGVwuBRydMjiv32x9DIbXZ4I5t4C34SNc";
    public static final String VIDEOS_URL = "https://api.themoviedb.org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

    TextView tvtitle;
    TextView tvoverview;
    RatingBar ratingBar;
    YouTubePlayerView youTubePlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tvtitle= findViewById(R.id.tvtitle);
        tvoverview= findViewById(R.id.tvoverview);
        ratingBar= findViewById(R.id.ratingBar);
        youTubePlayerView = findViewById(R.id.player);


        Movie movie = Parcels.unwrap(getIntent().getParcelableExtra("movie"));
        tvtitle.setText(movie.getTitle());
        tvoverview.setText(movie.getOverView());
        ratingBar.setRating((float)movie.getRating());


        AsyncHttpClient client = new AsyncHttpClient();
        client.get(String.format(VIDEOS_URL, movie.getMovieId()), new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {

                        try {
                            JSONArray results = json.jsonObject.getJSONArray("results");
                            if (results.length() == 0){
                                return;
                            }
                            String youtubeKey = results.getJSONObject(0).getString("key");
                            Log.d("DetailActivity",youtubeKey);
                            initializeYoutube(youtubeKey);
                        } catch (JSONException e) {
                            Log.e("DetailActivity","Failed to parse JSON results");
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {

                    }
                });



    }

    private void initializeYoutube(String youtubeKey) {

        youTubePlayerView.initialize(YOUTUBE_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                Log.d("DetailsActivity", "OnInitializationSuccess");
                youTubePlayer.cueVideo(youtubeKey);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.d("DetailsActivity", "OnInitializationFailure");
            }
        });
    }
}