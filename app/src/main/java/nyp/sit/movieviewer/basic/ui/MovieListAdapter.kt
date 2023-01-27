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
) : ArrayAdapter<Movie>(
    mContext,
    android.R.layout.simple_list_item_1,
) {
    private val resource = android.R.layout.simple_list_item_1

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view == null) {
            val layoutInflater = LayoutInflater.from(mContext)
            view = layoutInflater.inflate(resource, null)
        }

        val movie = getItem(position)

        if (movie != null) {
            val text = view?.findViewById<TextView>(android.R.id.text1)
            if (text != null) {
                text.text = movie.title
            }
        }

        return view!!
    }
}