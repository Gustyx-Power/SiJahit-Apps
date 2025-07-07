package id.kel3.sijahit.sijahit.user

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import id.kel3.sijahit.sijahit.ui.theme.SiJahitTheme
import id.kel3.sijahit.sijahit.user.ui.UserHomeScreen

class UserHomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SiJahitTheme {
                UserHomeScreen()
            }
        }
    }
}