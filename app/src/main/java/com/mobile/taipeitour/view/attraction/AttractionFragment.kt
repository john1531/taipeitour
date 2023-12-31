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

        root.findViewById<TextView>(R.id.textTitle).text = getString(R.string.app_en_name)
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

            this.loadingDialog = LoadingUtil.getInstance(it, getString(R.string.loading))
            this.loadingDialog?.show()

            var language = PrefUtil.getPref(it, "language") ?: getString(R.string.lang_code_zhtw)
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

        val langsKey = arrayOf(
            getString(R.string.lang_code_zhtw),
            getString(R.string.lang_code_zhcn),
            getString(R.string.lang_code_en),
            getString(R.string.lang_code_ja),
            getString(R.string.lang_code_ko),
            getString(R.string.lang_code_es),
            getString(R.string.lang_code_id),
            getString(R.string.lang_code_th),
            getString(R.string.lang_code_vi))
        val langsDesc = arrayOf(
            getString(R.string.lang_text_zhtw),
            getString(R.string.lang_text_zhcn),
            getString(R.string.lang_text_en),
            getString(R.string.lang_text_ja),
            getString(R.string.lang_text_ko),
            getString(R.string.lang_text_es),
            getString(R.string.lang_text_id),
            getString(R.string.lang_text_th),
            getString(R.string.lang_text_vi))

        this.activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(getString(R.string.select_language))
                .setSingleChoiceItems(langsDesc, selectedLang) { dialog: DialogInterface, which: Int ->
                    selectedLang = which
                }
                .setPositiveButton(getString(R.string.confirm)) { dialog: DialogInterface, which: Int ->
                    val selectedOption = langsKey[selectedLang]

                    PrefUtil.setPref(it, "language", selectedOption)

                    reload = true
                    this.fetch()
                }
                .setNegativeButton(getString(R.string.cancel)) { dialog: DialogInterface, which: Int ->
                    dialog.dismiss()
                }

            builder.create().show()
        }
    }
}