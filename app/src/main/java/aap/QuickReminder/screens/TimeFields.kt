package aap.QuickReminder.screens

import aap.QuickReminder.ui.theme.monoSpace_style
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp


/*
Here the hope was to have 4 different TextFields for hours, minutes, days, months.
However with this configuration it might not be possible to implement the required functionality that
the cursor would move backwards to the previous TextField when the current TextField
is empty and backspace is pressed
*/
@Composable
fun TimeTextField(
    maximum : Int,
    onChange : (String) -> Unit,
    value : String,
    modifier : Modifier = Modifier,
    onDone : () -> Unit
){
    TextField(
        modifier = modifier,
        value = value,
        onValueChange = {
            if (it.length < 3)
                onChange(it)
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        textStyle = monoSpace_style.copy(fontSize = 12.sp),
        keyboardActions = KeyboardActions(
            onDone = {
                onDone()
            }
        ),
        maxLines = 1,
        
    )
}