package aap.QuickReminder.Logic

import aap.QuickReminder.Logic.constants.pattern
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue


//This is horrible
fun timeTextFieldInputs(new : TextFieldValue, old : TextFieldValue) : TextFieldValue {
    //Log.v("tTFI new, old", new.toString() +"\r\n"+old.toString())
    val newText = new.text
    val oldText = old.text
    val newCursor = new.selection
    val oldCursor = old.selection
    var outText = newText
    while (outText.length <12) outText+=" "
    var out = new
    var hh=""//outText.substring(0,2)
    var mm=""//outText.substring(2,4)
    var dd=""//outText.substring(4,6)
    var MM=""//outText.substring(6,8)
    //val newchar="0"
    if (newText==oldText.take(8)){
        out=out.copy(text= formatTimeStringDisplay(outText.take(8).trimEnd()))
        return out
    }

    val newchar : String = newText
        .substring((newCursor.start-1).coerceAtLeast(0) until newCursor.start)
    if (newCursor.start == 9 && newchar.matches(pattern)) {
        outText = outText.replaceRange(
            6 until 8,
            newText.substring(
                newCursor.start - 1 until newCursor.start
            ) + " "
        )
        out = out.copy(
            text = outText.take(8).trimEnd(),
            selection = TextRange(7, 7)
        )
        return out
    }
    if (newCursor.start>oldCursor.start){
        if (newCursor.start>8){
            out=out.copy(selection= TextRange(8,8))
        }
        if (!newchar.matches(constants.pattern)){
            outText = outText.removeRange(newCursor.start-1, newCursor.start)
            if (newCursor.start%2==0 && newCursor.start < 8){
                outText = outText.replaceRange(newCursor.start-2 until newCursor.start,
                    constants.timeChars(newCursor.start - 1) +
                            newText.substring(newCursor.start-2 until newCursor.start-1)
                )
            }
            out = out.copy(text=outText.take(8).trimEnd(),
                selection = TextRange(newCursor.start-newCursor.start%2,
                    newCursor.start-newCursor.start%2)
            )
            return out
        }

        outText = outText.removeRange(newCursor.start, newCursor.start + 1)
        //Log.v("tTFI outText at newC>oldC", outText)
        hh=outText.substring(0,2)
        mm=outText.substring(2,4)
        dd=outText.substring(4,6)
        MM=outText.substring(6,8)
        if (simpleValidation(hh + mm + dd + MM) ==2) { //input OK
            out=out.copy(text=outText.take(8).trimEnd())
            //Log.v("tTFI" , "validation ok "+ out.toString())
            return out
        }
        else {
            if (newCursor.start % 2 == 0) {
                //Log.v("tTFI validation not ok, %2==0", outText)
                if (simpleValidation(hh + mm + dd + MM) ==1 && newCursor.start>5) { //if user tries to put 00 to day or month

                    outText=outText.replaceRange(newCursor.start-1 until newCursor.start, " ")
                    out = out.copy(
                        text = outText.take(8).trimEnd(),
                        selection = TextRange(newCursor.start-2, newCursor.start-2)
                    )
                    //Log.v("tTFI sV=1, %2==0", outText)
                    return out
                }
                outText = outText
                    .replaceRange(
                        newCursor.start - 2 until newCursor.start+1,
                        constants.timeChars(newCursor.start) +newText.substring(
                            newCursor.start - 2 until newCursor.start-1)+newText.substring(
                            newCursor.start - 1 until newCursor.start))
                hh=outText.substring(0,2)
                mm=outText.substring(2,4)
                dd=outText.substring(4,6)
                MM=outText.substring(6,8)
                if (simpleValidation(hh + mm + dd + MM) ==0) {
                    outText = outText.replaceRange(
                        (newCursor.start+1 until newCursor.start+2), " ")
                }

                if (newCursor.start == 8){
                    outText = outText.replaceRange(6 until 8,
                        newText.substring(
                            newCursor.start-1 until newCursor.start) +" ")
                    out = out.copy(
                        text = outText.take(8).trimEnd(),
                        selection = TextRange(7,7)
                    )
                    return out
                }
                out = out.copy(
                    text = outText.take(8).trimEnd(),
                    selection = TextRange(newCursor.start+1,newCursor.start+1)
                )
                //Log.v("tTFI %2==0", "validation not ok "+ out.toString())
                return out
            }
            else {
                outText = outText.replaceRange(
                    newCursor.start until newCursor.start + 1,
                    " "
                )
                out = out.copy(text = outText.take(8).trimEnd())
                //Log.v("tTFI validation not ok, %2==1", out.toString())
                return out
            }
        }
    }
    if (newCursor.start == oldCursor.start - 1 && newText != oldText) {
        outText =
            outText.substring(0, newCursor.start) + " " + outText.substring(newCursor.start).trimEnd()
        out=out.copy(text=outText.take(8).trimEnd())
        return out
    }
    out=out.copy(text=oldText.take(8).trimEnd())
    return out

}

fun formatDoneTimeTextFieldInput(intime : TextFieldValue) : TextFieldValue{
    return intime.copy(text = formatTimeStringDisplay(intime.text))
}




//simple validation for input while typing
fun simpleValidation(timeDate: String): Int {
    var output=2
    val HH = "0"+timeDate.substring(0,2).trim()
    val mm = "0"+timeDate.substring(2,4).trim()
    val dd = "0"+timeDate.substring(4,6).trim()
    val MM = "0"+timeDate.substring(6,8).trim()

    if (HH.toInt()<0 || HH.toInt()>23) output = 0
    if (mm.toInt()<0 || mm.toInt()>60) output = 0
    if (dd.toInt()<0 || dd.toInt()>31) output = 0
    if (MM.toInt()<0 || MM.toInt()>12) output = 0
    if (dd=="000" || MM=="000") output=1
    //Log.v("simplevalida", "$timeDate, HH = $HH, mm = $mm, dd = $dd, MM = $MM, output = $output")
    return output

}

//This is the final validation that the time is correctly input
fun timeStringValidation(time : String) : Boolean {
    val trimmed = time.trimEnd()
    if (trimmed.length < 7) return false
    val HH = trimmed.substring(0,2)
    val mm = trimmed.substring(2,4)
    val dd = trimmed.substring(4,6)
    val MM = trimmed.substring(6)
    if (HH.trim().isEmpty() || mm.trim().isEmpty()) return false
    if (dd == "  " || dd.trim() == "0" || dd == "00") return false
    if (MM == "0" || MM == "00") return false
    return true
}

//if dd is of form "3 " this makes it " 3"
fun formatTimeStringDisplay(input : String) : String{
    val HH = input.obtainHou()
    val mm = input.obtainMin()
    val dd = input.obtainDay()
    val MM = input.obtainMon()
    return formatHHmm(HH) + formatHHmm(mm) + formatdd(dd) + formatMM(MM)
}

fun formatHHmm(xx : String) : String{
    if (xx == "  ") return "  "
    return ("00"+xx.trim()).takeLast(2) //" 2" -> "02", "2 " -> "02"
}
fun formatdd(xx : String) : String{
    if (xx == "  " || xx == "00" || xx.trim() == "0") return "  "
    if (xx.substring(0,1) == "0") return (xx.replace("0", " ")).take(2)
    return (" "+xx.trim()).takeLast(2)
}
fun formatMM(xx : String) : String{
    if (xx.isEmpty() || xx == "  " || xx == "00" || xx.trim() == "0") return " "
    if (xx.substring(0,1) == "0") return (xx.trim()+" ").takeLast(2)
    return (xx.trim()+" ").take(2)
}

fun formatTimeStringZeroes(input : String) : String{
    val HH = input.obtainHou()
    val mm = input.obtainMin()
    val dd = input.obtainDay()
    val MM = input.obtainMon()
    return formatHHmmFin(HH) + formatHHmmFin(mm) + formatddMMFin(dd) + formatddMMFin(MM)
}

fun formatHHmmFin(xx : String) : String{
    return ("00"+xx.trim()).takeLast(2) //" 2" -> "02", "2 " -> "02", "  " -> "00"
}
fun formatddMMFin(xx : String) : String{
    if (xx == "  " || xx == "00" || xx.trim() == "0") return "01"
    return ("00"+xx.trim()).takeLast(2)
}

fun formatTimeStringSpaces(time : String) : String{
    val MM = ("  "+time.obtainMon().trimEnd()).takeLast(2)
    return time.obtainHou()+time.obtainMin()+time.obtainDay()+MM
}

fun String.obtainHou() : String{
    return ("$this  ").substring(0,2)
}
fun String.obtainMin() : String{
    var string  = this
    while(string.length < 4) string += " "
    return string.substring(2,4)
}
fun String.obtainDay() : String{
    var string  = this
    while(string.length < 6) string += " "
    return string.substring(4,6)
}
fun String.obtainMon() : String{
    var string  = this
    while(string.length < 8) string += " "
    return string.substring(6,8)
}



