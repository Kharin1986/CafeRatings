package com.gb.rating.ui.cafeInfo

import com.gb.rating.models.CafeReviewItem

object TempReviewList {
    fun getReviewList() = listOf(
        CafeReviewItem("222",25,"Все остались живы", 2f),
        CafeReviewItem("223",20,"Все остались живы", 3f),
        CafeReviewItem("224",15,"Все остались живы", 4f),
        CafeReviewItem("225",100,"Все остались живы, но не все довольны. Так себе место, не хотелось бы сюда попадать еще раз", 0f),
        CafeReviewItem("226",10,"Все остались живы", 0f),
        CafeReviewItem("227",12,"Все остались живы", 1f)
    )
}