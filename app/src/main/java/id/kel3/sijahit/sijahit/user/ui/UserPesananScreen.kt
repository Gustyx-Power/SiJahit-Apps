package id.kel3.sijahit.sijahit.user.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import id.kel3.sijahit.sijahit.user.viewmodel.UserPesananViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserPesananScreen(
    navController: NavController,
    viewModel: UserPesananViewModel = UserPesananViewModel()
) {
    val pesananList by viewModel.pesananList.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Pesanan Saya") })
        }
    ) { padding ->
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(padding)
        ) {
            items(pesananList) { pesanan ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Nama: ${pesanan.namaUser}")
                        Text("Jenis Pesanan: ${pesanan.jenisPesanan}")
                        Text("Jenis Kain: ${pesanan.jenisKain}")
                        Text("Status: ${pesanan.status}")
                        Spacer(Modifier.height(8.dp))
                        Text("Detail Ukuran:")
                        Text("- Panjang Badan: ${pesanan.ukuran.panjangBadan} cm")
                        Text("- Lebar Badan: ${pesanan.ukuran.lebarBadan} cm")
                        Text("- Pinggang: ${pesanan.ukuran.lebarPinggang} cm")
                        Text("- Pinggul: ${pesanan.ukuran.lebarPinggul} cm")
                        Text("- Lengan: ${pesanan.ukuran.lebarLengan} cm")
                        Text("- Panjang Lengan: ${pesanan.ukuran.panjangLengan} cm")
                    }
                }
            }
        }
    }
}