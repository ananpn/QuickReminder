package aap.QuickReminder

import android.content.Context
import android.content.SharedPreferences
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import java.util.concurrent.TimeUnit

fun OldDeleter(context : Context, sharedPreferences : SharedPreferences) {
    val intervalString = sharedPreferences.getString("DeleteInterval", "Never")
    var interval : Long = 0
    if (intervalString == "Never") interval = 0
    if (intervalString == "Daily") interval = 1
    if (intervalString == "Weekly") interval = 7
    if (interval>0){
        val request = PeriodicWorkRequestBuilder<MyWorker>(interval, TimeUnit.DAYS)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "delete_old_events_quickreminder",
            ExistingPeriodicWorkPolicy.REPLACE,
            request
        )
    }
    else{
        WorkManager.getInstance(context).cancelUniqueWork("delete_old_events_quickreminder")
    }

}

class MyWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        DeleteOldEvents(applicationContext)
        return Result.success()
    }
}

