package id.kel3.sijahit.sijahit.admin.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import id.kel3.sijahit.sijahit.admin.ui.components.AddStockDialog
import id.kel3.sijahit.sijahit.admin.viewmodel.AdminStockViewModel

@Composable
fun AdminStockScreen(viewModel: AdminStockViewModel = AdminStockViewModel()) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedStockId by remember { mutableStateOf<String?>(null) }

    val stockList by viewModel.stockList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val showAddDialog by viewModel.showDialog.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.showAddStockDialog() },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Stok")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                stockList.isEmpty() -> {
                    Text(
                        text = "Belum ada data stok kain.",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(stockList) { stock ->
                            AnimatedVisibility(
                                visible = true,
                                enter = fadeIn() + expandVertically()
                            ) {
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                                    ),
                                    elevation = CardDefaults.cardElevation(4.dp)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .padding(12.dp)
                                            .fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        // Gambar jika tersedia
                                        if (stock.gambar.isNotBlank()) {
                                            Image(
                                                painter = rememberAsyncImagePainter(stock.gambar),
                                                contentDescription = stock.nama,
                                                modifier = Modifier
                                                    .size(width = 80.dp, height = 100.dp),
                                                contentScale = ContentScale.Crop
                                            )
                                            Spacer(modifier = Modifier.width(12.dp))
                                        }

                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = stock.nama,
                                                style = MaterialTheme.typography.titleMedium
                                            )
                                            Text(
                                                text = "Warna: ${stock.warna}",
                                                style = MaterialTheme.typography.bodySmall
                                            )
                                            Text(
                                                text = "Jumlah: ${stock.jumlah} meter",
                                                style = MaterialTheme.typography.bodySmall
                                            )
                                        }

                                        IconButton(onClick = {
                                            selectedStockId = stock.id
                                            showDeleteDialog = true
                                        }) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "Hapus Stok",
                                                tint = MaterialTheme.colorScheme.error
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (showAddDialog) {
                AddStockDialog(viewModel)
            }

            if (showDeleteDialog && selectedStockId != null) {
                AlertDialog(
                    onDismissRequest = { showDeleteDialog = false },
                    confirmButton = {
                        TextButton(onClick = {
                            viewModel.hapusStock(selectedStockId!!)
                            showDeleteDialog = false
                        }) {
                            Text("Hapus")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDeleteDialog = false }) {
                            Text("Batal")
                        }
                    },
                    title = { Text("Hapus Data Stok") },
                    text = { Text("Apakah Anda yakin ingin menghapus data stok ini?") }
                )
            }
        }
    }
}