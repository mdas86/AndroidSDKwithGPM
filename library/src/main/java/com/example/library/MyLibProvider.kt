package com.example.library

import android.content.Context
import android.widget.Toast

object MyLibProvider {

    fun testSDK(context: Context) {
        Toast.makeText(context, "Test SDK function successfully", Toast.LENGTH_LONG).show()
    }

    fun test2SDK(context: Context) {
        Toast.makeText(context, "Test2 SDK function successfully", Toast.LENGTH_LONG).show()
    }

    fun test3SDK(context: Context) {
        Toast.makeText(context, "Test3 SDK function successfully", Toast.LENGTH_LONG).show()
    }

    fun test4SDK(context: Context) {
        Toast.makeText(context, "Test4 SDK function successfully", Toast.LENGTH_LONG).show()
    }
}