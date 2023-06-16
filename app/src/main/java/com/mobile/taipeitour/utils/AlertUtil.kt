package com.mobile.taipeitour.utils

import android.app.Activity
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog

class AlertUtil {

    companion object {
        public fun showAlert(act:Activity, title:String, msg:String,
                             left:String?, right:String?,
                             leftListen:DialogInterface.OnClickListener?, rightListen:DialogInterface.OnClickListener?)
        {
            val builder: AlertDialog.Builder = AlertDialog.Builder(act)
            with(builder) {
                setCancelable(false)
                setTitle(title)
                setMessage(msg)
                if (left != null) {
                    setNegativeButton(left, leftListen)
                }
                if (right != null) {
                    setPositiveButton(right, rightListen)
                }
                create().show()
            }
        }
    }
}