package id.kel3.sijahit.sijahit.user.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DesignServices
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import id.kel3.sijahit.sijahit.user.components.UserBottomNav
import id.kel3.sijahit.sijahit.user.ui.components.PromoCarousel

@Composable
fun UserHomeScreen(navController: NavHostController) {
    var currentIndex by remember { mutableIntStateOf(0) }
    var username by remember { mutableStateOf("Pengguna") }

    val user = FirebaseAuth.getInstance().currentUser

    // Fetch nama pengguna dari Firebase
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
                when (index) {
                    0 -> navController.navigate("home") // Home
                    1 -> navController.navigate("form_pesanan_user")
                    2 -> navController.navigate("pembayaran")
                    3 -> navController.navigate("profil")
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Halo, $username 👋",
                style = MaterialTheme.typography.headlineSmall
            )

            Text(
                text = "Temukan layanan jahit terbaik untuk Anda",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
            )

            PromoCarousel()

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Model Jahitan",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                JahitCard(
                    title = "Model Siap Pakai",
                    subtitle = "Pilih model, lalu isi ukuran",
                    icon = Icons.Default.Style,
                    gradient = listOf(Color(0xFF6A1B9A), Color(0xFFAB47BC)),
                    onClick = { navController.navigate("model_list") }
                )

                JahitCard(
                    title = "Model Custom",
                    subtitle = "Isi semua detail dan ukuran sendiri",
                    icon = Icons.Default.DesignServices,
                    gradient = listOf(Color(0xFF00695C), Color(0xFF4DB6AC)),
                    onClick = { navController.navigate("form_ukuran") }
                )

                JahitCard(
                    title = "Pesanan Saya",
                    subtitle = "Lihat status dan detail pesanan Anda",
                    icon = Icons.Default.ListAlt,
                    gradient = listOf(Color(0xFF3949AB), Color(0xFF5C6BC0)),
                    onClick = { navController.navigate("pesanan_user") }
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun JahitCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    gradient: List<Color>,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .background(Brush.horizontalGradient(gradient))
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = title,
                        fontSize = 18.sp,
                        color = Color.White
                    )
                    Text(
                        text = subtitle,
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }
        }
    }
}