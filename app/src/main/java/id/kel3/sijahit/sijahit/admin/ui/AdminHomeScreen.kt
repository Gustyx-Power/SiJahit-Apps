package id.kel3.sijahit.sijahit.admin.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@Composable
fun AdminHomeScreen() {
    var currentIndex by remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = currentIndex == 0,
                    onClick = { currentIndex = 0 },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Beranda") }
                )
                NavigationBarItem(
                    selected = currentIndex == 1,
                    onClick = { currentIndex = 1 },
                    icon = { Icon(Icons.Default.List, contentDescription = "Pesanan") },
                    label = { Text("Pesanan") }
                )
                NavigationBarItem(
                    selected = currentIndex == 2,
                    onClick = { currentIndex = 2 },
                    icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Stok") },
                    label = { Text("Stok") }
                )
                NavigationBarItem(
                    selected = currentIndex == 3,
                    onClick = { currentIndex = 3 },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profil") },
                    label = { Text("Profil") }
                )
            }
        }
    ) { padding ->
        when (currentIndex) {
            0 -> AdminDashboardScreen(modifier = Modifier.padding(padding))
            1 -> AdminOrdersScreen(modifier = Modifier.padding(padding))
            2 -> AdminStockScreen(modifier = Modifier.padding(padding))
            3 -> AdminProfileScreen(modifier = Modifier.padding(padding))
        }
    }
}