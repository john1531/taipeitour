package com.mobile.taipeitour.view.attraction

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.disk.DiskCache
import coil.load
import coil.memory.MemoryCache
import com.google.gson.Gson
import com.mobile.taipeitour.MainActivity
import com.mobile.taipeitour.R
import com.mobile.taipeitour.model.attraction.AttractionData

class AttractionAdapter(act: FragmentActivity, private val arrAttractions: List<AttractionData>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val activity: Activity = act
    var imageLoader: ImageLoader? = null

    class ViewHolderPlace(itemView: View): RecyclerView.ViewHolder(itemView){

        var image: ImageView? = null
        var txtTitle: TextView? = null
        var txtDesc: TextView? = null
        var linearSpace: LinearLayout? = null

        init {
            // Define click listener for the ViewHolder's View.
            image = itemView.findViewById(R.id.attraction_image)
            txtTitle = itemView.findViewById(R.id.attraction_title)
            txtDesc = itemView.findViewById(R.id.attraction_desc)
            linearSpace = itemView.findViewById(R.id.layout_space_cell)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {


        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cell_attraction, parent, false)

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

        return ViewHolderPlace(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        with(holder as ViewHolderPlace){
            val dataAttraction = arrAttractions[position]

            txtTitle?.text = dataAttraction.name
            txtDesc?.text = dataAttraction.introduction

            if(dataAttraction.images.isNotEmpty()) {
                imageLoader?.let { il ->
                    image?.load("${dataAttraction.images.first().src}${dataAttraction.images.first().ext}", il){
                        listener(
                            onError = { _, _ ->

                            }
                        )
                    }
                }
            }

            linearSpace?.setOnClickListener(View.OnClickListener { it: View ->

                val fragmentMenuManager = MainActivity.main?.supportFragmentManager?.beginTransaction()
                val fg = AttractionInfoFragment.newInstance(
                    Gson().toJson(dataAttraction)
                )
                fragmentMenuManager?.replace(R.id.content_fragment, fg, "")
                fragmentMenuManager?.addToBackStack(null)
                fragmentMenuManager?.commit()

            })
        }
    }

    override fun getItemCount(): Int {
        return arrAttractions.size
    }


}