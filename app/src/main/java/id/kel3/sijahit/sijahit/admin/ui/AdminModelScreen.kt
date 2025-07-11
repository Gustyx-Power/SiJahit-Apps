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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import id.kel3.sijahit.sijahit.admin.ui.components.AddModelDialog
import id.kel3.sijahit.sijahit.admin.viewmodel.AdminModelViewModel
import id.kel3.sijahit.sijahit.admin.viewmodel.ModelJahitan

@Composable
fun AdminModelScreen(viewModel: AdminModelViewModel = AdminModelViewModel()) {
    val modelList by viewModel.modelList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isAddDialogVisible by viewModel.isDialogVisible.collectAsState()
    val selectedModelId by viewModel.selectedModelId.collectAsState()
    val isDeleteDialogVisible by viewModel.isDeleteDialogVisible.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.showAddModelDialog()
            }) {
                Icon(Icons.Default.Add, contentDescription = "Tambah")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                modelList.isEmpty() -> {
                    Text(
                        text = "Belum ada model tersedia",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                else -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        items(modelList) { model ->
                            AnimatedVisibility(
                                visible = true,
                                enter = fadeIn() + expandVertically()
                            ) {
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(containerColor = Color.White),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(12.dp)
                                    ) {
                                        Image(
                                            painter = rememberAsyncImagePainter(model.gambar),
                                            contentDescription = model.nama,
                                            modifier = Modifier
                                                .width(80.dp)
                                                .height(120.dp),
                                            contentScale = ContentScale.Crop
                                        )

                                        Spacer(Modifier.width(12.dp))

                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = model.nama,
                                                style = MaterialTheme.typography.titleMedium.copy(
                                                    fontWeight = FontWeight.Bold
                                                )
                                            )
                                            Text(
                                                text = model.deskripsi,
                                                style = MaterialTheme.typography.bodySmall,
                                                maxLines = 3
                                            )
                                        }

                                        IconButton(onClick = {
                                            viewModel.confirmDelete(model.id)
                                        }) {
                                            Icon(Icons.Default.Delete, contentDescription = "Hapus")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 🔵 Dialog Tambah Model
            if (isAddDialogVisible) {
                AddModelDialog(viewModel)
            }

            // 🔴 Dialog Konfirmasi Hapus
            if (isDeleteDialogVisible && selectedModelId != null) {
                AlertDialog(
                    onDismissRequest = { viewModel.dismissDeleteDialog() },
                    confirmButton = {
                        TextButton(onClick = {
                            viewModel.hapusModel(selectedModelId!!)
                            viewModel.dismissDeleteDialog()
                        }) {
                            Text("Hapus")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { viewModel.dismissDeleteDialog() }) {
                            Text("Batal")
                        }
                    },
                    title = { Text("Hapus Model") },
                    text = { Text("Apakah Anda yakin ingin menghapus model ini?") }
                )
            }
        }
    }
}