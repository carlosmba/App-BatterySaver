package com.mendozasolutions.batterysaver.ui.home.adapter

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.usage.UsageStats
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mendozasolutions.batterysaver.databinding.AppItemBinding
import com.mendozasolutions.batterysaver.R
import com.mendozasolutions.batterysaver.core.BaseViewHolder
import com.mendozasolutions.batterysaver.core.PackageHandle
import com.mendozasolutions.batterysaver.data.model.App
import com.mendozasolutions.batterysaver.ui.home.OnClickAppListener
import java.text.SimpleDateFormat
import java.util.*

class HomeAdapter(private val context : Context, private val listener : OnClickAppListener) : RecyclerView.Adapter<BaseViewHolder<*>>() {

    private var listApp = mutableListOf<App>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return HomeViewHolder(LayoutInflater.from(context).inflate(R.layout.app_item, parent, false))

    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {

        when(holder){
           is HomeViewHolder -> {
               holder.bind(listApp[position])
               holder.setupListeners(listApp[position])
           }
       }
    }

    override fun getItemCount(): Int = listApp.size


    fun setListApp(list : List<App>){
        listApp.addAll(list)
        notifyDataSetChanged()
    }

    fun getListApp() : List<App> = listApp.toList()

    fun toggleCheckBoxAll(){
        listApp.forEach { app ->
            app.isCheck = !app.isCheck
        }
        notifyDataSetChanged()
    }

    private fun updateCheckBox(item : App){
        val index = listApp.indexOf(item)
        listApp[index].isCheck = !item.isCheck
    }

    fun removeAppList(app : App){
        val index = listApp.indexOf(app)
        listApp.removeAt(index)
        notifyItemRemoved(index)
    }

    inner class HomeViewHolder(mView : View) : BaseViewHolder<App>(mView){
        private val binding = AppItemBinding.bind(mView)
        @SuppressLint("SetTextI18n")
        override fun bind(item: App) {
                binding.imgApp.setImageDrawable(item.photoDrawable)
                binding.tvNameApp.text = item.name
                binding.checkBoxApp.isChecked = listApp[listApp.indexOf(item)].isCheck

        }
        fun setupListeners(app : App){
            binding.checkBoxApp.setOnCheckedChangeListener { buttonView, isChecked ->
                updateCheckBox(app)
            }
        }

        private fun convertTime(lastTimeUsed : Long) : String{
            val date = Date(lastTimeUsed)
            val format = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH)
            return format.format(date)

        }


    }

}