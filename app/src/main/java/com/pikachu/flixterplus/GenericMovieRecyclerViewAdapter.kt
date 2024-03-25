package com.pikachu.flixterplus

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson

private const val getImgURL = "https://image.tmdb.org/t/p/w500"

class GenericMovieRecyclerViewAdapter(private val context: Context, private val movies: MutableList<MovieGeneric>) :
    RecyclerView.Adapter<GenericMovieRecyclerViewAdapter.ViewHolder> () {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val myMovie = movies[position]
        holder.bind(myMovie)

        holder.itemView.setOnClickListener{
            val m = movies[position]
            m.let {
                val gson = Gson()
                val movieJson = gson.toJson(m)

                val intent = Intent(context, MovieDetailsActivity::class.java)
                intent.putExtra("MOVIE_EXTRA", movieJson) // Pass movie object as JSON string
                context.startActivity(intent)
            }
        }

    }

    override fun getItemCount(): Int {
        return movies.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        private val mediaImageView = itemView.findViewById<ImageView>(R.id.imageViewPoster)
        private val titleTextView = itemView.findViewById<TextView>(R.id.textViewTitle)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(movie: MovieGeneric) {
            titleTextView.text = movie.title

            Glide.with(context)
                .load(getImgURL + movie.posterPath)
                .into(mediaImageView)
        }


        override fun onClick(v: View?) {
            // Get selected article
//            val movie = movies[absoluteAdapterPosition]

            // Navigate to Details screen and pass selected article
//            val intent = Intent(context, DetailActivity::class.java)
//            intent.putExtra(ARTICLE_EXTRA, article)
//            context.startActivity(intent)
        }
    }

}