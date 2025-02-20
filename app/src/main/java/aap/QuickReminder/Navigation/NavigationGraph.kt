package aap.QuickReminder.Navigation

import aap.QuickReminder.MainViewModel
import aap.QuickReminder.screens.ReminderScreen
import aap.QuickReminder.screens.TimeScreen
import android.app.Activity
import android.content.Context
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.EaseOutSine
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable


@Composable
fun NavigationGraph(context : Context,
                    navController: NavHostController,
                    activity: Activity,
                    vm : MainViewModel
                    //dispDates : List<String>,
                    //sharedPreferences : SharedPreferences,
                    
){
    NavHost(
        navController,
        startDestination = Destinations.ReminderScreenDestination.route,
        enterTransition = { fadeIn(
            animationSpec = tween(
                durationMillis = 100,
                delayMillis = 100,
                easing = EaseInOut
            )
        ) },
        exitTransition = { fadeOut(
            animationSpec = tween(
                durationMillis = 100,
                delayMillis = 0,
                easing = EaseIn
            )
        ) }
    ) {
        composable(
            Destinations.ReminderScreenDestination.route,
        ) {
            ReminderScreen(
                context = context,
                activity = activity,
                vm = vm,
            )
        }
        composable(
            Destinations.TimeScreenDestination.route,
            enterTransition = {
                fadeIn(
                    animationSpec = tween(100, easing = EaseOutSine)
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(
                        durationMillis = 300,
                        delayMillis = 0,
                        easing = EaseOutSine)
                )
            },
        ){
            TimeScreen(
                context = context,
                activity = activity,
                vm = vm
            )
        }

    }
}