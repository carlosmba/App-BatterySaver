package com.mendozasolutions.batterysaver.domain.repository.home

import android.app.usage.UsageStats
import android.content.Context

interface AppRepo {

    fun getAppsProgressInfo(context : Context) : List<UsageStats>

    fun killAppProcess(context : Context, packageName : String)

}