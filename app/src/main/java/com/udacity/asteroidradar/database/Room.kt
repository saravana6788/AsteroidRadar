package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface AsteroidDao{

    @Query("SELECT * from DataBaseAsteroid ORDER BY closeApproachDate asc")
    fun getAsteroids():LiveData<List<DataBaseAsteroid>>

    @Query("SELECT * from DataBaseAsteroid where closeApproachDate BETWEEN :startDate AND :endDate ORDER BY closeApproachDate asc")
    fun getAsteroidsForDateRange(startDate:String,endDate:String):LiveData<List<DataBaseAsteroid>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAsteroids(vararg asteroids:DataBaseAsteroid)
}


@Database(entities = [DataBaseAsteroid::class], version = 1)
abstract class AsteroidDatabase:RoomDatabase(){
    abstract val asteroidDao:AsteroidDao
}


private lateinit var INSTANCE: AsteroidDatabase
fun getDatabaseInstance(context: Context):AsteroidDatabase{
    synchronized(AsteroidDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                AsteroidDatabase::class.java,
                "asteroids"
            )
                .build()
        }
    }
    return INSTANCE
}