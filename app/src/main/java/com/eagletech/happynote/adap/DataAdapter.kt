package com.eagletech.happynote.adap

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.eagletech.happynote.R
import com.eagletech.happynote.entities.Data

class DataAdapter(
    private val context: Context,
    private val onClick: (Data) -> Unit
) : RecyclerView.Adapter<DataAdapter.DataViewModel>() {
    private var datas: List<Data> = listOf()

    inner class DataViewModel(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvDataTitle: TextView = itemView.findViewById(R.id.tvDataTitle)
        private val tvDataBody: TextView = itemView.findViewById(R.id.tvDataBody)

        fun onBind(data: Data) {
            tvDataTitle.text = data.dataTitle
            tvDataBody.text = data.dataContent
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewModel {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_data, parent, false)
        return DataViewModel(itemView)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: DataViewModel, position: Int) {
        val data = datas[position]
        holder.itemView.setOnClickListener { onClick.invoke(data) }
        holder.onBind(datas[position])
    }

    fun setData(datas: List<Data>) {
        this.datas = datas
        notifyDataSetChanged()
    }
}