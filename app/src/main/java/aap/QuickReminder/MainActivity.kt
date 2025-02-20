package aap.QuickReminder

import aap.QuickReminder.Navigation.NavigationGraph
import aap.QuickReminder.ui.theme.QuickReminder10Theme
import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        super.onCreate(savedInstanceState)
        val context : Context = this
        
        setContent {
            val navController: NavHostController = rememberNavController()
            val vm : MainViewModel = MainViewModel(navController)
            QuickReminder10Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Transparent
                ) {
                    NavigationGraph(
                        context = context,
                        navController = navController,
                        activity = this,
                        vm = vm,
                    )

                }
            }
        }
    }

}


fun closeApp(activity: Activity) {
    activity.finishAffinity()
}

fun initializePermissionHelper(activity: Activity, rqCode : Int, permissions : Array<String>) {
    //Log.v("main","launching permissionhelper")
    val permissionHelper = PermissionHelper(activity, rqCode, permissions)
    permissionHelper.requestPermission()
}
