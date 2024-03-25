package com.pikachu.flixterplus

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.google.gson.Gson
import okhttp3.Headers
import org.json.JSONArray


private val getImgURL = "https://image.tmdb.org/t/p/w500"
private const val API_KEY = "f050e9cd49d9a138c81de2d8b8d1a374"

class MovieDetailsActivity : AppCompatActivity() {
    private lateinit var receivedMovie: MovieGeneric
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)

        val mMovieTitle = findViewById<TextView>(R .id.textMovieTitle)
        val mMovieOverview = findViewById<TextView>(R.id.textOverview)
        val mMovieLang = findViewById<TextView>(R.id.textLang)
        val mMovieRating = findViewById<TextView>(R.id.textRating)
        val mMovieRelease = findViewById<TextView>(R.id.textReleaseDatePlz)
        val mMovieGenre = findViewById<TextView>(R.id.textGenre)
        val mMovieRevenue = findViewById<TextView>(R.id.textRevenue)
        val intent = intent

        val client = AsyncHttpClient()
        val params = RequestParams()
        params["api_key"] = API_KEY


        // Get the JSON string from the intent
        val movieJson = intent.getStringExtra("MOVIE_EXTRA")
        val mGoBack = findViewById<ImageView>(R.id.goBack)


        // Convert the JSON string back to a LatestMovies object using Gson
        val gson = Gson()

        receivedMovie = gson.fromJson(movieJson, MovieGeneric::class.java)



        val movie_id = receivedMovie.id.toString()

        mMovieTitle.text = receivedMovie.title
        mMovieOverview.text = receivedMovie.overview

//"https://api.themoviedb.org/3/movie/${movie_id}/credits",
        client[
            "https://api.themoviedb.org/3/movie/${movie_id}",
            params,
            object : JsonHttpResponseHandler() { //connect these callbacks to your API call
                override fun onSuccess(
                    statusCode: Int,
                    headers: Headers,
                    json: JsonHttpResponseHandler.JSON
                ) {
                    // The wait for a response is over


                    Log.v("API CALL", json.toString())
                    mMovieRevenue.text = json.jsonObject.get("revenue").toString()
                    mMovieRating.text = json.jsonObject.get("vote_average").toString()
                    mMovieRelease.text = json.jsonObject.get("release_date").toString()
                    mMovieLang.text = json.jsonObject.get("original_language").toString()
                    val genre =  json.jsonObject.get("genres") as JSONArray



                    var boi = ""
                    for (i in 0 until genre.length()) {
                        val genreObject = genre.getJSONObject(i)
                        val genreName = genreObject.getString("name")
                        // Do something with genreName
                        boi += "$genreName, "
                    }
                    mMovieGenre.text = boi


                    // Look for this in Logcat:
                    Log.d("LatestMoviesFragment", "response successful" )
                }

                /*
                 * The onFailure function gets called when
                 * HTTP response status is "4XX" (eg. 401, 403, 404)
                 */
                override fun onFailure(
                    statusCode: Int,
                    headers: Headers?,
                    errorResponse: String,
                    t: Throwable?
                ) {
                    // The wait for a response is over
                    Log.v("FAIL", "Doesn't work")

                    // If the error is not null, log it!
                    t?.message?.let {
                        Log.e("LatestMoviesFragment", errorResponse)
                    }
                }
            }
        ]





//        Log.v("EEDE", receivedMovie.toString())
//        Log.v("EEDE", receivedMovie.title.toString())

//        mMovieOverview.text = receivedMovie.overview.toString()
//        mMovieTitle.text = receivedMovie.title.toString()

        mGoBack.setOnClickListener {
            onBackPressed()
        }
    }

}