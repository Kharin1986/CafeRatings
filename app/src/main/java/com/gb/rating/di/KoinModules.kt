package com.gb.rating.di

import com.gb.rating.ui.ViewModelMain
import com.gb.rating.ui.fireBaseDemo.ui.MainViewModel
import com.gb.rating.ui.list.ListViewModel
import com.google.firebase.database.FirebaseDatabase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {

    single { FirebaseDatabase.getInstance()} bind FirebaseDatabase::class
}

val main = module {
    viewModel {ViewModelMain()}
}

val listFragment = module {
    viewModel {ListViewModel()}
}
