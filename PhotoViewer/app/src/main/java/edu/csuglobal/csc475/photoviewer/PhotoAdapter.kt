package edu.csuglobal.csc475.photoviewer

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class PhotoAdapter(private val photos: List<String>) : RecyclerView.Adapter<PhotoAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.photo_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_photo, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val photoUri = photos[position]
        
        // Load thumbnail with Glide using the recommended approach
        Glide.with(holder.itemView.context)
            .load(photoUri)
            .apply(RequestOptions()
                .override(200, 200) // Specify a smaller size for thumbnails
                .centerCrop())
            .into(holder.imageView)
        
        // Set click listener to open the detail view
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, PhotoDetailActivity::class.java).apply {
                putExtra("PHOTO_URI", photoUri)
                putExtra("PHOTO_INDEX", position)
                putStringArrayListExtra("PHOTOS_LIST", ArrayList(photos))
            }
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount() = photos.size
}