package id.kel3.sijahit.sijahit.user.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import id.kel3.sijahit.sijahit.user.components.UserBottomNav
import id.kel3.sijahit.sijahit.user.ui.components.PromoCarousel

@Composable
fun UserHomeScreen() {
    var currentIndex by remember { mutableIntStateOf(0) }
    var username by remember { mutableStateOf("Pengguna") }

    val user = FirebaseAuth.getInstance().currentUser

    LaunchedEffect(Unit) {
        if (user?.displayName != null) {
            username = user.displayName!!
        } else {
            val uid = user?.uid
            uid?.let {
                FirebaseDatabase.getInstance().getReference("users")
                    .child(uid).child("username").get()
                    .addOnSuccessListener { snapshot ->
                        username = snapshot.getValue(String::class.java) ?: "Pengguna"
                    }
            }
        }
    }

    Scaffold(
        bottomBar = {
            UserBottomNav(currentIndex) { index ->
                currentIndex = index
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                text = "Halo, $username 👋",
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = "Temukan layanan jahit terbaik untuk Anda",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(Modifier.height(16.dp))

            PromoCarousel() // slider dummy promo
        }
    }
}