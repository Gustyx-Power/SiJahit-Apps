package id.kel3.sijahit.sijahit.admin.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import id.kel3.sijahit.sijahit.admin.viewmodel.Pesanan

@Composable
fun PesananCard(pesanan: Pesanan, onUbahStatus: (String) -> Unit) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val isDini = currentUser?.email == "gustiadityamuzaky08@gmail.com"
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("👤 ${pesanan.namaUser}", style = MaterialTheme.typography.titleMedium)
            Text("📦 Jenis Pesanan: ${pesanan.jenisPesanan}")
            Text("🧵 Jenis Kain: ${pesanan.jenisKain}")
            Text("💰 Total: Rp ${formatRupiah(pesanan.total)}")
            Text("📌 Status: ${pesanan.status}", color = Color.DarkGray)

            if (expanded) {
                Spacer(Modifier.height(12.dp))
                Text("📏 Detail Ukuran:", fontWeight = FontWeight.SemiBold)
                with(pesanan.ukuran) {
                    Text("• Panjang Badan: $panjangBadan cm")
                    Text("• Lebar Badan: $lebarBadan cm")
                    Text("• Pinggang: $lebarPinggang cm")
                    Text("• Pinggul: $lebarPinggul cm")
                    Text("• Lengan: $lebarLengan cm")
                    Text("• Panjang Lengan: $panjangLengan cm")
                }

                Spacer(Modifier.height(12.dp))

                when (pesanan.status.lowercase()) {
                    "menunggu" -> {
                        if (isDini) {
                            Button(
                                onClick = { onUbahStatus("Diproses") },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
                            ) {
                                Text("Konfirmasi")
                            }
                        } else {
                            Text("Menunggu konfirmasi dari Dini", color = Color.Gray)
                        }
                    }

                    "diproses" -> {
                        Button(
                            onClick = { onUbahStatus("Selesai") },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF388E3C))
                        ) {
                            Text("Selesaikan")
                        }
                    }

                    "selesai" -> {
                        Text("✅ Pesanan selesai", color = Color(0xFF4CAF50), fontWeight = FontWeight.Medium)
                    }
                }
            }

            Spacer(Modifier.height(4.dp))
            Text(
                if (expanded) "Tutup ▲" else "Lihat Detail ▼",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.End)
                    .clickable { expanded = !expanded }
            )
        }
    }
}

fun formatRupiah(value: Int): String {
    return "%,d".format(value).replace(',', '.')
}