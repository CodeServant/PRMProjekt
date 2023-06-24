package com.example.prmprojekt

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class MyWorker(
    ctx: Context,
    parmas: WorkerParameters
) : CoroutineWorker(ctx, parmas) {

    override suspend fun doWork(): Result {
        Log.d("WORK", "worker started succesfully")
        Notificatons.createNotification(super.getApplicationContext())
        return Result.success()
    }

}