package test.kotlin.com.kotlintest.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey


@Entity(tableName = "personData")
data class PersonData(@PrimaryKey(autoGenerate = true) var id: Long?,
                      @ColumnInfo(name = "firstName") var firstName: String,
                      @ColumnInfo(name = "lastName") var lastName: String,
                      @ColumnInfo(name = "age") var age: Number

) {
    constructor() : this(null, "", "", 0)

    constructor(firstName: String, lastName: String, age: Int) : this(null, firstName, lastName, age)
}