package id.kel3.sijahit.sijahit.user.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import id.kel3.sijahit.sijahit.user.model.ModelJahitan
import id.kel3.sijahit.sijahit.user.model.UkuranData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormUkuranScreen(navController: NavController, model: ModelJahitan) {
    val context = LocalContext.current

    // State input ukuran
    var panjangBadan by remember { mutableStateOf("") }
    var panjangLengan by remember { mutableStateOf("") }
    var lebarBadan by remember { mutableStateOf("") }
    var lebarPinggang by remember { mutableStateOf("") }
    var lebarPinggul by remember { mutableStateOf("") }
    var lebarLengan by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Form Ukuran") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Model: ${model.nama}", style = MaterialTheme.typography.titleLarge)
            Text(model.deskripsi, style = MaterialTheme.typography.bodyMedium)

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // Input Field Ukuran
            UkuranField("Panjang Badan (cm)", panjangBadan) { panjangBadan = it }
            UkuranField("Panjang Lengan (cm)", panjangLengan) { panjangLengan = it }
            UkuranField("Lebar Badan (cm)", lebarBadan) { lebarBadan = it }
            UkuranField("Lebar Pinggang (cm)", lebarPinggang) { lebarPinggang = it }
            UkuranField("Lebar Pinggul (cm)", lebarPinggul) { lebarPinggul = it }
            UkuranField("Lebar Lengan (cm)", lebarLengan) { lebarLengan = it }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    val inputList = listOf(
                        panjangBadan, panjangLengan,
                        lebarBadan, lebarPinggang,
                        lebarPinggul, lebarLengan
                    )

                    if (inputList.any { it.isBlank() }) {
                        Toast.makeText(context, "Lengkapi semua ukuran!", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val user = FirebaseAuth.getInstance().currentUser
                    if (user == null) {
                        Toast.makeText(context, "User tidak ditemukan, silakan login kembali.", Toast.LENGTH_SHORT).show()
                        // Optionally navigate to login screen
                        // navController.navigate("login_route")
                        return@Button
                    }
                    val uid = user.uid
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
                        "jenisPesanan" to model.nama,
                        "jenisKain" to "Belum dipilih",
                        "status" to "Menunggu",
                        "total" to 0,
                        "timestamp" to System.currentTimeMillis(),
                        "ukuran" to ukuran
                    )

                    dbRef.child(id).setValue(pesanan).addOnSuccessListener {
                        Toast.makeText(context, "Pesanan berhasil dikirim!", Toast.LENGTH_SHORT).show()
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

@Composable
fun UkuranField(label: String, value: String, onChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = { onChange(it.filter { c -> c.isDigit() }) },
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )
}