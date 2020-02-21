package com.gb.rating.models.usercase

import com.gb.rating.dataBase.CafeDataSource
import com.gb.rating.models.CafeItem
import com.gb.rating.models.repository.CafeRepository
import com.gb.rating.models.utils.MainApplication
import io.reactivex.Completable
import io.reactivex.Maybe

class CafeInteractor(private val repository: CafeRepository) {

    @Throws(IllegalArgumentException::class)
    fun writeCafe(cafe: CafeItem): Completable {
        return repository.writeCafe(cafe)
    }


    fun retrieveCafeList(country: String, city: String): Maybe<List<CafeItem>> {
        return repository.retrieveCafeList(country, city)
    }

    fun retrieveCafeListByType(country: String, city: String, type: String): Maybe<List<CafeItem>> {
        return repository.retrieveCafeListByType(country, city, type)
    }


    fun updateInternalDatabase(country: String, city: String): Maybe<List<CafeItem>> {
        return repository.retrieveCafeList(country, city)
            .doOnSuccess { cafeItems -> writeRetrievedCafeListToLocalDatabase(cafeItems) }
    }

    private fun writeRetrievedCafeListToLocalDatabase(cafeItems: List<CafeItem>) {
        val dbHelperW =
            CafeDataSource(MainApplication.applicationContext()) //getApplicationContext()
        dbHelperW.openW()
        dbHelperW.writeCafeList(cafeItems)
    }

}
