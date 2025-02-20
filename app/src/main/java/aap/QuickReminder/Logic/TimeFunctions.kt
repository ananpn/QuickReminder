package aap.QuickReminder.Logic

import aap.QuickReminder.Logic.constants.currentYear
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class TimeFunctions {
    companion object {
        //takes format eg "HHmmddMMyyy" or "yyyy" and returns the current time in this format
        fun getTimeNow(format : String) : String {
            val formatter = DateTimeFormatter.ofPattern(format)
            val current = LocalDateTime.now().format(formatter)
            return current
        }

        fun formatToLocalDateTime(time : String, format : String) : LocalDateTime {
            //Log.v("fTLC time format", time+" "+format)
            val formatter = DateTimeFormatter.ofPattern(format)
            val output = LocalDateTime.parse(time, formatter)
            return output
        }

        fun timeToLocalDate(time : String, year : String) : LocalDate? {
            if (time.length < 7) return null
            var output : LocalDate? = null
            try {

                val date = formatToLocalDateTime(
                    formatTimeStringZeroes(time) +year,
                    "HHmmddMMyyyy"
                )
                output = date.toLocalDate()
            }
            catch (e : Exception){
            }
            return output
        }

        @Composable
        fun timeMillis(time : String) : Long {
            val date= formatToLocalDateTime(time.replace(" ", "0"),
                                            "HHmmddMMyyyy")
            val millis = date.toEpochSecond(ZoneId.systemDefault()
                                                    .getRules()
                                                    .getOffset(Instant.now())
                    )*1000
            //Log.v("timeMillis", time+" "+millis)
            return millis
        }

        //formatting of time inputs for creating calendar event
        fun timeFormatToCalendar(time : String, year : String) : String{
            val formatterHHmm = DateTimeFormatter.ofPattern("HHmm")
            val formatterddMM = DateTimeFormatter.ofPattern("ddMMyyyy")
            val currentHHmm = LocalDateTime.now().format(formatterHHmm)
            val tomorrowddMM = LocalDateTime.now().plusDays(1).format(formatterddMM)
            val currentddMM = LocalDateTime.now().format(formatterddMM)
            var time2 = ""
            if (time.trimEnd().length==4){
                if (currentHHmm.toInt()>=time.trimEnd().toInt()) {
                    time2 = time.trimEnd() + tomorrowddMM
                    //Log.v("timeformatter tomorrow", time2)
                }
                else {
                    time2 = time.trimEnd() + currentddMM
                    //Log.v("timeformatter today", time2)
                }
                if (time2.substring(4 until 5)=="0"){
                    time2=time2.substring(0 until 4)+" "+time2.substring(5)
                }
            }
            if (time.trimEnd().length==7){
                time2 = time.replaceRange(6,6,"0").trimEnd().take(8)
                //Log.v("time2 7", time2)
            }
            if (time.trimEnd().length==8){
                time2=time
            }
            time2 = formatTimeStringZeroes(time2) //if dd is of form "3 " this makes it " 3"
            if (time2.trimEnd().length==8) {
                time2 += ("0000"+year.replace(" ", "")).takeLast(4)

            }
            return time2
        }

        fun autoFillNextDate(time : String, year : String) : String{
            val todayYear = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy"))
            val tomorrowYear= LocalDateTime.now().plusDays(1)
                .format(DateTimeFormatter.ofPattern("yyyy"))
            val nextYear= LocalDateTime.now().plusYears(1)
                .format(DateTimeFormatter.ofPattern("yyyy"))

            val formatterHHmm = DateTimeFormatter.ofPattern("HHmm")
            val formatterddMM = DateTimeFormatter.ofPattern("ddMM")
            val formatterMMdd = DateTimeFormatter.ofPattern("MMdd")
            val tomorrowddMM = LocalDateTime.now().plusDays(1).format(formatterddMM)
            val currentHHmm = LocalDateTime.now().format(formatterHHmm)
            val todayddMM = LocalDateTime.now().format(formatterddMM)
            val todayMMdd = LocalDateTime.now().format(formatterddMM)
            val time2 = formatTimeStringSpaces(time).replace(" ", "0")
            //Log.v("autoFillNextDate", "time2 = $time2")
            if (time2.substring(4,8) == "0000"){
                if (currentHHmm>=time2.substring(0,4)) {
                    return formatTimeStringDisplay(time2.substring(0,4)+tomorrowddMM) +tomorrowYear
                    //Log.v("timeformatter tomorrow", time2)
                }
                else return formatTimeStringDisplay(time2.substring(0,4)+todayddMM) + todayYear
            }
            else if (time2.substring(6,8)+time2.substring(4,6)<todayMMdd){
                return (formatTimeStringDisplay(time2) +nextYear)
            }
            return time+year
        }

        fun isTimeBeforeCurrent(time : String, year : String) : Boolean {
            val formatterHHmm = DateTimeFormatter.ofPattern("HHmm")
            val formatterYMD = DateTimeFormatter.ofPattern("yyyyMMddHHmm")
            var inputDate = ""
            val currentHHmm = LocalDateTime.now().format(formatterHHmm)
            var time2 = formatTimeStringSpaces(time)
            //Log.v("isTBC time2", time2+".")
            if (time2.trimEnd().length == 4 && year==currentYear) {
                if (currentHHmm >= time2.trimEnd()) {
                    return true
                }
            }
            /*
            if (time2.length == 7) {
                time2 = time2.replaceRange(6, 6, "0").take(8)
            }
            */
            if (time2.length == 8 && time2.substring(0,2) != "  " && time2.substring(2,4) != "  ") {
                time2 += year
                try {
                    inputDate = formatToLocalDateTime(
                        time2.replace(" ", "0"), "HHmmddMMyyyy"
                    ).format(formatterYMD)
                }
                catch (e : Exception){
                    //Log.v("isTBC error", time2)
                    return false
                }
                //Log.v("isTBC inputdate", inputDate)
                val currentDate= getTimeNow("yyyyMMddHHmm")
                if (inputDate<=currentDate) return true
            }
            return false
        }

        fun autoFillValidation(time : String, year : String) : Boolean{
            Log.v("autofillValidation", "time = $time, year = $year")
            if (time.trimEnd().length < 4) return false
            if (time.trimEnd().length==4) {
                return true
            }
            return isTimeBeforeCurrent(time, year)
        }

        //Visually transforms input of the timetextfield to format hh:mm  dd.MM
        fun timeDateFilter(input: AnnotatedString): TransformedText {

            // Making hh:mm d.MM string.
            val text = input
            val trimmed = if (text.length >= 8) text.substring(0..7) else text
            var out = ""
            var n = 0

            for (i in trimmed.indices) {
                out += trimmed[i]
                if (i % 2 == 1 && i != 7) {
                    out += constants.datechars[n]
                    n++
                }
            }

            val timeOffsetTranslator = object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int {
                    if (offset <= 1) return offset
                    if (offset <= 3) return offset + 1
                    if (offset <= 5) return offset + 3
                    if (offset <= 7) return offset + 4
                    return 12
                }

                override fun transformedToOriginal(offset: Int): Int {
                    if (offset <= 1) return offset
                    if (offset <= 4) return offset - 1
                    if (offset <= 8) return offset - 3
                    if (offset <= 11) return offset - 4
                    return 8
                }
            }
            return TransformedText(AnnotatedString(out), timeOffsetTranslator)
        }

        fun convertMillisToLocalDate(millis: Long?) : LocalDate? {
            if (millis == null) return null
            else
            return Instant
                .ofEpochMilli(millis)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
        }

        fun LocalDate.toMilliseconds(): Long {
            val localDateTime = this.atStartOfDay()
            val instant = localDateTime.toInstant(ZoneOffset.UTC)
            return instant.toEpochMilli()
        }

        fun Int.toDateString(): String {
            ("00"+this.toString()).takeLast(2).apply{
                if (this.substring(0,1)=="0") return " "+this.takeLast(1)
                else return this
            }
        }

        fun Int.toMonthString(): String {
            return ("00"+this.toString()).takeLast(2).replace("0", "")
        }

        fun appendDateToTime(time : String, date : String) : String {
            var output = time
            if (output.isEmpty()){
                output = "    "+date
            }
            else{
                //the inputted time must begin at hours and minutes
                while(output.length<4){
                    output += " "
                }
                output = output.take(4)+date

            }

            return output
        }
    }
}