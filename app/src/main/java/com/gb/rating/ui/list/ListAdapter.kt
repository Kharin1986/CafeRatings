package com.gb.rating.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gb.rating.models.CafeItem
import kotlinx.android.synthetic.main.fragment_list_item.view.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException

class ListAdapter : RecyclerView.Adapter<ListAdapter.ListHolder>() {

    private var cafeItems : List<CafeItem> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListHolder {
        return ListHolder(LayoutInflater.from(parent.context).inflate(com.gb.rating.R.layout.fragment_list_item,
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

            // установка картинки с Firebase Storage через реквизит cafeItem.cafeId
            setImage(item)
       }

        // установка картинки с Firebase Storage через реквизит cafeItem.cafeId
        private fun View.setImage(item: CafeItem) {
            if (item.cafeId != "") {
                try {
                    val storageReference = FirebaseStorage.getInstance().getReference()
                        .child("cafeImages/" + item.cafeId + "/main.jpg")
                    storageReference.downloadUrl.addOnSuccessListener { uri ->
                        var imageURL = uri.toString()
                        Glide.with(this).load(imageURL).into(imageList_FragmentItem)
                    }.addOnFailureListener {
                        // Handle any errors
                    }
                } catch (s: StorageException) {
                }

                // можно использовать все это с помощью GlideApp в Kotlin (в Java - просто через Glide.load(storageReference).into(imageList_FragmentItem)), но мне ... не удалось ... GlideApp не появился.
                // https://medium.com/@egemenhamutcu/displaying-images-from-firebase-storage-using-glide-for-kotlin-projects-3e4950f6c103

                //val storage = FirebaseStorage.getInstance()
                //val gsReference = storage.getReferenceFromUrl("gs://fir-test-81397.appspot.com/cafeImages/cloud.jpg")
                //GlideApp.with(context)
                //.load(gsReference)
                //.into(imageList_FragmentItem)
            }
        }
    }

}