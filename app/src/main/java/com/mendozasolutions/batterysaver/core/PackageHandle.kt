package com.mendozasolutions.batterysaver.core

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable

class PackageHandle (private val context : Context){
    private val packageName : String = ""
    private val manager : PackageManager = context.packageManager

    fun getAppIcon() : Drawable = manager.getApplicationIcon(packageName)

    fun getAppLabel() : String = manager.getApplicationLabel(getAppInfo(packageName)).toString()

    fun getSourceDir() : String = getAppInfo(packageName).sourceDir

    fun getFlags() : Int = getAppInfo(packageName).flags

    private fun getAppInfo(packageName: String): ApplicationInfo = manager.getApplicationInfo(this.packageName, 0)


}