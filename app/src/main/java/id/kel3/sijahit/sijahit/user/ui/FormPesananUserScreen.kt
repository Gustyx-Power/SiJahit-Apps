package id.kel3.sijahit.sijahit.user.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormPesananUserScreen(navController: NavController) {
    val context = LocalContext.current
    val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // State input
    var nama by remember { mutableStateOf("") }
    var jenisPesananExpanded by remember { mutableStateOf(false) }
    var selectedJenisPesanan by remember { mutableStateOf("") }
    val jenisPesananOptions = listOf("Jahit Baru", "Permak", "Custom Model")
    var jenisCustom by remember { mutableStateOf("") }

    var jenisKain by remember { mutableStateOf("") }
    var panjangBadan by remember { mutableStateOf("") }
    var lebarBadan by remember { mutableStateOf("") }
    var pinggang by remember { mutableStateOf("") }
    var pinggul by remember { mutableStateOf("") }
    var lengan by remember { mutableStateOf("") }
    var panjangLengan by remember { mutableStateOf("") }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Formulir Pesanan") }) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("📝 Data Pesanan", style = MaterialTheme.typography.titleLarge)

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = nama,
                        onValueChange = { nama = it },
                        label = { Text("Nama Lengkap") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    ExposedDropdownMenuBox(
                        expanded = jenisPesananExpanded,
                        onExpandedChange = { jenisPesananExpanded = !jenisPesananExpanded }
                    ) {
                        OutlinedTextField(
                            readOnly = true,
                            value = selectedJenisPesanan,
                            onValueChange = {},
                            label = { Text("Jenis Pesanan") },
                            trailingIcon = {
                                Icon(
                                    imageVector = if (jenisPesananExpanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                                    contentDescription = null
                                )
                            },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )

                        ExposedDropdownMenu(
                            expanded = jenisPesananExpanded,
                            onDismissRequest = { jenisPesananExpanded = false }
                        ) {
                            jenisPesananOptions.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        selectedJenisPesanan = option
                                        jenisPesananExpanded = false
                                        if (option != "Custom Model") jenisCustom = ""
                                    }
                                )
                            }
                        }
                    }

                    if (selectedJenisPesanan == "Custom Model") {
                        OutlinedTextField(
                            value = jenisCustom,
                            onValueChange = { jenisCustom = it },
                            label = { Text("Jenis Custom Model") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    OutlinedTextField(
                        value = jenisKain,
                        onValueChange = { jenisKain = it },
                        label = { Text("Jenis Kain") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Divider()

                    Text("📏 Ukuran", style = MaterialTheme.typography.titleMedium)

                    OutlinedTextField(value = panjangBadan, onValueChange = { panjangBadan = it }, label = { Text("Panjang Badan (cm)") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = lebarBadan, onValueChange = { lebarBadan = it }, label = { Text("Lebar Badan (cm)") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = pinggang, onValueChange = { pinggang = it }, label = { Text("Pinggang (cm)") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = pinggul, onValueChange = { pinggul = it }, label = { Text("Pinggul (cm)") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = lengan, onValueChange = { lengan = it }, label = { Text("Lengan (cm)") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = panjangLengan, onValueChange = { panjangLengan = it }, label = { Text("Panjang Lengan (cm)") }, modifier = Modifier.fillMaxWidth())
                }
            }

            Button(
                onClick = {
                    val panjangBadanInt = panjangBadan.toIntOrNull()
                    val lebarBadanInt = lebarBadan.toIntOrNull()
                    val pinggangInt = pinggang.toIntOrNull()
                    val pinggulInt = pinggul.toIntOrNull()
                    val lenganInt = lengan.toIntOrNull()
                    val panjangLenganInt = panjangLengan.toIntOrNull()

                    if (
                        nama.isBlank() || selectedJenisPesanan.isBlank() || jenisKain.isBlank() ||
                        panjangBadanInt == null || lebarBadanInt == null ||
                        pinggangInt == null || pinggulInt == null ||
                        lenganInt == null || panjangLenganInt == null
                    ) {
                        scope.launch {
                            snackbarHostState.showSnackbar("Semua kolom wajib diisi dengan benar.")
                        }
                    } else {
                        val pesananRef = FirebaseDatabase.getInstance().getReference("pesanan").push()
                        val data = mapOf(
                            "uid" to uid,
                            "namaUser" to nama,
                            "jenisPesanan" to selectedJenisPesanan,
                            "jenisKain" to jenisKain,
                            "status" to "Menunggu",
                            "ukuran" to mapOf(
                                "panjangBadan" to panjangBadanInt,
                                "lebarBadan" to lebarBadanInt,
                                "lebarPinggang" to pinggangInt,
                                "lebarPinggul" to pinggulInt,
                                "lebarLengan" to lenganInt,
                                "panjangLengan" to panjangLenganInt
                            )
                        )
                        pesananRef.setValue(data)
                        Toast.makeText(context, "Pesanan berhasil dikirim", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Kirim Pesanan")
            }
        }
    }
}