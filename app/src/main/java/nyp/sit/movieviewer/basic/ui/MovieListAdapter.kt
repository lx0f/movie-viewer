package nyp.sit.movieviewer.basic.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import nyp.sit.movieviewer.basic.R
import nyp.sit.movieviewer.basic.entity.Movie

class MovieListAdapter(
    private val mContext: Context,
    private val resource: Int = R.layout.list_item_movie,
) : ArrayAdapter<Movie>(
    mContext,
    resource,
) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view == null) {
            val layoutInflater = LayoutInflater.from(mContext)
            view = layoutInflater.inflate(resource, null)
        }

        val movie = getItem(position)

        if (movie != null) {
            val text = view?.findViewById<TextView>(R.id.movie_title)
            if (text != null) {
                text.text = movie.title
            }
        }

        return view!!
    }
}