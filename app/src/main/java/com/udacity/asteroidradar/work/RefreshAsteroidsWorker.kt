package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.getDatabaseInstance
import com.udacity.asteroidradar.repository.AsteroidRepository

class RefreshAsteroidsWorker(private val context:Context, params:WorkerParameters):CoroutineWorker(context,params) {
    override suspend fun doWork(): Result {
        val database = getDatabaseInstance(context)
        val repository = AsteroidRepository(database)
        return try{
            repository.refreshAsteroids()
            Result.success()
        }catch (exception:Exception){
            Result.retry()
        }

    }


    companion object{
        const val  WORK_NAME = "AsteroidsUpdater"
    }

}