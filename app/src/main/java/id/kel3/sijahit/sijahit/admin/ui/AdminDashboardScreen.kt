@file:Suppress("DEPRECATION")

package id.kel3.sijahit.sijahit.admin.ui

import androidx.compose.animation.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import id.kel3.sijahit.sijahit.admin.ui.components.SummaryCard
import kotlinx.coroutines.delay
import java.text.NumberFormat
import java.util.*

@Composable
fun AdminDashboardScreen(
    saldo: Int,
    pesananAktif: Int,
    konfirmasiMenunggu: Int,
    pesananSelesai: Int,
    stokMenipis: Int,
    adminCount: Int,
    pelanggan: Int,
    totalProduk: Int
) {
    var currentIndex by remember { mutableStateOf(0) }
    val user = FirebaseAuth.getInstance().currentUser
    val displayName = user?.displayName ?: "Admin"

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = currentIndex == 0,
                    onClick = { currentIndex = 0 },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Beranda") },
                    label = { Text("Beranda") }
                )
                NavigationBarItem(
                    selected = currentIndex == 1,
                    onClick = { currentIndex = 1 },
                    icon = {
                        @Suppress("DEPRECATION")
                        Icon(Icons.Default.List, contentDescription = "Pesanan")
                    },
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
                    icon = { Icon(Icons.Default.Mode, contentDescription = "Model") },
                    label = { Text("Model") }
                )
                NavigationBarItem(
                    selected = currentIndex == 4,
                    onClick = { currentIndex = 4 },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profil") },
                    label = { Text("Profil") }
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text(
                text = when (currentIndex) {
                    0 -> "Halo, $displayName 👋"
                    1 -> "Daftar Pesanan"
                    2 -> "Manajemen Stok"
                    3 -> "Model"
                    4 -> "Profil Admin"
                    else -> ""
                },
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
            )

            Text(
                text = when (currentIndex) {
                    0 -> "Pantau dan kelola seluruh aktivitas admin"
                    1 -> "Lihat dan kelola pesanan pelanggan"
                    2 -> "Atur stok barang tersedia"
                    3 -> "Kelola Model Anda"
                    4 -> "Informasi akun & pengaturan"
                    else -> ""
                },
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(Modifier.height(16.dp))

            when (currentIndex) {
                0 -> BerandaContent(
                    saldo,
                    pesananAktif,
                    konfirmasiMenunggu,
                    pesananSelesai,
                    stokMenipis,
                    adminCount,
                    pelanggan,
                    totalProduk
                )
                1 -> AdminPesananScreen()
                2 -> AdminStockScreen()
                3 -> AdminModelScreen()
                4 -> AdminProfileScreen()
            }
        }
    }
}

@Composable
@Preview
fun AdminDashboardScreenPreview() {
    AdminDashboardScreen(
        saldo = 1000000,
        pesananAktif = 5,
        konfirmasiMenunggu = 2,
        pesananSelesai = 10,
        stokMenipis = 3,
        adminCount = 2,
        pelanggan = 50,
        totalProduk = 100
    )
}


@Composable
fun BerandaContent(
    saldo: Int,
    aktif: Int,
    menunggu: Int,
    selesai: Int,
    stokMenipis: Int,
    adminCount: Int,
    pelanggan: Int,
    totalProduk: Int
) {
    var visible by remember { mutableStateOf(false) }
    var selectedFilter by remember { mutableStateOf("Hari ini") }
    val dummyData = when (selectedFilter) {
        "Hari ini" -> listOf(30, 50, 40, 70, 60)
        "Seminggu" -> listOf(100, 200, 180, 220, 300, 250, 280)
        "Sebulan" -> listOf(120, 180, 200, 190, 210, 250, 300, 270)
        else -> emptyList()
    }

    LaunchedEffect(Unit) {
        delay(300)
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(initialOffsetY = { it }) + expandIn()
    ) {
        Column {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF2196F3)),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("Saldo Saat Ini", fontSize = 18.sp, fontWeight = FontWeight.Medium, color = Color.White)
                    Text("Rp ${formatRupiah(saldo)}", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text("Data saldo terbaru", fontSize = 14.sp, color = Color.White.copy(alpha = 0.8f))
                }
            }

            Spacer(Modifier.height(16.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF64B5F6)),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("Pesanan Aktif", fontSize = 18.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                        Column {
                            Text("Aktif", fontSize = 14.sp, color = Color.White.copy(alpha = 0.8f))
                            Text(aktif.toString(), fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                        Column {
                            Text("Menunggu", fontSize = 14.sp, color = Color.White.copy(alpha = 0.8f))
                            Text(menunggu.toString(), fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                        Column {
                            Text("Selesai", fontSize = 14.sp, color = Color.White.copy(alpha = 0.8f))
                            Text(selesai.toString(), fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                SummaryCard("Total Admin", adminCount.toString(), Icons.Default.AdminPanelSettings, Modifier.weight(1f))
                SummaryCard("Pelanggan", pelanggan.toString(), Icons.Default.People, Modifier.weight(1f))
            }

            Spacer(Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                SummaryCard("Total Produk", totalProduk.toString(), Icons.Default.Store, Modifier.weight(1f))
                SummaryCard("Stok Menipis", stokMenipis.toString(), Icons.Default.Warning, Modifier.weight(1f))
            }

            Spacer(Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("Diagram Penghasilan", fontSize = 18.sp, fontWeight = FontWeight.Medium)
                    Text("Saldo Keseluruhan: Rp ${formatRupiah(saldo)}", fontSize = 14.sp, color = Color.DarkGray)
                    Spacer(Modifier.height(8.dp))
                    FilterDropdown(selectedFilter) { selectedFilter = it }
                    Spacer(Modifier.height(16.dp))
                    SimpleBarChart(data = dummyData)
                }
            }
        }
    }
}

@Composable
@Preview
fun BerandaContentPreview() {
    BerandaContent(
        saldo = 500000,
        aktif = 3,
        menunggu = 1,
        selesai = 5,
        stokMenipis = 2,
        adminCount = 1,
        pelanggan = 25,
        totalProduk = 50
    )
}


@Composable
fun FilterDropdown(selected: String, onSelected: (String) -> Unit) {
    val options = listOf("Hari ini", "Seminggu", "Sebulan")
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedButton(onClick = { expanded = true }) {
            Text(selected)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
@Preview
fun FilterDropdownPreview() {
    FilterDropdown(selected = "Hari ini", onSelected = {})
}


@Composable
fun SimpleBarChart(data: List<Int>) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp) // Increased height to accommodate text
    ) {
        val barWidth = (size.width - (data.size + 1) * 8.dp.toPx()) / data.size.coerceAtLeast(1)
        val max = data.maxOrNull() ?: 1
        val textPaint = Paint().asFrameworkPaint().apply {
            isAntiAlias = true
            color = android.graphics.Color.BLACK
            textSize = 12.sp.toPx() // Convert sp to px for canvas
        }

        data.forEachIndexed { index, value ->
            val barHeight = (value / max.toFloat()) * size.height
            val xOffset = index * (barWidth + 8.dp.toPx()) + 8.dp.toPx()
            val barTopY = size.height - barHeight

            drawRect(
                color = Color(0xFF4CAF50),
                topLeft = androidx.compose.ui.geometry.Offset(xOffset, barTopY),
                size = androidx.compose.ui.geometry.Size(barWidth, barHeight)
            )

            // Draw text value above the bar
            val text = formatRupiah(value) // Menggunakan formatRupiah untuk menampilkan nilai
            val textX = xOffset + barWidth / 2 - textPaint.measureText(text) / 2
            val textY = barTopY - 4.dp.toPx() // Position text slightly above the bar

            drawContext.canvas.nativeCanvas.drawText(
                text,
                textX,
                textY,
                textPaint
            )
        }
    }
}

@Composable
@Preview
fun SimpleBarChartPreview() {
    SimpleBarChart(data = listOf(10, 20, 15, 30, 25))
}


fun formatRupiah(value: Int): String {
    val formatter = NumberFormat.getNumberInstance(Locale("id", "ID"))
    return formatter.format(value)
}