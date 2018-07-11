package test.kotlin.com.kotlintest

import android.arch.persistence.room.TypeConverter

class Converters {

    @TypeConverter
    fun fromNumber(value: Number): Int {
        return value.toInt()
    }

    @TypeConverter
    fun toNumber(value: Int): Number {
        return value
    }

}