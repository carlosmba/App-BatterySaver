package com.mendozasolutions.batterysaver.domain.services

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Intent
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.mendozasolutions.batterysaver.ui.main.MainActivity

class MyAccessibilityService : AccessibilityService() {
    private val TAG = "MyAccessibilityService"
    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.i(TAG, "Conectado")
        val info = AccessibilityServiceInfo()

        info.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED

        info.packageNames = arrayOf("com.android.settings")

        info.feedbackType = AccessibilityServiceInfo.DEFAULT

        info.notificationTimeout = 100

        serviceInfo = info

    }



    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        Log.i(TAG, "Entrando onAccessibilityEvent " + event?.eventType)
        Log.i(TAG, "isHibernation ${ConfigHibernationProcess.isHibernation}")
        if(ConfigHibernationProcess.isHibernation && AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED == event?.eventType){
            val nodeInfo = event.source
            Log.i(TAG, "Cambio de ventana onAccessibilityEvent: nodeInfo=$nodeInfo")
            if(nodeInfo == null) return
            var list = nodeInfo.findAccessibilityNodeInfosByViewId("com.android.settings:id/right_button")
            if(list.isNotEmpty()){
                list.forEach { node ->
                    Log.i(TAG, "ACC::onAccessibilityEvent: right_button " + node);
                    node.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                }
                Log.i(TAG,"tamaño de la lista 1 $list" )
            }
            list = nodeInfo.findAccessibilityNodeInfosByViewId("android:id/button1")
            if(list.isNotEmpty()){
                val intent = Intent(this, MainActivity::class.java)
                list.forEach { node ->
                    Log.i(TAG, "ACC::onAccessibilityEvent: button1 " + node);
                    node.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                    startActivity(intent)
                }
                Log.i(TAG,"tamaño de la lista 2 $list" )
            }


        }
    }

    override fun onInterrupt() {
        Log.i(TAG, "Servicio interrumpido")
    }


}