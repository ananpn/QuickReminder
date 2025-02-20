package aap.QuickReminder.Navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Destinations(
    val route: String,
    val title: String? = null,
    val icon: @Composable () -> ImageVector
) {
    object ReminderScreenDestination : Destinations(
        route = "reminder_screen",
        title = "Reminder",
        icon = { Icons.Outlined.Edit }
    )

    object TimeScreenDestination : Destinations(
        route = "time_screen",
        title = "Time",
        icon = {Icons.Outlined.Notifications}
    )

}

