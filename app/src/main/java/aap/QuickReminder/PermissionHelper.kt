package aap.QuickReminder

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionHelper(private val activity: Activity, private val rqCode : Int, private val permissions : Array<String>) {
    /*
    companion object {
        public const val REQUEST_CODE = rqCode
    }
    */
    fun requestPermission() {
        //Log.v("PermissionHelper","launched permissionhelper")
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission)
                != PackageManager.PERMISSION_GRANTED
            ) {
                //Log.v("PermissionHelper","requesting permissions")
                ActivityCompat.requestPermissions(
                    activity,
                    permissions,
                    rqCode
                )
                break
            }
        }
    }
}