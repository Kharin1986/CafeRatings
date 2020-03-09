package com.gb.rating.ui.cafeInfo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gb.rating.R
import com.gb.rating.models.CafeReviewItem
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.fragment_cafe_info_item.view.*
import kotlinx.android.synthetic.main.user_item.view.*


class CafeInfoAdapter : RecyclerView.Adapter<CafeInfoAdapter.CafeInfoHolder>() {

    private var cafeReviewsItems : List<CafeReviewItem> = ArrayList()

    fun refreshList(list : List<CafeReviewItem>) {
        this.cafeReviewsItems = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CafeInfoHolder {
        return CafeInfoHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.fragment_cafe_info_item,
                parent, false))
    }

    override fun getItemCount(): Int = cafeReviewsItems.size

    override fun onBindViewHolder(holder: CafeInfoHolder, position: Int) {
       holder.bind(cafeReviewsItems[position])
    }

    inner class CafeInfoHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind (item: CafeReviewItem) = with(itemView) {
            user_card.tv_user_rating.text = item.userRating.toString()
            ratingbar_all.rating = item.cafeRating
            tv_cafe_review.text = item.reviewText
        }
    }
}