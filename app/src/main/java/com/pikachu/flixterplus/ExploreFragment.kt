package com.pikachu.flixterplus

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import kotlinx.serialization.json.Json
import okhttp3.Headers
import org.json.JSONArray


private var lastFetchTime: Long = 0
private const val CACHE_EXPIRATION_TIME = 5 * 60 * 1000 // 5 minutes in milliseconds



class ExploreFragment : Fragment(), OnListFragmentInteractionListener {
    private val latestMovies = mutableListOf<MovieGeneric>()
    private val upcomingMovies = mutableListOf<MovieGeneric>()
    private val categories = mutableListOf<String>()

    private lateinit var latestMoviesRecyclerView: RecyclerView
    private lateinit var upcomingMoviesRecyclerView: RecyclerView
    private lateinit var categoriesRecyclerView: RecyclerView


    /**
     * This method is responsible for creating the fragment's view hierarchy.
     * In this method, you inflate the layout for the fragment and initialize
     * any views within that layout. It returns the root view of the
     * fragment's layout. This is where you set up the UI components of
     * your fragment
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_explore, container, false)

        latestMoviesRecyclerView = view.findViewById(R.id.latestMovieRecyclerView)
        upcomingMoviesRecyclerView = view.findViewById(R.id.upcomingMovieRecyclerView)
        categoriesRecyclerView = view.findViewById(R.id.categoriesRecyclerView)

        val latestMovieAdapter = GenericMovieRecyclerViewAdapter(requireContext(), latestMovies)
        latestMoviesRecyclerView.adapter = latestMovieAdapter

        val upcomingMovieAdapter = GenericMovieRecyclerViewAdapter(requireContext(), upcomingMovies)
        upcomingMoviesRecyclerView.adapter = upcomingMovieAdapter


        categoriesRecyclerView.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): RecyclerView.ViewHolder {
                val categoryView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_category, parent, false)
                return object : RecyclerView.ViewHolder(categoryView) {}
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                val textView = holder.itemView.findViewById<TextView>(R.id.categoryText)
                textView.text = categories[position]
            }

            override fun getItemCount(): Int {
                return categories.size
            }
        }

        fetchLatestMoviesData()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun fetchLatestMoviesData() {
        val currentTime = System.currentTimeMillis()

        // Check if data is stale or cache is empty
        if (currentTime - lastFetchTime > CACHE_EXPIRATION_TIME || lastFetchTime.toInt() == 0) {
            val client = AsyncHttpClient()
            val params = RequestParams()
            params["api_key"] = SEARCH_API_KEY
            params["page"] = "1"
            params["language"] = "en-US"
            params["sort_by"] = "popularity.desc"

            client.get(BASE_URL.plus("now_playing"), params, object : JsonHttpResponseHandler() {

                /**
                 *
                 */
                override fun onFailure(statusCode: Int, headers: Headers?, response: String?, throwable: Throwable?) {
                    Log.e("EXPLORERFRAG/LATEST_MOVIES", "Failed to fetch articles: $statusCode")
                }

                /**
                 *
                 */
                override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON?) {
                    Log.i("EXPLORERFRAG/LATEST_MOVIES", "Successfully fetched movies: $json")

                    // Update last fetch time
                    lastFetchTime = System.currentTimeMillis()

                    val response = json?.jsonObject?.get("results") as JSONArray
                    Log.v("API CALL", response.toString())

                    // Add explicit type information to help the compiler infer the type correctly
                    val parsedJSON = Json.decodeFromString<GenericMovieListResponse>(
                        GenericMovieListResponse.serializer(),
                        json.jsonArray.toString()
                    )
//                    val parsedJson = createJson().
//                    decodeFromString(
//                        GenericMovieListResponse.serializer(),
//                        json.jsonObject.toString()
//                    )
//
//
////                    parsedJson.response?.let { list ->
////                        latestMovies.addAll(list)
////                        latestMoviesRecyclerView.adapter?.notifyDataSetChanged()
////                    }
                }
            })
        } else {
            // Use cached data
            Log.d("EXPLORERFRAG/LATEST_MOVIES", "Using cached data")
        }
    }

    override fun onItemClick(item: MovieGeneric) {
        TODO("Not yet implemented")
    }

}