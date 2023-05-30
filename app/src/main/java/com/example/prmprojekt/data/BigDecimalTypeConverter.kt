package com.example.prmprojekt.data

import androidx.room.TypeConverter
import java.math.BigDecimal


class BigDecimalTypeConverter {
    val decDivisor = BigDecimal(100)

    @TypeConverter
    fun toBigDecimal(lg: Long): BigDecimal {
        return BigDecimal(lg).divide(decDivisor)
    }

    @TypeConverter
    fun fromBigDecimal(bd: BigDecimal): Long {
        return bd.multiply(decDivisor).toLong()
    }
}