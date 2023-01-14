package nyp.sit.movieviewer.intermediate.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import nyp.sit.movieviewer.intermediate.R
import nyp.sit.movieviewer.intermediate.entity.Movie
import nyp.sit.movieviewer.intermediate.util.TheMovieDbUrlHelper

class FavouriteMovieAdapter(
    private val mContext: Context,
) : ArrayAdapter<Movie>(
    mContext,
    R.layout.list_item_movie,
) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val movie = getItem(position)
        val view = if (convertView == null) {
            val layoutInflater = LayoutInflater.from(mContext)
            layoutInflater.inflate(R.layout.list_item_movie, null)
        } else {
            convertView
        }
        if (movie != null) {
            val textView = view.findViewById<TextView>(R.id.movie_title)
            val imageView = view.findViewById<ImageView>(R.id.imageView)
            textView.text = movie.title
            Picasso.get()
                .load(TheMovieDbUrlHelper.builImageUrl(movie.poster_path ?: ""))
                .into(imageView)
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        }
        return view
    }
}
