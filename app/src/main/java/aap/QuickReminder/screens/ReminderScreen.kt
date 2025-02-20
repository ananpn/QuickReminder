package aap.QuickReminder.screens

import aap.QuickReminder.Logic.constants
import aap.QuickReminder.MainViewModel
import aap.QuickReminder.initializePermissionHelper
import aap.QuickReminder.ui.theme.internaliconColor
import android.app.Activity
import android.content.Context
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
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
import androidx.compose.ui.unit.dp
import kotlin.random.Random

@Composable
fun ReminderScreen(
    context: Context,
    activity: Activity,
    vm: MainViewModel
){
    var remindernotdone by remember { mutableStateOf(true) }
    
    var textFieldValue by remember{ mutableStateOf(vm.reminderText) }
    
    
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.offset(16.dp, -67.dp)
    )
    {
        Text("")
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
    
    ) {
        reminderTextField(
            modifier = Modifier.fillMaxWidth(0.73f),
            vm = vm,
            onChange = { textFieldValue = it },
            value = textFieldValue
        )
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.10f),
            onClick = {
                vm.reminderText = textFieldValue
                vm.showTimeScreen()
            }
        ) {
            Icon(
                Icons.Filled.ArrowForward,
                contentDescription = "ArrowForward",
                modifier = Modifier
                    .size(60.dp)
                    .offset(0.dp, 0.dp),
                tint = internaliconColor
            )
        }
    }
    
    if (!remindernotdone) {
        val rqCode: Int = Random.nextInt(100, 500)
        initializePermissionHelper(activity, rqCode, constants.permissions)
    }
        


}