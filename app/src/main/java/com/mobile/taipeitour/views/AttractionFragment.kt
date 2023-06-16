package com.mobile.taipeitour.views

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.mobile.taipeitour.R
import com.mobile.taipeitour.api.RequestManager
import com.mobile.taipeitour.model.Attractions
import com.mobile.taipeitour.utils.LoadingUtil
import com.mobile.taipeitour.utils.PrefUtil

/**
 * A simple [Fragment] subclass.
 * Use the [AttractionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AttractionFragment : Fragment() {
    var v: View? = null

    private var loading: AlertDialog? = null
    private lateinit var textTitle: TextView
    private lateinit var btnBack: Button
    private lateinit var btnLanguage: Button

    //PrefUtil.setPref(c, "loginPassword", loginPassword)
    //PrefUtil.getPref(c, "loginPassword")

    val langsKey = arrayOf("zh-tw", "zh-cn", "en", "ja", "ko", "es", "id", "th", "vi")
    val langsDesc = arrayOf("正體中文", "簡體中文", "英文", "日文", "韓文", "西班牙文", "印尼文", "泰文", "越南文")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_attraction, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        this.v = view
        super.onViewCreated(view, savedInstanceState)

        this.textTitle = view.findViewById(R.id.textTitle)
        this.textTitle.text = "TaipeiTour"
        this.btnBack = view.findViewById(R.id.btnBack)
        this.btnBack.visibility = View.INVISIBLE
        this.btnLanguage = view.findViewById(R.id.btnLanguage)
        this.btnLanguage.setOnClickListener {
            showLanguageChoiceDialog()
        }

        requestAttractionsData()
    }

    private fun requestAttractionsData(){

        this.activity?.let {
            this.loading = LoadingUtil.getInstance(it, "資料讀取中...請稍候")
            this.loading?.show()

            RequestManager().requestAttractions(it, PrefUtil.getPref(it, "language") ?: "zh-tw") { result: String ->
                this.loading?.cancel()

                var data = Gson().fromJson(result, Attractions::class.java)

                val attractionAdapter = AttractionAdapter(it, data)
                this.v?.findViewById<RecyclerView>(R.id.list_attraction)?.layoutManager =
                    LinearLayoutManager(it)
                this.v?.findViewById<RecyclerView>(R.id.list_attraction)?.adapter = attractionAdapter
            }
        }
    }

    private fun showLanguageChoiceDialog() {
        var selectedLang = 0

        this.activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("請選擇顯示語言")
                .setSingleChoiceItems(langsDesc, selectedLang) { dialog: DialogInterface, which: Int ->
                    selectedLang = which
                }
                .setPositiveButton("確定") { dialog: DialogInterface, which: Int ->
                    val selectedOption = langsKey[selectedLang]

                    PrefUtil.setPref(it, "language", selectedOption)
                    requestAttractionsData()
                }
                .setNegativeButton("取消") { dialog: DialogInterface, which: Int ->
                    dialog.dismiss()
                }

            builder.create().show()
        }
    }
}