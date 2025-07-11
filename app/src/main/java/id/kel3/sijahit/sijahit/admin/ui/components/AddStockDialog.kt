package id.kel3.sijahit.sijahit.admin.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import id.kel3.sijahit.sijahit.admin.viewmodel.AdminStockViewModel

@Composable
fun AddStockDialog(viewModel: AdminStockViewModel) {
    var nama by remember { mutableStateOf("") }
    var jumlah by remember { mutableStateOf("") }
    var warna by remember { mutableStateOf("") }
    var gambar by remember { mutableStateOf("") }

    var errorNama by remember { mutableStateOf(false) }
    var errorJumlah by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = { viewModel.hideAddStockDialog() },
        confirmButton = {
            TextButton(onClick = {
                errorNama = nama.isBlank()
                errorJumlah = jumlah.toIntOrNull() == null || jumlah.toInt() <= 0

                if (!errorNama && !errorJumlah) {
                    viewModel.tambahStock(
                        nama = nama.trim(),
                        jumlah = jumlah.toInt(),
                        warna = warna.trim(),
                        gambar = gambar.trim()
                    )
                }
            }) {
                Text("Tambah")
            }
        },
        dismissButton = {
            TextButton(onClick = { viewModel.hideAddStockDialog() }) {
                Text("Batal")
            }
        },
        title = { Text("Tambah Stok Kain") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = nama,
                    onValueChange = {
                        nama = it
                        errorNama = false
                    },
                    label = { Text("Nama Kain") },
                    isError = errorNama,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                if (errorNama) {
                    Text(
                        "Nama tidak boleh kosong",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                OutlinedTextField(
                    value = jumlah,
                    onValueChange = {
                        jumlah = it
                        errorJumlah = false
                    },
                    label = { Text("Jumlah (meter)") },
                    isError = errorJumlah,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                if (errorJumlah) {
                    Text(
                        "Jumlah harus lebih dari 0",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                OutlinedTextField(
                    value = warna,
                    onValueChange = { warna = it },
                    label = { Text("Warna Kain") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = gambar,
                    onValueChange = { gambar = it },
                    label = { Text("Link Gambar (Opsional)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    )
}