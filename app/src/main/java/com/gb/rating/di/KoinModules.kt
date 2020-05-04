package com.gb.rating.di

import com.gb.rating.fireBase_RealTime.repository.Cafe_FB_Impl
import com.gb.rating.fireBase_RealTime.repository.UnverifiedRating_FB_Impl
import com.gb.rating.fireBase_RealTime.repository.VerifiedRating_FB_Impl
import com.gb.rating.models.repository.CafeRepository
import com.gb.rating.models.repository.UnverifiedRatingRepository
import com.gb.rating.models.repository.VerifiedRatingRepository
import com.gb.rating.models.usercase.CafeInteractor
import com.gb.rating.models.usercase.UnverifiedRatingInteractor
import com.gb.rating.models.usercase.VerifiedRatingInteractor
import com.gb.rating.ui.ViewModelMain
import com.gb.rating.ui.cafeInfo.CafeInfoViewModel
import com.gb.rating.ui.home.HomeViewModel
import com.gb.rating.ui.list.ListViewModel
import com.gb.rating.ui.review.ReviewSharedViewModel
import com.gb.rating.ui.search.SearchViewModel
import com.gb.rating.ui.settings.SettingsViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {

    single { FirebaseDatabase.getInstance()} bind FirebaseDatabase::class
    single { FirebaseAuth.getInstance()}

    single { Cafe_FB_Impl(get(), null) } bind CafeRepository::class
    single {CafeInteractor(get())}

    single {UnverifiedRating_FB_Impl(get(), null)} bind UnverifiedRatingRepository::class
    single {UnverifiedRatingInteractor(get())}

    single {VerifiedRating_FB_Impl(get(), null)} bind VerifiedRatingRepository::class
    single {VerifiedRatingInteractor(get())}


}

val main = module {
    viewModel {ViewModelMain(get())}
}

val listFragment = module {
    viewModel {ListViewModel()}
}

val cafeInfo = module {
 //   viewModel {CafeInfoViewModel(get())} // убрано для демонстрации 27.02 Kharin
    viewModel {CafeInfoViewModel()}
}

val home = module {
    viewModel {HomeViewModel()}
}

//val review = module {
//    viewModel {ReviewSharedViewModel(get())} bind ReviewSharedViewModel::class
//}


val search = module {
    viewModel {SearchViewModel()}
}

val settings = module {
    viewModel {SettingsViewModel()}
}
