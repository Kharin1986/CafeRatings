package com.gb.rating.ui.list

import com.gb.rating.R
import com.gb.rating.models.CafeItem

object TempCafeList {

    fun getCafeList() = listOf(
        CafeItem(R.drawable.cafe, "Пора покушать", "фастфуд","Пельмени ватрушки на заказ", 1, "Россия", "Москва","Лениа", "5","1255455","15:15",2.0),
        CafeItem(R.drawable.cafe, "Забегаловка", "фастфуд","Быстрые обеды, могут налить кофе, а могут и не налить", 2, "Россия", "Москва","Лениа", "5","1255455","15:15",3.0),
        CafeItem(R.drawable.cafe, "Супер бар", "фастфуд","Микро блюда и микро напитки, супер нано еда от чубайса", 3, "Россия", "Москва","Лениа", "5","1255455","15:15",15.3),
        CafeItem(R.drawable.cafe, "Маэстро", "фастфуд","Еда под музыку легче заходит и выходит", 4, "Россия", "Москва","Лениа", "5","1255455","15:15",12.0),
        CafeItem(R.drawable.cafe, "Пора покушать", "фастфуд","Пельмени ватрушки на заказ", 5, "Россия", "Москва","Лениа", "5","1255455","15:15",2.0),
        CafeItem(R.drawable.cafe, "Магнит", "фастфуд","Так и манит к нам поесть, у нас есть в котлете шерсть", 3, "Россия", "Москва","Лениа", "5","1255455","15:15",18.0),
        CafeItem(R.drawable.cafe, "Вилка ложка", "фастфуд","У нас не едят руками, вилки и ложки всегда в ниличии", 2, "Россия", "Москва","Лениа", "5","1255455","15:15",7.0)
    )
}