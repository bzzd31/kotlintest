package test.kotlin.com.kotlintest

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import test.kotlin.com.kotlintest.model.AppDatabase
import test.kotlin.com.kotlintest.model.PersonData


class MainActivity : AppCompatActivity() {
    private var mDb: AppDatabase? = null
    private lateinit var mDbWorkerThread: DbWorkerThread

    private val mUiHandler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mDbWorkerThread = DbWorkerThread("dbWorkerThread")
        mDbWorkerThread.start()

        mDb = AppDatabase.getInstance(this)

        insertPersonDataInDb(createRandomPerson())
        fetchWeatherDataFromDb()
    }

    private fun fetchWeatherDataFromDb() {
        val task = Runnable {
            val personData =
                    mDb?.personDataDao()?.getAll()
            mUiHandler.post({
                if (personData == null || personData.isEmpty()) {
                    Log.d("DAO", "No data in cache..!!")
                } else {
                    for (person in personData) {
                        Log.d("Person " + person.id + ": ", person.firstName + " " + person.lastName + ": " + person.age);
                    }
                }
            })
        }
        mDbWorkerThread.postTask(task)
    }

    private fun insertPersonDataInDb(personData: PersonData) {

        val task = Runnable { mDb?.personDataDao()?.insert(personData) }
        mDbWorkerThread.postTask(task)
    }

    private fun createRandomPerson(): PersonData {
        return PersonData("Lucas", "Bled", 26)
    }
}
