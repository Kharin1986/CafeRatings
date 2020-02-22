package com.gb.rating.models

data class CafeReviewItem  (
    var userId: String = "",
    var userRating: Int = 0,
    var reviewText: String = "",
    var cafeRating: Float = 0.0f
)