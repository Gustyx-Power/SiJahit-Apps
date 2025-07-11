package id.kel3.sijahit.sijahit.user

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import id.kel3.sijahit.sijahit.user.navigation.UserNavGraphWrapper

@Composable
fun UserMainApp() {
    val navController = rememberNavController()
    UserNavGraphWrapper(navController)
}