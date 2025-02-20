package aap.QuickReminder.Modals

import aap.QuickReminder.Logic.TimeFunctions.Companion.convertMillisToLocalDate
import aap.QuickReminder.Logic.TimeFunctions.Companion.toMilliseconds
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import kotlinx.coroutines.delay
import java.lang.reflect.Field
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDatePicker(
    inDate : LocalDate?,
    upDate : (LocalDate?) -> Unit
) {
    val dateMillis by remember(inDate?.toMilliseconds()){ mutableStateOf(inDate?.toMilliseconds()) }
    var dateState : DatePickerState? by remember{ mutableStateOf(null) }
    var inDateReceived by remember{mutableStateOf(false)}
    var dateStateNull by remember{mutableStateOf(false)}
    LaunchedEffect(dateMillis){
        inDateReceived=true
        dateState = null
        delay(20)
        dateStateNull = true
        delay(200)
        inDateReceived = false
    }
    LaunchedEffect(dateState?.selectedDateMillis) {
        if (!inDateReceived)
            upDate(convertMillisToLocalDate(dateState?.selectedDateMillis))
    }
    if (inDateReceived&&dateStateNull){
        dateState = rememberDatePickerState(
            initialSelectedDateMillis = dateMillis
        )
        dateStateNull = false
    }
    val nnDateState = dateState ?: rememberDatePickerState(dateMillis)

    val scaleFactor = 0.78f

    Box(modifier = Modifier
        .graphicsLayer {
            scaleX = scaleFactor*1.02f
            scaleY = scaleFactor
            translationY = -260f
        }
    ) {
        if (dateState != null){
            DatePicker(
                title = null,
                headline = null,
                state = dateState!!,
                showModeToggle = false,
            )
        }
        else {
            DatePicker(
                title = null,
                headline = null,
                state = nnDateState,
                showModeToggle = false,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
fun getStateData(datePickerState: DatePickerState): Any? {
    val field: Field = DatePickerState::class.java.getDeclaredField("stateData")
    field.isAccessible = true
    return field.get(datePickerState)
}

@OptIn(ExperimentalMaterial3Api::class)
fun setStateData(datePickerState: DatePickerState, stateData: Any?) {
    val field: Field = DatePickerState::class.java.getDeclaredField("stateData")
    field.isAccessible = true
    field.set(datePickerState, stateData)
}