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

class GenericMovieRecyclerViewAdapter(private val context: Context, private val movies: MutableList<MovieGeneric>) :
    RecyclerView.Adapter<GenericMovieRecyclerViewAdapter.ViewHolder> () {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val myMovie = movies[position]
        holder.bind(myMovie)
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
                .load(movie.posterPath)
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