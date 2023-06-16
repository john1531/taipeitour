package com.mobile.taipeitour.views
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.disk.DiskCache
import coil.load
import coil.memory.MemoryCache
import com.mobile.taipeitour.R

class ImageAdapter(act: FragmentActivity, private val images: List<String>) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    val activity: Activity = act
    var imageLoader: ImageLoader? = null

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_image, parent, false)

        activity.let { it->
            imageLoader = ImageLoader.Builder(it)
                .memoryCache {
                    MemoryCache.Builder(it)
                        .maxSizePercent(0.25)
                        .build()
                }
                .diskCache {
                    DiskCache.Builder()
                        .directory(it.cacheDir.resolve("image_cache"))
                        .maxSizePercent(0.02)
                        .build()
                }
                .build()
        }

        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageUrl = images[position]
        imageLoader?.let { il ->
            holder.imageView?.load(imageUrl, il){
                listener(
                    onError = { _, _ ->

                    }
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return images.size
    }
}