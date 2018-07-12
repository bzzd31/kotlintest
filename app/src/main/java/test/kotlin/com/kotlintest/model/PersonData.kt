package test.kotlin.com.kotlintest.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*


@Entity(tableName = "personData")
data class PersonData(@PrimaryKey(autoGenerate = true) var id: Long?,
                      @ColumnInfo(name = "firstName") var firstName: String,
                      @ColumnInfo(name = "lastName") var lastName: String,
                      @ColumnInfo(name = "age") var age: Number,
                      @ColumnInfo(name = "creationDate") var creationDate: Date

) {
    constructor() : this(null, "", "", 0, Date())

    constructor(firstName: String, lastName: String, age: Number, date: Date) : this(null, firstName, lastName, age, date)

    override fun toString(): String {
        return "PersonData(id=$id, firstName='$firstName', lastName='$lastName', age=$age, creationDate=$creationDate)"
    }


}