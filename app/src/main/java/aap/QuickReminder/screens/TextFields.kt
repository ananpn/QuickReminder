package aap.QuickReminder.screens

import aap.QuickReminder.Logic.TimeFunctions
import aap.QuickReminder.Logic.constants
import aap.QuickReminder.Logic.constants.autoFill
import aap.QuickReminder.Logic.constants.autoFillyear
import aap.QuickReminder.Logic.constants.currentYear
import aap.QuickReminder.Logic.constants.timeDone
import aap.QuickReminder.Logic.formatDoneTimeTextFieldInput
import aap.QuickReminder.Logic.timeTextFieldInputs
import aap.QuickReminder.MainViewModel
import aap.QuickReminder.ui.theme.monoSpace_style
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun reminderTextField(
    modifier : Modifier, vm : MainViewModel, value : String, onChange : (String) -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    var textFieldLoaded by remember { mutableStateOf(false) }
    TextField(
        modifier = modifier
            .focusRequester(focusRequester)
            .onGloballyPositioned {
                if (!textFieldLoaded) {
                    focusRequester.requestFocus() // IMPORTANT
                    textFieldLoaded = true // stop cyclic recompositions
                }
            },
        value = value,
        onValueChange = {
            onChange(it)
        },
        label = { Text("Reminder:") },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next,
            capitalization = KeyboardCapitalization.Sentences
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun yearTextFieldOld(
    exxYear: TextFieldValue,
    onChanged: (TextFieldValue) -> Unit,
    onDone : () -> Unit
) {
    var inYear by remember { mutableStateOf(TextFieldValue(text= currentYear)) }
    val pattern = remember { Regex("^\\d+\$") }
    val maxChar = 4
    //Log.v("inyear in yearTextField", inyear.toString())
    if (autoFillyear) {
        inYear=exxYear
    }
    var donePressed by remember { mutableStateOf(false) }
    LaunchedEffect(donePressed){
        if (donePressed) {
            timeDone = true
            donePressed = false
        }
    }
    TextField(
        value = inYear,
        onValueChange = {
            val newt=it.text
            if (newt.isEmpty() || (newt.matches(pattern) && newt.length <= maxChar)) {
                inYear = it
            }
            else {
                if (newt.matches(pattern)&&it.selection.end == maxChar+1) {
                    inYear = it.copy(text = newt.take(3) + newt.takeLast(1))
                }
                else {
                    if (it.selection.end>inYear.selection.end && newt != inYear.text){
                        val outt = newt.removeRange(it.selection.end until it.selection.end+1)
                        inYear = it.copy(text = outt)
                    }
                }
            }
            if (autoFillyear) {
                onChanged(it)
                autoFillyear =false
            }
        },
        label = { Text("Year:") },
        placeholder = { Text(currentYear) },
        singleLine = true,
        maxLines = 1,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        textStyle = LocalTextStyle.current.copy(
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Normal
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                onDone()
            }
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun timeTextFieldOld(
    exxtime : String,
    onChanged : (String) -> Unit,
    onDone : () -> Unit,
) {
    var intime by remember { mutableStateOf(TextFieldValue(text=exxtime)) }
    val focusRequester = remember { FocusRequester() }
    var textFieldLoaded by remember { mutableStateOf(false) }
    //Log.v("exxtime in timeTextField", exxtime.toString())
    if (constants.autoFill) {
        intime = TextFieldValue(text = exxtime)
    }
    TextField(
        modifier = Modifier
            .focusRequester(focusRequester)
            .onFocusChanged {
                intime = formatDoneTimeTextFieldInput(intime)
            }
            .onGloballyPositioned {
                if (!textFieldLoaded) {
                    focusRequester.requestFocus() // IMPORTANT
                    textFieldLoaded = true // stop cyclic recompositions
                }
            },
        value = intime,
        onValueChange = {
            intime = timeTextFieldInputs(it, intime)
            onChanged(intime.text)
            if (autoFill) {
                onChanged(it.text)
                constants.autoFill = false
            }
        },
        label = { Text("Time:" ) },
        placeholder = { Text("hh:mm  DD.MM") },
        singleLine = true,
        maxLines = 1,
        visualTransformation = { TimeFunctions.timeDateFilter(it) },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        textStyle = monoSpace_style,
        
        /*      LocalTextStyle.current.copy(
                      fontFamily = FontFamily.Monospace,
                      fontWeight = FontWeight.Normal
                  ),*/
        keyboardActions = KeyboardActions(
            onDone = {
                onDone()
            }
        )
    )
}
