package com.udacity.asteroidradar

import android.app.Application
import android.os.Build
import androidx.work.*
import com.udacity.asteroidradar.work.RefreshAsteroidsWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class AsteroidApplication:Application() {

    private val applicationScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        refreshAsteroids()
    }

    private fun refreshAsteroids(){
        applicationScope.launch{
            val constraints = Constraints.Builder().
                    setRequiredNetworkType(NetworkType.UNMETERED)
                .setRequiresBatteryNotLow(true)
                .setRequiresCharging(true)
                .apply {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        setRequiresDeviceIdle(true)
                    }
                }
                .build()
            val recurringWork = PeriodicWorkRequestBuilder<RefreshAsteroidsWorker>(1,TimeUnit.DAYS)
                .setConstraints(constraints)
                .build()
            WorkManager.getInstance().enqueueUniquePeriodicWork(RefreshAsteroidsWorker.WORK_NAME,
                ExistingPeriodicWorkPolicy.REPLACE,
                recurringWork)
        }
    }
}