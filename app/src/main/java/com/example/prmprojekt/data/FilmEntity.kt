package com.example.prmprojekt.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity
class FilmEntity(
    var nazwa: String = "",
    var rating: BigDecimal? = null,
    @PrimaryKey(autoGenerate = true)
    var id: Int?,
    var url: String = ""
) {
}