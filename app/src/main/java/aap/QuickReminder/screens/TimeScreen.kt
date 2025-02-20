package aap.QuickReminder.screens

import aap.QuickReminder.CalendarEventManager
import aap.QuickReminder.Logic.TimeFunctions
import aap.QuickReminder.Logic.TimeFunctions.Companion.appendDateToTime
import aap.QuickReminder.Logic.TimeFunctions.Companion.autoFillNextDate
import aap.QuickReminder.Logic.TimeFunctions.Companion.autoFillValidation
import aap.QuickReminder.Logic.TimeFunctions.Companion.isTimeBeforeCurrent
import aap.QuickReminder.Logic.TimeFunctions.Companion.timeToLocalDate
import aap.QuickReminder.Logic.TimeFunctions.Companion.toDateString
import aap.QuickReminder.Logic.TimeFunctions.Companion.toMonthString
import aap.QuickReminder.Logic.constants
import aap.QuickReminder.Logic.constants.autoFill
import aap.QuickReminder.Logic.constants.autoFillyear
import aap.QuickReminder.Logic.timeStringValidation
import aap.QuickReminder.MainViewModel
import aap.QuickReminder.Modals.MyDatePicker
import aap.QuickReminder.Modals.SettingsDialog
import aap.QuickReminder.OldDeleter
import aap.QuickReminder.closeApp
import aap.QuickReminder.ui.theme.internaliconColor
import aap.QuickReminder.ui.theme.primaryColor
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat

@Composable
fun TimeScreen(context: Context, activity: Activity, vm: MainViewModel){
    val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(
            "QuickReminderPrefs",
            Context.MODE_PRIVATE
        )
    
    var timeDone by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf(vm.reminderText) }
    
    var exxTime by remember { mutableStateOf(vm.timeString) }
    var year by remember { mutableStateOf(vm.yearString) }
    
    var exxTimeValue by remember { mutableStateOf(TextFieldValue(text = vm.timeString)) }
    var exxYear by remember { mutableStateOf(TextFieldValue(text = vm.yearString)) }
    
    var openSettings by remember { mutableStateOf(false) }
    var openCalendar by remember { mutableStateOf(false) }
    var tempTimeYear = ""
    //var timeDone by remember { mutableStateOf(false) }
    //Log.v("alku", "time="+time.toString())
    
    val timeInputOK = remember(exxTime, year){
        !isTimeBeforeCurrent(exxTime, year) && timeStringValidation(exxTime)
    }
    
    BackHandler {
        vm.timeString = exxTime
        vm.yearString = year
        openCalendar = false
        vm.showReminderScreen()
    }
    
    if (constants.timeDone) if (!isTimeBeforeCurrent(exxTime, year) &&
        timeStringValidation(exxTime)
    ){
        constants.timeDone =false
        timeDone=false //creates calendar event
    }
    
    
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.offset(16.dp, -67.dp)
    )
    {
        val current = TimeFunctions.getTimeNow("HH:mm  dd.MM.yyyy")
        Text("Current time:\r\n"+current, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = primaryColor)
        Button(modifier = Modifier.offset(x = 5.dp ),
               onClick = { openSettings = true}){
            Icon(
                Icons.Filled.Settings,
                contentDescription = "Settings",
                modifier = Modifier
                    .size(24.dp)
                    .offset(0.dp, 0.dp),
                tint = internaliconColor
            )
        }
        Button(modifier = Modifier.offset(x = 5.dp ),
               onClick = { openCalendar = !openCalendar}){
            Icon(
                Icons.Filled.DateRange,
                contentDescription = "Calendar",
                modifier = Modifier
                    .size(24.dp)
                    .offset(0.dp, 0.dp),
                tint = internaliconColor
            )
        }
        SettingsDialog(context, openSettings, sharedPreferences,
                       dismissRequest = {
                           openSettings = it
                           OldDeleter(context, sharedPreferences)
                       })
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
    
    ) {
        //Log.v("tiemfieldsrun run", "exxTime="+exxTime.toString())
        Column(Modifier.weight(5f)){
            timeTextFieldOld(
                exxtime = exxTime,
                onChanged = {
                    exxTime = it
                },
                onDone = {
                
                
                }
            )
        }
        Column(Modifier.weight(3f)){
            yearTextFieldOld(
                exxYear = exxYear,
                onChanged = {
                    exxYear = it
                },
                onDone = {}
            )
            
        }
        
        if (timeInputOK) {
            //Log.v("Done button run", "exxTime="+exxTime.toString())
            Button(
                modifier = Modifier
                    .fillMaxWidth(0.27f)
                    .fillMaxHeight(0.10f),
                onClick = {
                    timeDone = true}) //creates calendar event
            {
                Icon(
                    Icons.Filled.Done,
                    contentDescription = "Done",
                    modifier = Modifier
                        .size(60.dp)
                        .offset(0.dp, 0.dp),
                    tint = internaliconColor
                )
            }
        }
        else{
            Box(){
                Button(
                    modifier = Modifier
                        .fillMaxWidth(0.27f)
                        .fillMaxHeight(0.10f),
                    onClick = {
                        if (autoFillValidation(exxTime, year)){
                            autoFill = true
                            autoFillyear = true
                            //this returns the next exxTime+date+year the given exxTime or exxTime+date happens
                            tempTimeYear = autoFillNextDate(exxTime, year)
                            //tempTimeYear = TimeFunctions.timeFormatToCalendar(exxTime, tomorrowYear)
                            //Log.v("main autofill", "tempTimeYear = $tempTimeYear")
                            //Log.v("tempTimeYear3", tempTimeYear)
                            
                            exxYear = TextFieldValue(
                                text = tempTimeYear.takeLast(4),
                                selection = TextRange(4, 4)
                            )
                            //Log.v("autofill eka", exxTime +" "+year)
                            exxTime = tempTimeYear.dropLast(4)
                            /*
                            exxTimeValue = TextFieldValue(
                                text=tempTimeYear.dropLast(4),
                                selection= TextRange(4,4)
                            )
                            */
                        }
                    }) {
                    Icon(
                        Icons.Filled.Edit,
                        contentDescription = "Edit",
                        modifier = Modifier
                            .size(48.dp)
                            .offset(0.dp, -2.dp),
                        tint = internaliconColor,
                    )
                }
                if (autoFillValidation(exxTime, year)){
                    Text("Autofill\r\nnext",
                         modifier = Modifier
                             .offset(0.dp, 55.dp)
                             .fillMaxWidth(0.27f),
                         lineHeight = 8.sp,
                         textAlign = TextAlign.Center,
                         color = internaliconColor,
                         fontSize = 8.sp
                    )
                }
                
            }
        }
    }
    if (openCalendar){
        MyDatePicker(
            inDate = timeToLocalDate(exxTime, year),
            upDate = {
                if (it != null) {
                    exxYear = exxYear.copy(text = it.year.toString())
                    autoFillyear = true
                    autoFill = true
                    val dateFromCalendar = it.dayOfMonth.toDateString()+
                            it.monthValue.toMonthString()
                    exxTimeValue=exxTimeValue
                        .copy(text= appendDateToTime(exxTime, dateFromCalendar))
                }
            }
        )
    }
    
    
    if (timeDone) {
        timeDone=false //prevents creating double events in some cases
        //val time2 = TimeFunctions.timeFormatToCalendar(exxTime,year)
        val time2 = TimeFunctions.timeFormatToCalendar(exxTime, year).trim().replace(" ", "0")
        //Log.v("!timenotdone, time2", time2)
        var deniedPermissions: String = ""
        for (permission in constants.permissions){
            if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED) {
                deniedPermissions += permission
            }}
        if (deniedPermissions == "") {
            var text2=text
            if (text==""){
                text2="Reminder"
            }
            CalendarEventManager.createCalendarEvent(
                context = context,
                text = text2,
                time = time2,
                calendarId = sharedPreferences.getInt("CalendarId", 1),
                activity = activity
            )
        }
        else {
            val toast = Toast.makeText(context, "Cannot create reminder: no Calendar permissions. Exiting...", Toast.LENGTH_LONG) // in Activity
            toast.show()
            closeApp(activity)
        }
        
        
    }

}