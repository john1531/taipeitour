package com.mobile.taipeitour.utils

import android.content.Context

class PrefUtil {

    companion object {
        private const val SHARE_NAME: String = "taipeitour"
        fun setPref(context: Context?, key: String, value: String) {
            if (context == null) {
                return
            }

            val pref = context.getSharedPreferences(SHARE_NAME, Context.MODE_PRIVATE)
            val editor = pref.edit()
            editor.putString(key, value).commit()
        }

        fun getPref(context: Context?, key: String): String? {
            if (context == null) {
                return null
            }

            val pref = context.getSharedPreferences(SHARE_NAME, Context.MODE_PRIVATE)
            return pref.getString(key, null)
        }

        fun rmPref(context: Context?, key: String){
            val preferences = context?.getSharedPreferences(SHARE_NAME, Context.MODE_PRIVATE)
            val editor = preferences?.edit()
            editor?.remove(key)
            editor?.apply()
        }

    }


}