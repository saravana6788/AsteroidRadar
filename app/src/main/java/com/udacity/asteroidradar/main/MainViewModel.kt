package com.udacity.asteroidradar.main

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.AsteroidAPI
import com.udacity.asteroidradar.database.getDatabaseInstance
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val _picOfday = MutableLiveData<PictureOfDay>()
    val picOfDay:LiveData<PictureOfDay>
        get() = _picOfday

    private val repository = AsteroidRepository(getDatabaseInstance(application))



    private var _filterAsteroid = MutableLiveData(AsteroidIntervals.ALL)

    @RequiresApi(Build.VERSION_CODES.O)
    val asteroidData = Transformations.switchMap(_filterAsteroid) {
        when (it!!) {
            AsteroidIntervals.WEEK -> repository.asteroidsForWeek
            AsteroidIntervals.TODAY -> repository.asteroidsForToday
            else -> repository.asteroids
        }
    }


    init{
        getImageOfDay()
        refreshAsteroids()
    }


    private fun getImageOfDay(){
       viewModelScope.launch {
           val pictureOfDay = AsteroidAPI.retrofitService.getImageOfTheDay()
           _picOfday.value = pictureOfDay
       }
    }

    private fun refreshAsteroids(){
        viewModelScope.launch {
            repository.refreshAsteroids()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun filterAsteroids(interval: AsteroidIntervals){
        _filterAsteroid.value = interval
        }

}



