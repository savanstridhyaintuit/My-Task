package com.savan.mytask

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class PaginationAdapter(private val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val movieList = mutableListOf<Posts>()
    private var isLoadingAdded = false

    companion object {
        private const val LOADING = 0
        private const val ITEM = 1
    }

    fun setPostsList(movieList: List<Posts>) {
        this.movieList.clear()
        this.movieList.addAll(movieList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ITEM -> {
                val view = inflater.inflate(R.layout.item_list, parent, false)
                MovieViewHolder(view)
            }

            LOADING -> {
                val view = inflater.inflate(R.layout.item_progress, parent, false)
                LoadingViewHolder(view)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val movie = movieList[position]
        when (getItemViewType(position)) {
            ITEM -> {
                val movieViewHolder = holder as MovieViewHolder
                movieViewHolder.post_title.text = movie.title
                movieViewHolder.post_Id.text = movie.id.toString()
                movieViewHolder.post_card.setOnClickListener {
                    context.startActivity(
                        Intent(
                            context,
                            DetailActivity::class.java
                        ).putExtra("body", movie.body)
                    )
                }
            }

            LOADING -> {
                val loadingViewHolder = holder as LoadingViewHolder
                loadingViewHolder.progressBar.visibility = View.VISIBLE
                if (position == 100) {
                    loadingViewHolder.progressBar.visibility = View.GONE
                }
            }
        }
    }

    override fun getItemCount() = movieList.size

    override fun getItemViewType(position: Int): Int {
        return if (position == movieList.size - 1 && isLoadingAdded) LOADING else ITEM
    }

    fun addLoadingFooter() {
        isLoadingAdded = true
        add(Posts())
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false
        val position = movieList.size - 1
        val result = getItem(position)
        if (result != null) {
            movieList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun add(movie: Posts) {
        movieList.add(movie)
        notifyItemInserted(movieList.size - 1)
    }

    fun addAll(movieResults: List<Posts>) {
        for (result in movieResults) {
            add(result)
        }
    }

    fun getItem(position: Int): Posts? {
        return if (position < movieList.size) movieList[position] else null
    }

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val post_Id: TextView = itemView.findViewById(R.id.post_id)
        val post_title: TextView = itemView.findViewById(R.id.post_title)
        val post_card: CardView = itemView.findViewById(R.id.card_view)
    }

    inner class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val progressBar: ProgressBar = itemView.findViewById(R.id.loadmore_progress)
    }
}

