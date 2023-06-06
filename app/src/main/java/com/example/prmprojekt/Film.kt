package com.example.prmprojekt

import java.math.BigDecimal

data class Film (var nazwa: String="", var rating: BigDecimal?=null, var id: Int, var url: String = ""){

}
fun isRating(rating : BigDecimal?):Boolean{
    if(rating == null) return false
    var valid = rating >= BigDecimal(1) && rating<=BigDecimal(10)
    return valid
}