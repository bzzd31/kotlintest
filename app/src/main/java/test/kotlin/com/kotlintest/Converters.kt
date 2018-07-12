package test.kotlin.com.kotlintest

import android.arch.persistence.room.TypeConverter
import java.util.*


class Converters {

    @TypeConverter
    fun fromNumber(value: Number): Int {
        return value.toInt()
    }

    @TypeConverter
    fun toNumber(value: Int): Number {
        return value
    }

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return (if (date == null) null else date!!.time)?.toLong()
    }

}