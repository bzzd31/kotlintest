package test.kotlin.com.kotlintest.model

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import test.kotlin.com.kotlintest.Converters

@Database(entities = [(PersonData::class)], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun personDataDao(): PersonDataDao

    companion object {
        var dbInstance: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase? {
            if (dbInstance == null)
                dbInstance = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
            return dbInstance;
        }
    }
}