package id.kel3.sijahit.sijahit.admin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import id.kel3.sijahit.sijahit.admin.ui.AdminHomeScreen
import id.kel3.sijahit.sijahit.ui.theme.SiJahitTheme

class AdminHomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SiJahitTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    AdminHomeScreen()
                }
            }
        }
    }
}