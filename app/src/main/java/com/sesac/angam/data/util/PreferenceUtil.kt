package com.sesac.angam.data.util

import android.content.Context
import android.content.SharedPreferences

class PreferenceUtil (context: Context){

    // SharedPreferences 인스턴스 생성 : 싱글턴 패턴
    val prefs: SharedPreferences =
        context.getSharedPreferences("prefs_name", Context.MODE_PRIVATE)

    fun getString(key: String, defValue: String): String {
        return prefs.getString(key, defValue).toString()
    }

    fun setString(key: String, str: String) {
        prefs.edit().putString(key, str).apply()
    }

    fun saveArrayList(key: String, arrayList: ArrayList<String>) {
        val editor = prefs.edit()
        editor.putStringSet(key, HashSet(arrayList))
        editor.apply()
    }

    fun getArrayList(key: String): ArrayList<String> {
        val savedSet = prefs.getStringSet(key, HashSet<String>())
        return ArrayList(savedSet)
    }

    fun getSharedPreferences(): SharedPreferences {
        return prefs
    }


}