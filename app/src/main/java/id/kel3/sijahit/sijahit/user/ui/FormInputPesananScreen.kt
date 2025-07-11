package id.kel3.sijahit.sijahit.user.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import id.kel3.sijahit.sijahit.user.components.UkuranField
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import id.kel3.sijahit.sijahit.user.model.UkuranData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormInputPesananScreen(navController: NavController) {
    val context = LocalContext.current

    // Input Fields
    var namaModel by remember { mutableStateOf("") }
    var deskripsi by remember { mutableStateOf("") }
    var jenisKain by remember { mutableStateOf("") }
    var panjangBadan by remember { mutableStateOf("") }
    var panjangLengan by remember { mutableStateOf("") }
    var lebarBadan by remember { mutableStateOf("") }
    var lebarPinggang by remember { mutableStateOf("") }
    var lebarPinggul by remember { mutableStateOf("") }
    var lebarLengan by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Pesanan Model Custom") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = namaModel,
                onValueChange = { namaModel = it },
                label = { Text("Nama Model") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = deskripsi,
                onValueChange = { deskripsi = it },
                label = { Text("Deskripsi (Opsional)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = jenisKain,
                onValueChange = { jenisKain = it },
                label = { Text("Jenis Kain") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Divider(Modifier.padding(vertical = 4.dp))

            UkuranField("Panjang Badan (cm)", panjangBadan) { panjangBadan = it }
            UkuranField("Panjang Lengan (cm)", panjangLengan) { panjangLengan = it }
            UkuranField("Lebar Badan (cm)", lebarBadan) { lebarBadan = it }
            UkuranField("Lebar Pinggang (cm)", lebarPinggang) { lebarPinggang = it }
            UkuranField("Lebar Pinggul (cm)", lebarPinggul) { lebarPinggul = it }
            UkuranField("Lebar Lengan (cm)", lebarLengan) { lebarLengan = it }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    if (
                        namaModel.isBlank() || jenisKain.isBlank() ||
                        listOf(
                            panjangBadan, panjangLengan, lebarBadan,
                            lebarPinggang, lebarPinggul, lebarLengan
                        ).any { it.isBlank() }
                    ) {
                        Toast.makeText(context, "Lengkapi semua data!", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val pb = panjangBadan.toIntOrNull()
                    val pl = panjangLengan.toIntOrNull()
                    val lb = lebarBadan.toIntOrNull()
                    val lpg = lebarPinggang.toIntOrNull()
                    val lpl = lebarPinggul.toIntOrNull()
                    val ll = lebarLengan.toIntOrNull()

                    if (listOf(pb, pl, lb, lpg, lpl, ll).any { it == null || it <= 0 }) {
                        Toast.makeText(context, "Ukuran harus berupa angka positif!", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val user = FirebaseAuth.getInstance().currentUser
                    val uid = user?.uid ?: return@Button
                    val dbRef = FirebaseDatabase.getInstance().getReference("users").child(uid).child("pesanan")
                    val id = "order${System.currentTimeMillis()}"



                    val ukuran = UkuranData(
                        panjangBadan.toInt(),
                        panjangLengan.toInt(),
                        lebarBadan.toInt(),
                        lebarPinggang.toInt(),
                        lebarPinggul.toInt(),
                        lebarLengan.toInt()
                    )

                    val pesanan = mapOf(
                        "id" to id,
                        "namaUser" to (user.displayName ?: "User"),
                        "jenisPesanan" to namaModel,
                        "deskripsi" to deskripsi,
                        "jenisKain" to jenisKain,
                        "status" to "Menunggu",
                        "total" to 0,
                        "timestamp" to System.currentTimeMillis(),
                        "ukuran" to ukuran
                    )

                    dbRef.child(id).setValue(pesanan).addOnSuccessListener {
                        Toast.makeText(context, "Pesanan custom dikirim!", Toast.LENGTH_SHORT).show()
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