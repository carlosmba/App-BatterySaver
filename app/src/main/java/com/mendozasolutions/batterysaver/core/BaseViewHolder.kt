package com.mendozasolutions.batterysaver.core

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<T>(mView : View) : RecyclerView.ViewHolder(mView) {
    abstract  fun bind(item : T)

}