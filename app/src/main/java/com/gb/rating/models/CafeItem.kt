
package com.gb.rating.models

class CafeItem(
    var img: Int=0,
    var name: String = "",
    var type: String = "",
    var desc: String = "",
    var rating: Int = 3,
    var country: String = "",
    var city: String = "",
    var street: String = "",
    var home: String = "",
    var loc: String = "",
    var wTime: String = "",

    var distance: Double = 1.0, // оставил
    var cafeId: String = "",
    var latitude: Float = 0f,
    var longitude: Float = 0f,
    var deleted: Boolean = false, //для обратного преобразования из Firebase и прочих в cv SQLlite - предполагаются, что они не знают друг друга
    var fav: Boolean = false
)