package id.kel3.sijahit.sijahit.admin.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import id.kel3.sijahit.sijahit.admin.viewmodel.AdminModelViewModel

@Composable
fun AddModelDialog(viewModel: AdminModelViewModel) {
    var nama by remember { mutableStateOf("") }
    var deskripsi by remember { mutableStateOf("") }
    var linkGambar by remember { mutableStateOf("") }

    Dialog(onDismissRequest = { viewModel.hideAddModelDialog() }) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 8.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = "Tambah Model Jahitan",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = nama,
                    onValueChange = { nama = it },
                    label = { Text("Nama Model") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = deskripsi,
                    onValueChange = { deskripsi = it },
                    label = { Text("Deskripsi") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = linkGambar,
                    onValueChange = { linkGambar = it },
                    label = { Text("Link Gambar (rasio 9:16)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(20.dp))

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = { viewModel.hideAddModelDialog() }) {
                        Text("Batal")
                    }

                    Spacer(Modifier.width(8.dp))

                    Button(
                        onClick = {
                            viewModel.tambahModel(nama, deskripsi, linkGambar)
                        },
                        enabled = nama.isNotBlank() && deskripsi.isNotBlank() && linkGambar.isNotBlank()
                    ) {
                        Text("Simpan")
                    }
                }
            }
        }
    }
}