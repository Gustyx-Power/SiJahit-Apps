package id.kel3.sijahit.sijahit.admin.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AdminDashboardScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Admin Dashboard",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
        )

        Text(
            text = "Pantau dan kelola semua aktivitas",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 4.dp)
        )

        Spacer(Modifier.height(16.dp))

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(16.dp)) {
                Text("Saldo Saat Ini", fontSize = 18.sp, fontWeight = FontWeight.Medium)
                Text("Rp 0", fontSize = 32.sp, fontWeight = FontWeight.Bold)
                Text("Data saldo belum tersedia", fontSize = 14.sp, color = Color.Gray)
            }
        }

        Spacer(Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            SummaryCard("Pesanan Aktif", "0", Icons.Default.List, Modifier.weight(1f))
            SummaryCard("Stok Menipis", "0", Icons.Default.ShoppingCart, Modifier.weight(1f))
        }

        Spacer(Modifier.height(12.dp))

        SummaryCard("Konfirmasi Menunggu", "0", Icons.Default.Notifications, Modifier.fillMaxWidth())
    }
}