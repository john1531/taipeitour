package com.mobile.taipeitour.utils

import android.app.Activity
import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog

class LoadingUtil {

    companion object {

        private var instance: AlertDialog? = null
        private var act:Activity? = null
        private var title:String = ""

//        @Synchronized
//        private fun createInstance() {
//            if (instance == null) {
//                instance = setProgressDialog()
//            }
//        }

        fun getInstance(act:Activity, title:String): AlertDialog? {
            this.act = act
            this.title = title
            if (this.instance == null) {
                this.instance = setProgressDialog()
            }
            return this.instance
        }

        fun setProgressDialog() : AlertDialog{

            // Creating a Linear Layout
            val llPadding = 30
            val ll = LinearLayout(act)
            ll.orientation = LinearLayout.HORIZONTAL
            ll.setPadding(llPadding, llPadding, llPadding, llPadding)
            ll.gravity = Gravity.CENTER
            var llParam = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            llParam.gravity = Gravity.CENTER
            ll.layoutParams = llParam

            // Creating a ProgressBar inside the layout
            val progressBar = ProgressBar(act)
            progressBar.isIndeterminate = true
            progressBar.setPadding(0, 0, llPadding, 0)
            progressBar.layoutParams = llParam
            llParam = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            llParam.gravity = Gravity.CENTER

            // Creating a TextView inside the layout
            val tvText = TextView(act)
            tvText.text = title
            tvText.setTextColor(Color.parseColor("#000000"))
            tvText.textSize = 15f
            tvText.layoutParams = llParam
            ll.addView(progressBar)
            ll.addView(tvText)

            // Setting the AlertDialog Builder view
            // as the Linear layout created above
            val builder: AlertDialog.Builder = AlertDialog.Builder(act!!)
            builder.setCancelable(true)
            builder.setView(ll)

            // Displaying the dialog
            val dialog: AlertDialog = builder.create()
//            dialog.show()

            val window: Window? = dialog.window
            if (window != null) {
                val layoutParams = WindowManager.LayoutParams()
                layoutParams.copyFrom(dialog.window?.attributes)
                layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
                layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
                dialog.window?.attributes = layoutParams

                // Disabling screen touch to avoid exiting the Dialog
                window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }

            return dialog
        }

    }
}