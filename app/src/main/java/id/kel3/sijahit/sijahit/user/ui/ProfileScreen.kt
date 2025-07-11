package id.kel3.sijahit.sijahit.user.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import id.kel3.sijahit.sijahit.user.components.UserProfileCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    val context = LocalContext.current
    val user = FirebaseAuth.getInstance().currentUser
    val name = user?.displayName ?: "Pengguna"
    val email = user?.email ?: "Tidak diketahui"

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Profil Saya") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(24.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            UserProfileCard(name = name, email = email, badge = "User")

            Button(onClick = {
                FirebaseAuth.getInstance().signOut()
                Toast.makeText(context, "Berhasil logout", Toast.LENGTH_SHORT).show()
                navController.navigate("login") {
                    popUpTo("home") { inclusive = true }
                }
            }) {
                Text("Logout")
            }
        }
    }
}