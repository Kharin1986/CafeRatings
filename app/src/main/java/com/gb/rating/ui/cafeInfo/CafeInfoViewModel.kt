package com.gb.rating.ui.cafeInfo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gb.rating.models.CafeReviewItem
import com.gb.rating.models.usercase.VerifiedRatingInteractor

//class CafeInfoViewModel(private val verifiedRatingInteractor : VerifiedRatingInteractor) : ViewModel() { //27.02 Kharin - для тестового списка
class CafeInfoViewModel : ViewModel() {
    val viewState = MutableLiveData<String>()
    private var reviewList : MutableLiveData<List<CafeReviewItem>> = MutableLiveData()
    init {
        reviewList.value = TempReviewList.getReviewList()
    }
    fun getReviewList() = reviewList
}
