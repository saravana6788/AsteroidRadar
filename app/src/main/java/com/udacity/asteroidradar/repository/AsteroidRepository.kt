package com.udacity.asteroidradar.repository

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.api.AsteroidAPI
import com.udacity.asteroidradar.api.NetworkUtils
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.asDatabaseModel
import com.udacity.asteroidradar.database.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AsteroidRepository(private val database:AsteroidDatabase) {

    @RequiresApi(Build.VERSION_CODES.O)
    val currentDate: String = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE)

    val asteroids:LiveData<List<Asteroid>> = Transformations.map(database.asteroidDao.getAsteroids()){
        it.asDomainModel()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    val asteroidsForToday:LiveData<List<Asteroid>> = Transformations.map(database.asteroidDao.getAsteroidsForDateRange(currentDate,currentDate)){
        it.asDomainModel()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    val asteroidsForWeek:LiveData<List<Asteroid>> = Transformations.map(database.asteroidDao.getAsteroidsForDateRange(currentDate,LocalDateTime.now().plusDays(7).toString())){
        it.asDomainModel()
    }



    suspend fun refreshAsteroids(){
        withContext(Dispatchers.IO){
            val asteroids = NetworkUtils.parseAsteroidsJsonResult(JSONObject(AsteroidAPI.retrofitService.getAsteroids()))
            database.asteroidDao.insertAsteroids(* asteroids.asDatabaseModel())
        }

    }
}