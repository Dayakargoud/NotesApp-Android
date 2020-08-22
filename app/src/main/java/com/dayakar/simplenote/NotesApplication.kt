package com.dayakar.simplenote

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.*
import com.dayakar.simplenote.work.RefreshDataWorker
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class NotesApplication: Application(), Configuration.Provider  {
    private val applicationScope = CoroutineScope(Dispatchers.Default)
    @Inject lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()
        delayedInit()
    }
    private fun delayedInit() {
        applicationScope.launch {
            setUpOneTmeWork()
        }
    }

    private fun setupRecurringWork() {
        val repeatingRequest = PeriodicWorkRequestBuilder<RefreshDataWorker>(1, TimeUnit.HOURS)
            .build()

        WorkManager.getInstance().enqueueUniquePeriodicWork(RefreshDataWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,repeatingRequest)
    }

    fun setUpOneTmeWork(){
        val uploadWorkRequest = OneTimeWorkRequestBuilder<RefreshDataWorker>().build()
        WorkManager.getInstance(this).enqueue(uploadWorkRequest)
    }

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

}