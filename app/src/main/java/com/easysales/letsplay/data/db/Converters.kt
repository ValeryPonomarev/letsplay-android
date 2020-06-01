package com.easysales.letsplay.data.db

import androidx.room.TypeConverter
import java.util.*

class Converters {
    @TypeConverter
    fun fromDate(value: Date?): Long? {
        return value?.time
    }

    @TypeConverter
    fun toDate(value: Long?): Date? {
        return if(value == null) null else Date(value)
    }

//    @TypeConverter fun measureToDB(data: Measure): String = data.name
//    @TypeConverter fun measureFromDB(data: String): Measure = Measure.valueOf(data)
}