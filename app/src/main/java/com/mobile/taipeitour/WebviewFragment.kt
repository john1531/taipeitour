package com.mobile.taipeitour

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.os.Message
import android.view.View
import android.webkit.*
import android.webkit.WebView.WebViewTransport
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.mobile.taipeitour.utils.LoadingUtil
import java.util.*


open class WebviewFragment : Fragment() {

    private var v: View? = null
    private var web: WebView? = null
    var urlStr: String = ""
    var btnBack: Button? = null
    var loadingDialog: AlertDialog? = null
    var alwaysShowBack: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        this.v = view
        this.web = view.findViewById<WebView>(R.id.fg_web)!!

        this.btnBack = view.findViewById<Button>(R.id.btnBack)

        if (alwaysShowBack) this.btnBack?.visibility = View.VISIBLE else this.btnBack?.visibility =
            View.GONE
        this.btnBack?.setOnClickListener(View.OnClickListener { it: View ->
            if (this.web?.canGoBack() == true) {
                this.web?.goBack()
            } else {
                if (alwaysShowBack) {
                    activity?.supportFragmentManager?.popBackStack()
                }
            }
        })

        this.web!!.webViewClient = WebViewClient()

        with(this.web!!.settings) {
            javaScriptEnabled = true
            setSupportMultipleWindows(true)
            domStorageEnabled = true
            allowFileAccess = true
            setSupportZoom(true)
            builtInZoomControls = true
            loadWithOverviewMode = true
            useWideViewPort = true
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            }
            textZoom = 100
            javaScriptCanOpenWindowsAutomatically = true
        }

        web!!.webChromeClient = object : WebChromeClient() {
            override fun onCreateWindow(
                view: WebView,
                dialog: Boolean,
                userGesture: Boolean,
                resultMsg: Message
            ): Boolean {
                val newWebView = WebView(view.context)
                newWebView.webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {

                        val i = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        startActivity(i)

                        return false
                    }
                }
                val transport = resultMsg.obj as WebViewTransport
                transport.webView = newWebView
                resultMsg.sendToTarget()
                return true
            }
        }

        this.web!!.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return super.shouldOverrideUrlLoading(view, request)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                if (url != null) {
                    urlStr = url
                }
                loadingDialog?.cancel()
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                if (view != null) {
                    if (view.canGoBack()) {
                        btnBack?.visibility = View.VISIBLE
                    } else {
                        if (alwaysShowBack) {
                            btnBack?.visibility = View.VISIBLE
                        } else {
                            btnBack?.visibility = View.GONE
                        }
                    }

                }
                activity?.let {
                    loadingDialog = LoadingUtil.getInstance(it, "讀取中...請稍候")
                    loadingDialog?.show()
                }

            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
            }

            override fun onReceivedSslError(
                view: WebView?,
                handler: SslErrorHandler?,
                error: SslError?
            ) {
                val sslCertificate = error!!.certificate

                val builder = AlertDialog.Builder(
                    activity!!
                )
                builder.setTitle("SSL 憑證錯誤")
                builder.setMessage("無法驗證伺服器SSL憑證。\n仍要繼續嗎?")
                builder.setPositiveButton(
                    "繼續"
                ) { dialog, which -> handler!!.proceed() }
                builder.setNegativeButton(
                    "取消"
                ) { dialog, which -> handler!!.cancel() }

                val dialog = builder.create()
                dialog.show()
            }
        }

        web!!.loadUrl(this.urlStr)
    }

    override fun onResume() {
        super.onResume()
    }
}
