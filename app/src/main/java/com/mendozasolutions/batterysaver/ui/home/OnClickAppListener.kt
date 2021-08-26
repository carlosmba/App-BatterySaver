package com.mendozasolutions.batterysaver.ui.home

import com.mendozasolutions.batterysaver.data.model.App

interface OnClickAppListener {
    fun onClickForceCloseApp(listApp : List<App>)
    fun onClickCheckBoxAll(isCheck : Boolean)
}