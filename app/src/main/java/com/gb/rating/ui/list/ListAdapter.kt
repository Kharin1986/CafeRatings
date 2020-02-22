package com.gb.rating.ui.list

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.navigation.fragment.FragmentNavigator
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gb.rating.MainActivity
import com.gb.rating.R
import com.gb.rating.models.CafeItem
import com.gb.rating.ui.review.ReviewFragment
import kotlinx.android.synthetic.main.fragment_list_item.view.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException

class ListAdapter(val onItemClick: ((CafeItem)-> Unit)? = null) : RecyclerView.Adapter<ListAdapter.ListHolder>(){

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

    inner class ListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind (item: CafeItem) = with(itemView) {
            nameCafe_FragmentList.text = item.name
            address_FragmentList.text = buildAddress(item)
            descriptionCafe_FragmentList.text = item.desc
            distanceToCafe_FragmentList.text = "${item.distance.toString()} км" //км нужно запихать в строковые ресурсы
            ratingBar_FragmentList.progress = item.rating
            heartselector.isChecked = item.fav

            // установка картинки с Firebase Storage через реквизит cafeItem.cafeId
            setImage(item)

            itemView.setOnClickListener { onItemClick?.invoke(item) }
       }

        private fun buildAddress (item: CafeItem) : String {
            val sb = java.lang.StringBuilder()
            sb.append("ул. ")
                .append(item.street)
//                .append(", ")
//                .append(item.home)

            return sb.toString()
        }


        // установка картинки с Firebase Storage через реквизит cafeItem.cafeId
        private fun View.setImage(item: CafeItem) {
            if (item.cafeId != "") {
                try {
                    val storageReference = FirebaseStorage.getInstance().getReference()
                        .child("cafeImages/" + item.cafeId + "/main.jpg")
                    storageReference.downloadUrl.addOnSuccessListener { uri ->
                        val imageURL = uri.toString()
                        Glide.with(this).load(imageURL).into(imageList_FragmentItem)
                    }.addOnFailureListener {
                        // Handle any errors
                    }
                } catch (s: StorageException) {
                }
            }
        }
    }

}