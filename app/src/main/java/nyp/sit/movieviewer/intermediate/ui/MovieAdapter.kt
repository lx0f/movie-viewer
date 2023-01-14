package nyp.sit.movieviewer.intermediate.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import nyp.sit.movieviewer.intermediate.R
import nyp.sit.movieviewer.intermediate.entity.Movie
import nyp.sit.movieviewer.intermediate.util.TheMovieDbUrlHelper

class MovieAdapter(
    diffCallback: DiffUtil.ItemCallback<Movie>,
    private val onItemClickListener: OnItemClickListener
) : PagingDataAdapter<Movie, MovieAdapter.ViewHolder>(diffCallback) {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView
        val imageView: ImageView
        val layout: LinearLayout

        init {
            textView = view.findViewById(R.id.movie_title)
            imageView = view.findViewById(R.id.imageView)
            layout = view.findViewById(R.id.layout)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(movie: Movie)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_movie, parent, false)
        val image = view.findViewById<ImageView>(R.id.imageView)
        image.scaleType = ImageView.ScaleType.CENTER_CROP
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = getItem(position)
        Picasso.get()
            .load(TheMovieDbUrlHelper.builImageUrl(movie?.poster_path ?: ""))
            .into(holder.imageView)
        holder.textView.text = movie?.title
        holder.layout.setOnClickListener{
            if (movie != null) {
                onItemClickListener.onItemClick(movie)
            }
        }
    }
}