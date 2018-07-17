package test.kotlin.com.kotlintest

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.TextView
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import test.kotlin.com.kotlintest.model.AppDatabase
import test.kotlin.com.kotlintest.model.PersonData
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {
    private val MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION: Int = 1
    private val MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: Int = 2
    private val MY_PERMISSIONS_REQUEST_ACCESS_WIFI_STATE: Int = 3
    private val MY_PERMISSIONS_REQUEST_ACCESS_NETWORK_STATE: Int = 4
    private val MY_PERMISSIONS_REQUEST_INTERNET: Int = 5
    private val MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: Int = 6

    private var mDb: AppDatabase? = null
    private var map: MapView? = null
    private var mMapController: MapController? = null
    private var mLocationOverlay: MyLocationNewOverlay? = null

    private var textView: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkPermissions()
        databaseStuff()
        mapViewStuff()
    }

    /**
     * Test some map view stuff
     * Using map view and controller
     * Put one marker and refresh its position
     */
    private fun mapViewStuff() {
        val ctx = applicationContext
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))
        setContentView(R.layout.activity_main)

        map = findViewById<View>(R.id.map) as MapView
        textView = findViewById(R.id.textView)

        map?.setBuiltInZoomControls(false)
        map?.setMultiTouchControls(true)
        mMapController = map?.controller as MapController
        mMapController?.setZoom(13)
        val gPt = GeoPoint(48.8583, 2.2944)
        mMapController?.setCenter(gPt)

        val scheduler = Executors.newSingleThreadScheduledExecutor()
        val startMarker = Marker(map)
        startMarker.position = gPt
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        startMarker.title = "My Marker"
        map?.overlays?.add(startMarker)
        scheduler.scheduleAtFixedRate({
            startMarker.position = GeoPoint(startMarker.position.latitude + 0.001, startMarker.position.longitude + 0.001)
        }, 0, 5, TimeUnit.SECONDS)
    }

    /**
     * Test some database stuff
     * Using thread to create and get entities
     */
    private fun databaseStuff() {
        mDb = AppDatabase.getInstance(this)
        Thread(Runnable {
            // a potentially  time consuming task
            mDb?.personDataDao()?.insert(createRandomPerson())
            mDb?.personDataDao()?.insert(createRandomPerson())
            val personData =
                    mDb?.personDataDao()?.getAll()
            showData(personData)
        }).start()
    }

    public override fun onResume() {
        super.onResume()
        mLocationOverlay?.enableMyLocation()
        map?.onResume() //needed for compass, my location overlays, v6.0.0 and up

    }

    public override fun onPause() {
        super.onPause()
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        mLocationOverlay?.disableMyLocation()
        map?.onPause()  //needed for compass, my location overlays, v6.0.0 and up
    }

    private fun showData(personData: List<PersonData>?) {
        if (personData != null) {
            for (person in personData) {
                textView?.text = person.firstName
                Log.d("Person " + person.id + ": ", person.firstName + " " + person.lastName + ": " + person.age + " -> created at " + person.creationDate)
            }
        }
    }

    private fun createRandomPerson(): PersonData {
        var date = Date(2018, 7, 12)
        return PersonData("Lucas", "Bled", 26, date)
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                    MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION)
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_WIFI_STATE),
                    MY_PERMISSIONS_REQUEST_ACCESS_WIFI_STATE)
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_NETWORK_STATE),
                    MY_PERMISSIONS_REQUEST_ACCESS_NETWORK_STATE)
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.INTERNET),
                    MY_PERMISSIONS_REQUEST_INTERNET)
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        if ((grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED)) {
            System.exit(0)
        }
        return
    }
}
