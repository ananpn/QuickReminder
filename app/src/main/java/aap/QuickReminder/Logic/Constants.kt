package aap.QuickReminder.Logic

import android.Manifest

object constants {
    val permissions =
        arrayOf(Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR)
    val datechars = listOf(":", "  ", ".")
    var autoFill : Boolean = false
    var autoFillyear : Boolean = false
    val pattern = Regex("^\\d+\$")

    var timeDone = false //global variables are used for now
    var wentBack = false
    var textStored = ""
    fun timeChars(label : Int) : String{
        if (label>=5){
            return " "
        }
        return "0"
    }
    val currentYear= TimeFunctions.getTimeNow("yyyy")

}