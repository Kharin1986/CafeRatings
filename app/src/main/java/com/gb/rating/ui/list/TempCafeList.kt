package com.gb.rating.ui.list
import com.gb.rating.R
import com.gb.rating.models.CafeItem

object TempCafeList {

    fun getCafeList() = listOf(
        CafeItem(R.drawable.cafe, "Пора покушать", "Пельмени ватрушки на заказ", 1, 2.0),
        CafeItem(R.drawable.cafe, "Забегаловка", "Быстрые обеды, могут налить кофе, а могут и не налить", 2, 3.0),
        CafeItem(R.drawable.cafe, "Супер бар", "Микро блюда и микро напитки, супер нано еда от чубайса", 3, 15.3),
        CafeItem(R.drawable.cafe, "Маэстро", "Еда под музыку легче заходит и выходит", 4, 12.0),
        CafeItem(R.drawable.cafe, "Пора покушать", "Скорей ешь когда голодный, не зверей зайди скорей", 5, 7.0),
        CafeItem(R.drawable.cafe, "Магнит", "Так и манит к нам поесть, у нас есть в котлете шерсть", 3, 18.0),
        CafeItem(R.drawable.cafe, "Вилка ложка", "У нас не едят руками, вилки и ложки всегда в ниличии", 2, 7.0)
    )
}