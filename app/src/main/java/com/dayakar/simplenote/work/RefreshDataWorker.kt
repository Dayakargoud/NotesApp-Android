package com.dayakar.simplenote.work

import android.content.Context
import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.work.WorkerInject
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.dayakar.simplenote.repository.MainRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class RefreshDataWorker  @WorkerInject constructor(@Assisted appContext: Context,
                                                   @Assisted workerParams: WorkerParameters,val repository: MainRepository): Worker(appContext, workerParams) {

    override  fun doWork(): Result {
        try {
            refreshData()
            Log.d("Work Manager", "doWork: Synsc data in progress")
        }catch (e:Exception){
            Log.d("Work Manager", "doWork: ${e.message}")
            return Result.retry()
        }
        return Result.success()
    }
    companion object {
        const val WORK_NAME = "com.dayakar.simplenote.work.RefreshDataWorker"
    }
    fun refreshData(){
        CoroutineScope(Dispatchers.Main).launch {
            repository.syncNotes()
        }
    }

}