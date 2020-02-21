package com.gb.rating.ui.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gb.rating.fireBase_RealTime.repository.Cafe_FB_Impl
import com.gb.rating.models.CafeItem
import com.gb.rating.models.usercase.CafeInteractor
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.MaybeObserver
import io.reactivex.disposables.Disposable
import java.util.ArrayList

class ListViewModel() : ViewModel() {
    val viewState = MutableLiveData<String>()
}