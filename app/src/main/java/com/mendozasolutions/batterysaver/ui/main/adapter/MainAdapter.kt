package com.mendozasolutions.batterysaver.ui.main.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mendozasolutions.batterysaver.databinding.AppItemBinding
import com.mendozasolutions.batterysaver.R
import com.mendozasolutions.batterysaver.core.BaseViewHolder
import com.mendozasolutions.batterysaver.data.model.App
import com.mendozasolutions.batterysaver.ui.main.OnClickAppListener
import java.text.SimpleDateFormat
import java.util.*

class MainAdapter( private val listener : OnClickAppListener) : RecyclerView.Adapter<BaseViewHolder<*>>() {
    private val TAG = "HomeAdapter"
    private var listApp = mutableListOf<App>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return HomeViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.app_item, parent, false))

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
    fun getListApp() : List<App> = listApp.toList()


    fun setListApp(list : List<App>){
        listApp.addAll(list)
        notifyDataSetChanged()
    }


    fun toggleCheckBoxAll(isCheck : Boolean){
        listApp.forEach { app ->
            app.isCheck = isCheck
        }
        notifyDataSetChanged()
    }

    private fun updateCheckBox(item : App){
        val index = listApp.indexOf(item)
        val check = !item.isCheck
        listApp[index].isCheck = check
        listener.onClickCheckBoxApp(check)

    }

    fun removeAppList(app : App){
        val index = listApp.indexOf(app)
        listApp.removeAt(index)
        notifyItemRemoved(index)
    }

    inner class HomeViewHolder(mView : View) : BaseViewHolder<App>(mView){
        private val binding = AppItemBinding.bind(mView)
        override fun bind(item: App) {
                binding.imgApp.setImageDrawable(item.photoDrawable)
                binding.tvNameApp.text = item.name
                binding.checkBoxApp.isChecked = listApp[listApp.indexOf(item)].isCheck
            Log.i(TAG, "${ item.name } - ${ item.lastTimeUsed }")

        }
        fun setupListeners(app : App){
            binding.checkBoxApp.setOnClickListener {
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