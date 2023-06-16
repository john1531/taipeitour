package com.mobile.taipeitour.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.gson.Gson
import com.mobile.taipeitour.R
import com.mobile.taipeitour.api.RequestManager
import com.mobile.taipeitour.model.AttractionData
import com.mobile.taipeitour.model.Attractions
import com.mobile.taipeitour.utils.LoadingUtil
import com.mobile.taipeitour.utils.PrefUtil

private const val ARG_ATTRACTION = "attractions"

/**
 * A simple [Fragment] subclass.
 * Use the [AttractionInfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AttractionInfoFragment : Fragment() {
    var v: View? = null

    private lateinit var attraction: AttractionData
    private lateinit var textTitle: TextView
    private lateinit var btnBack: Button
    private lateinit var btnLanguage: Button

    private lateinit var viewPager: ViewPager2
    private lateinit var textAttractionTitle: TextView
    private lateinit var textAttractionDesc: TextView
    private lateinit var textAttractionUrl: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            attraction = Gson().fromJson(it.getString(ARG_ATTRACTION).toString(), AttractionData::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_attraction_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        this.v = view
        super.onViewCreated(view, savedInstanceState)

        this.textTitle = view.findViewById(R.id.textTitle)
        this.textTitle.text = attraction.name
        this.btnBack = view.findViewById(R.id.btnBack)
        this.btnBack.setOnClickListener {
            this.activity?.let {
                it.supportFragmentManager?.popBackStack()
            }
        }
        this.btnLanguage = view.findViewById(R.id.btnLanguage)
        this.btnLanguage.visibility = View.INVISIBLE

        this.activity?.let {
            val listOfImage: MutableList<String> = mutableListOf()
            attraction.images.forEach {
                listOfImage.add("${attraction.images[0].src}${attraction.images[0].ext}")
            }
            this.viewPager = view.findViewById(R.id.viewPager)
            val adapter = ImageAdapter(it, listOfImage)
            this.viewPager.adapter = adapter
        }

        this.textAttractionTitle = view.findViewById(R.id.attraction_title)
        this.textAttractionTitle.text = attraction.name
        this.textAttractionDesc = view.findViewById(R.id.attraction_desc)
        this.textAttractionDesc.text = attraction.introduction
        this.textAttractionUrl = view.findViewById(R.id.attraction_url)
        this.textAttractionUrl.text = attraction.url
    }

    companion object {
        @JvmStatic
        fun newInstance(attractionString: String) =
            AttractionInfoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_ATTRACTION, attractionString)
                }
            }
    }
}