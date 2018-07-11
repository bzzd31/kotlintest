package test.kotlin.com.kotlintest.model

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

@Dao
interface PersonDataDao {
    @Query("SELECT * from personData")
    fun getAll(): List<PersonData>

    @Insert
    fun insert(personData: PersonData)

    @Query("DELETE from personData")
    fun deleteAll()

    @Delete
    fun delete(personData: PersonData)
}
