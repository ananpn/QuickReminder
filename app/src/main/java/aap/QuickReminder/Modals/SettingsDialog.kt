package aap.QuickReminder.Modals

import aap.QuickReminder.DeleteOldEvents
import aap.QuickReminder.ui.theme.internaliconColor
import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun SettingsDialog(context : Context,
                   openSettings : Boolean,
                   sharedPreferences : SharedPreferences,
                   dismissRequest : (Boolean) -> Unit) {
    if (openSettings) {
        val radioOptions = listOf("Never", "Daily", "Weekly")
        val initialValue = sharedPreferences.getString("DeleteInterval", "Never")
        val (selectedOption, onOptionSelected) = remember { mutableStateOf(initialValue) }
        val initialCalendarId = sharedPreferences.getInt("CalendarId", 1)
        val (calendarId, idUpdated) = remember { mutableStateOf(initialCalendarId.toString()) }
        AlertDialog(
            onDismissRequest = {
                sharedPreferences
                    .edit()
                    .putInt("CalendarId", stringToInt(calendarId))
                    .apply()
                dismissRequest(false)
            },
            title = {
                Text(text = "Settings")
            },
            text = {
                Column(){
                    Text("How often to delete old reminders?")
                    Row {
                        radioOptions.forEach { text ->
                            Column(
                                Modifier
                                    .weight(1f)
                                    .selectable(
                                        selected = (text == selectedOption),
                                        onClick = {
                                            onOptionSelected(text)
                                            sharedPreferences
                                                .edit()
                                                .putString("DeleteInterval", text)
                                                .apply()
                                        }
                                    )
                            ) {
                                RadioButton(
                                    modifier = Modifier.align(CenterHorizontally),
                                    selected = (text == selectedOption),
                                    onClick = {
                                        onOptionSelected(text)
                                        sharedPreferences.edit().putString("DeleteInterval", text).apply()}
                                )
                                Text(
                                    modifier = Modifier.align(CenterHorizontally),
                                    text = text,
                                    textAlign = TextAlign.Start,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.size(30.dp))
                    Row{
                        Text(modifier = Modifier.align(CenterVertically),
                            text = "Delete old reminders now:")
                        Button(
                            modifier = Modifier
                                .align(CenterVertically)
                                .offset(20.dp, 0.dp),
                            onClick={
                                DeleteOldEvents(context)
                            }
                        ){
                            Icon(
                                Icons.Filled.Delete,
                                contentDescription = "Delete",
                                modifier = Modifier
                                    .size(20.dp),
                                tint = internaliconColor
                            )

                        }
                    }
                    Spacer(modifier = Modifier.size(12.dp))
                    Text("Calendar to use, as integer (0, 1, 2...):")
                    Spacer(modifier = Modifier.size(4.dp))
                    Row(){
                        TextField(
                            value = calendarId,
                            onValueChange = {new ->
                                idUpdated(new)
                            },
                            modifier = Modifier.fillMaxWidth(0.4f),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done
                            ),
                        )
                        Spacer(modifier = Modifier.size(6.dp))
                        Text(
                            text = "Depends on the phone, but for example 0 might be the calendar " +
                                    "linked to your Google Account, etc.",
                            textAlign = TextAlign.Start,
                            style = MaterialTheme.typography.bodySmall
                                .copy(color = Color.LightGray.copy(alpha = 0.6f)
                                )
                        )
                    }
                }

            },
            confirmButton = {
                TextButton(
                    onClick = {
                        sharedPreferences
                            .edit()
                            .putInt("CalendarId", stringToInt(calendarId))
                            .apply()
                        dismissRequest(false)
                    }
                ) {
                    Text(
                        text = "Done"
                    )
                }
            },
        )
    }
}

fun stringToInt(string : String) : Int {
    var output = 1
    try {
        output = string.trim().filter{it.isDigit()}.toInt()
    }
    catch (e : Exception){
    }
    return output
}