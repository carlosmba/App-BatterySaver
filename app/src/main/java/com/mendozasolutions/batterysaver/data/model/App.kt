package com.mendozasolutions.batterysaver.data.model

import android.graphics.drawable.Drawable

data class App(val photoDrawable : Drawable, val name : String,val packageName : String, val sourceDir : String, var isCheck : Boolean = true){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as App

        if (packageName != other.packageName) return false

        return true
    }

    override fun hashCode(): Int {
        return packageName.hashCode()
    }
}
