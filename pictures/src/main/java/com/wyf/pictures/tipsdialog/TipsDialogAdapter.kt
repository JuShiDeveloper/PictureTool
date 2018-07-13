package com.base.muslim.tipsdialog

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

import com.wyf.pictures.R


class TipsDialogAdapter(private val data: List<String>, private val mContext: Context) : BaseAdapter() {

    override fun getItemId(p0: Int): Long = 0L


    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(position: Int): Any {
        return data[position]
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        var view = convertView
        var holder: TipsViewHolder
        if (view == null) {
            view = View.inflate(mContext, R.layout.exit_dialog_hint_content_item, null)
            holder = TipsViewHolder(view)
            view.tag = holder
        } else {
            holder = view.tag as TipsViewHolder
        }
        holder.tView.text = data[position]
        return view
    }

    internal inner class TipsViewHolder(itemView: View) {
        val tView: TextView = itemView as TextView
    }
}
