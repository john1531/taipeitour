package com.mobile.taipeitour.view.attraction

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
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.mobile.taipeitour.R
import com.mobile.taipeitour.model.attraction.AttractionData
import com.mobile.taipeitour.utils.LoadingUtil
import com.mobile.taipeitour.utils.PrefUtil
import com.mobile.taipeitour.viewmodel.attraction.AttractionViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [AttractionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AttractionFragment : Fragment() {
    var v: View? = null

    private var attractionViewModel: AttractionViewModel? = null
    private var attractionsData: MutableList<AttractionData> = mutableListOf()

    private var loadingDialog: AlertDialog? = null

    var reload = false
    var currentPage = 1
    val langsKey = arrayOf("zh-tw", "zh-cn", "en", "ja", "ko", "es", "id", "th", "vi")
    val langsDesc = arrayOf("正體中文", "簡體中文", "英文", "日文", "韓文", "西班牙文", "印尼文", "泰文", "越南文")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_attraction, container, false)

        attractionViewModel = ViewModelProvider(this).get(AttractionViewModel::class.java)
        attractionViewModel!!.attractionsData.observe(viewLifecycleOwner) {

            this.loadingDialog?.cancel()

            attractionViewModel?.let {
                val attractionsData = it.attractionsData.value
                attractionsData?.let { data ->
                    this.activity?.let { act ->
                        if (reload) {
                            this.attractionsData.clear()
                        }
                        this.attractionsData.addAll(data)
                        val attractionAdapter = AttractionAdapter(act, this.attractionsData)
                        root.findViewById<RecyclerView>(R.id.list_attraction)?.layoutManager = LinearLayoutManager(act)
                        root.findViewById<RecyclerView>(R.id.list_attraction)?.adapter = attractionAdapter
                    }
                }
            }
        }

        root.findViewById<TextView>(R.id.textTitle).text = "TaipeiTour"
        root.findViewById<Button>(R.id.btnBack).visibility = View.INVISIBLE
        root.findViewById<Button>(R.id.btnLanguage).setOnClickListener {
            showLanguageChoiceDialog()
        }
        root.findViewById<RecyclerView>(R.id.list_attraction).addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {

                    reload = false
                    currentPage += 1
                    fetch()
                }
            }
        })
        root.findViewById<SwipeRefreshLayout>(R.id.container).setOnRefreshListener {
            root.findViewById<SwipeRefreshLayout>(R.id.container).isRefreshing = false

            reload = true
            currentPage = 1
            fetch()
        }

        return root
    }

    override fun onStart() {
        super.onStart()

        this.fetch()
    }

    private fun fetch() {

        this.activity?.let {

            this.loadingDialog = LoadingUtil.getInstance(it, "讀取中...請稍候")
            this.loadingDialog?.show()

            var language = PrefUtil.getPref(it, "language") ?: "zh-tw"
            val paramData = mapOf<String, Any>(
                "lang" to language,
                "page" to currentPage
            )
            attractionViewModel?.let { vm ->
                vm.fetchData(paramData)
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

                    this.fetch()
                }
                .setNegativeButton("取消") { dialog: DialogInterface, which: Int ->
                    dialog.dismiss()
                }

            builder.create().show()
        }
    }
}