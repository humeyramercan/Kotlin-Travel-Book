package com.humeyramercan.kotlinmaps.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import androidx.room.RoomDatabase
import com.humeyramercan.kotlinmaps.R
import com.humeyramercan.kotlinmaps.adapter.PlaceAdapter
import com.humeyramercan.kotlinmaps.databinding.ActivityMainBinding
import com.humeyramercan.kotlinmaps.model.Place
import com.humeyramercan.kotlinmaps.roomdb.PlaceDao
import com.humeyramercan.kotlinmaps.roomdb.PlaceDatabase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

private lateinit var binding: ActivityMainBinding



class MainActivity : AppCompatActivity() {

    private lateinit var db:PlaceDatabase
    private lateinit var placeDao:PlaceDao
    private val compositeDisposible=CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        val view= binding.root
        setContentView(view)

        db=Room.databaseBuilder(applicationContext,PlaceDatabase::class.java,"Places").build()
        placeDao=db.placeDao()

        compositeDisposible.add(
            placeDao.getAll().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(this::handleResponse)
        )
    }
    private fun handleResponse(placeList:List<Place>){
        binding.recyclerView.layoutManager=LinearLayoutManager(this)
       val adapter=PlaceAdapter(placeList)
        binding.recyclerView.adapter=adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater=menuInflater
        menuInflater.inflate(R.menu.place_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==R.id.add_place){
            val intent=Intent(this,MapsActivity::class.java)
            intent.putExtra("info","new")
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }
}