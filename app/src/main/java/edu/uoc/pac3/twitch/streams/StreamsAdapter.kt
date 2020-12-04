package edu.uoc.pac3.twitch.streams

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.uoc.pac3.R
import edu.uoc.pac3.data.streams.Stream


class StreamsAdapter(private var streams: List<Stream>) : RecyclerView.Adapter<StreamsAdapter.ViewHolder>() {

    private fun getStream(position: Int): Stream {
        return streams[position]
    }

    fun setStreams(streams: List<Stream>) {
        this.streams = streams
        // Reloads the RecyclerView with new adapter data
        notifyDataSetChanged()
    }

    // Creates View Holder for re-use
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val contactView = inflater.inflate(R.layout.row_stream_list_content, parent, false)
        // Return a new holder instance
        return ViewHolder(contactView)
    }


    // Binds re-usable View for a given position
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val stream = getStream(position)
        holder.titleView.text = stream.title
        holder.userView.text = stream.userName
        var url = stream.thumbnailUrl?.replace("{width}", "200")?.replace("{height}", "100")
        Glide.with(holder.itemView)
                .load(url)
                .into(holder.thumbnailView)
    }

    // Returns total items in Adapter
    override fun getItemCount(): Int {
        return streams.size
    }

    // Holds an instance to the view for re-use
    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val titleView: TextView = view.findViewById(R.id.title)
        val userView: TextView = view.findViewById(R.id.userName)
        val thumbnailView: ImageView = view.findViewById(R.id.thumbnail)
    }
}