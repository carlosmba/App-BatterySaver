package com.mendozasolutions.batterysaver.domain.repository.main

import android.app.usage.UsageStats
import android.content.Context

interface AppRepo {

    fun getAppsProgressInfo(context : Context) : List<UsageStats>

}