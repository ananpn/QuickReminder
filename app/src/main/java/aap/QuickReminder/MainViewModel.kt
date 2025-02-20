package aap.QuickReminder

import aap.QuickReminder.Logic.constants.currentYear
import aap.QuickReminder.Navigation.Destinations
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController

class MainViewModel(navHostController: NavHostController) : ViewModel() {
    val navHostController = navHostController
    fun showReminderScreen(){
        navHostController.navigate(Destinations.ReminderScreenDestination.route)
    }
    fun showTimeScreen(){
        navHostController.navigate(Destinations.TimeScreenDestination.route)
    }

    var reminderText : String = ""
    
    var timeString : String = ""
    var yearString : String = currentYear
    
    
    /*
    var hours : String = ""
    var minutes : String = ""
    var days : String = ""
    var months : String = ""
    */
    


}