package aap.QuickReminder

import aap.QuickReminder.Logic.TimeFunctions
import android.app.Activity
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.provider.CalendarContract
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.TimeZone

class CalendarEventManager {
    companion object{
        @Composable
        fun createCalendarEvent(context: Context, text: String, time: String, calendarId : Int, activity: Activity) {
            val startTime = TimeFunctions.timeMillis(time)
            val values = ContentValues().apply {
                put(CalendarContract.Events.TITLE, text)
                put(CalendarContract.Events.DESCRIPTION, "Reminder Created by Quick Reminder")
                put(CalendarContract.Events.DTSTART, startTime)
                put(CalendarContract.Events.DTEND, startTime)
                put(CalendarContract.Events.CALENDAR_ID, calendarId) // Change the calendar ID as needed
                put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().id)
                put(CalendarContract.Events.HAS_ALARM, 1)
            }
            val uri = context.contentResolver.insert(CalendarContract.Events.CONTENT_URI, values)
            Log.v("", uri.toString())
            val eventId = uri?.lastPathSegment?.toLongOrNull()
            eventId?.let {
                //setting the reminder alert

                addReminderToEvent(context, context.contentResolver, eventId)
                val toast = Toast.makeText(
                    context,
                    "Reminder created: '" + text + "'",
                    Toast.LENGTH_SHORT
                ) // in Activity
                toast.show()
                closeApp(activity)
            } ?: run {
                val toast = Toast.makeText(
                    context,
                    "Error: Reminder probably not created! Unknown failure!",
                    Toast.LENGTH_LONG
                ) // in Activity
                toast.show()
                closeApp(activity)

            }
        }
    }
}

@Composable
fun addReminderToEvent(context : Context, contentResolver: ContentResolver, eventId: Long) {
    // Create a new reminder ContentValues
    val reminderValues = ContentValues().apply {
        put(CalendarContract.Reminders.EVENT_ID, eventId.toInt())
        put(CalendarContract.Reminders.MINUTES, 0)
        put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT)
    }
    // Insert the reminder into the Reminders table
    try{
        contentResolver.insert(CalendarContract.Reminders.CONTENT_URI, reminderValues)
    }
    catch (e : Exception){
        val toast = Toast.makeText(
            context,
            "Error: Alarm not set. Exception: $e",
            Toast.LENGTH_LONG
        ) // in Activity
        toast.show()
    }
}

fun DeleteOldEvents(context : Context) {
    val currentTimeMillis = LocalDateTime.now()
        .toEpochSecond(
            ZoneId.systemDefault().getRules().getOffset(Instant.now()))*1000-61000
    val eventsUri = CalendarContract.Events.CONTENT_URI
    val selection = "${CalendarContract.Events.DESCRIPTION} = ? AND ${CalendarContract.Events.DTSTART} < ?"
    val selectionArgs = arrayOf("Reminder Created by Quick Reminder", currentTimeMillis.toString())
    val contentResolver = context.contentResolver
    contentResolver.delete(eventsUri, selection, selectionArgs)
}
/*
fun DeleteAllEvents(context : Context) {
    val eventsUri = CalendarContract.Events.CONTENT_URI
    val selection = "${CalendarContract.Events.DESCRIPTION} = ?"
    val selectionArgs = arrayOf("Reminder Created by Quick Reminder")
    val contentResolver = context.contentResolver
    contentResolver.delete(eventsUri, selection, selectionArgs)
}*/
