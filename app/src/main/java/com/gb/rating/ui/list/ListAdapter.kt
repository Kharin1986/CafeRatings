package com.gb.rating.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gb.rating.R
import com.gb.rating.models.CafeItem
import kotlinx.android.synthetic.main.fragment_list_item.view.*

class ListAdapter : RecyclerView.Adapter<ListAdapter.ListHolder>() {

    private var cafeItems : List<CafeItem> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListHolder {
        return ListHolder(LayoutInflater.from(parent.context).inflate(R.layout.fragment_list_item,
            parent, false))
    }

    override fun getItemCount() = cafeItems.size

    override fun onBindViewHolder(holder: ListHolder, position: Int) {
        holder.bind(cafeItems[position])
    }

    fun refreshList(list : List<CafeItem>) {
        this.cafeItems = list
        notifyDataSetChanged()
    }

    class ListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind (item: CafeItem) = with(itemView) {
            nameCafe_FragmentList.text = item.name
            descriptionCafe_FragmentList.text = item.desc
            distanceToCafe_FragmentList.text = "${item.distance.toString()} км" //км нужно запихать в строковые ресурсы
            ratingBar_FragmentList.progress = item.rating
        }
    }

}