package nyp.sit.movieviewer.basic.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import nyp.sit.movieviewer.basic.R
import nyp.sit.movieviewer.basic.entity.Movie
import nyp.sit.movieviewer.basic.entity.User

class MovieAdapter(
    diffCallback: DiffUtil.ItemCallback<Movie>,
    private val onItemClickListener: OnItemClickListener
) : PagingDataAdapter<Movie, MovieAdapter.ViewHolder>(diffCallback) {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView

        init {
            textView = view.findViewById(R.id.movie_title)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(movie: Movie)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_movie, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = getItem(position)
        holder.textView.text = movie?.title
        holder.textView.setOnClickListener{
            if (movie != null) {
                onItemClickListener.onItemClick(movie)
            }
        }
    }
}