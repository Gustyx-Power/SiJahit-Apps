package id.kel3.sijahit.sijahit.user

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.compose.rememberNavController
import id.kel3.sijahit.sijahit.ui.theme.SiJahitTheme
import id.kel3.sijahit.sijahit.user.navigation.UserNavHost

class UserHomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SiJahitTheme {
                val navController = rememberNavController()
                UserNavHost(navController = navController)
            }
        }
    }
}