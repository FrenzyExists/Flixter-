package com.pikachu.flixterplus

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Headers
import org.json.JSONArray


private var lastFetchTime: Long = 0
private const val CACHE_EXPIRATION_TIME = 5 * 60 * 1000 // 5 minutes in milliseconds


// Create a ViewModel to hold your data
class ExploreFragment : Fragment(), OnListFragmentInteractionListener {
    private val latestMovies = mutableListOf<MovieGeneric>()
    private val upcomingMovies = mutableListOf<MovieGeneric>()
    private val categories = mutableListOf<Categories>()

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

        latestMoviesRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        upcomingMoviesRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        categoriesRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

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
                textView.text = categories[position].name.toString()
            }

            override fun getItemCount(): Int {
                return categories.size
            }
        }

        Log.d("EXPLORERFRAG/LATEST_MOVIES/EVENMOREBEFORE/ONCREATE", latestMovies.toString())

        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchLatestMoviesData()
        fetchUpcomingMoviesData()
        fetchCategoriesData()
    }



    private fun isDataStale(): Boolean {
        val currentTime = System.currentTimeMillis()
        return currentTime - lastFetchTime > CACHE_EXPIRATION_TIME || lastFetchTime == 0L
    }

    private fun fetchCategoriesData() {
        val client = AsyncHttpClient()
        val params = RequestParams()
        params["api_key"] = SEARCH_API_KEY
        client.get("https://api.themoviedb.org/3/genre/movie/list", params, object : JsonHttpResponseHandler() {

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.e("EXPLORERFRAG/CAT", "Failed to fetch categories: $statusCode")
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON?) {
                Log.i("EXPLORERFRAG/CAT", "Successfully fetched categories: $json")

                val response = json?.jsonObject?.get("genres") as JSONArray
                val gson = Gson()

                val arrayCat = object : TypeToken<List<Categories>>() {}.type
                val models: List<Categories>? =
                    gson.fromJson(response.toString(), arrayCat)
                models.let {
                    if (it != null) {
                        categories.addAll(it)
                        categoriesRecyclerView
                            .adapter?.notifyDataSetChanged()
                    }
                }
            }
        })
    }
    private fun fetchUpcomingMoviesData() {
        val client = AsyncHttpClient()
        val params = RequestParams()
        params["api_key"] = SEARCH_API_KEY
        params["page"] = "1"
        params["language"] = "en-US"
        params["sort_by"] = "popularity.desc"
        client.get(BASE_URL.plus("upcoming"), params, object : JsonHttpResponseHandler() {

            /**
             *
             */
            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.e("EXPLORERFRAG/UPCOMING", "Failed to fetch movies: $statusCode")
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON?) {
                Log.i("EXPLORERFRAG/UPCOMING", "Successfully fetched movies: $json")

                val response = json?.jsonObject?.get("results") as JSONArray
                val gson = Gson()

                val arrayUpcoming = object : TypeToken<List<MovieGeneric>>() {}.type
                val models: List<MovieGeneric>? =
                    gson.fromJson(response.toString(), arrayUpcoming)
                models.let {
                    if (it != null) {
                        upcomingMovies.addAll(it)
                        Log.d("EXPLORERFRAG/LATEST_MOVIES/ONCHANGE", latestMovies.toString())
                        upcomingMoviesRecyclerView.adapter?.notifyDataSetChanged()
                    }
                }
            }
        })
    }

    private fun fetchLatestMoviesData() {
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
            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.e("EXPLORERFRAG/LATEST_MOVIES", "Failed to fetch articles: $statusCode")
            }

            /**
             *
             */
            @SuppressLint("NotifyDataSetChanged")
            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON?) {
                Log.i("EXPLORERFRAG/LATEST_MOVIES", "Successfully fetched movies: $json")

                val response = json?.jsonObject?.get("results") as JSONArray
                val gson = Gson()

                val arrayLatest = object : TypeToken<List<MovieGeneric>>() {}.type
                val models: List<MovieGeneric>? =
                    gson.fromJson(response.toString(), arrayLatest)
                models.let {
                    if (it != null) {
                        latestMovies.addAll(it)
                        Log.d("EXPLORERFRAG/LATEST_MOVIES/ONCHANGE", latestMovies.toString())
                        latestMoviesRecyclerView.adapter?.notifyDataSetChanged()
                    }
                }
            }
        })
    }

    override fun onItemClick(item: MovieGeneric) {
        TODO("Not yet implemented")
    }

}