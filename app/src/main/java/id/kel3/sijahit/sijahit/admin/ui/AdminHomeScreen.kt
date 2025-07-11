package id.kel3.sijahit.sijahit.admin.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import id.kel3.sijahit.sijahit.admin.viewmodel.AdminHomeViewModel
import androidx.compose.runtime.collectAsState

@Composable
fun AdminHomeScreen(viewModel: AdminHomeViewModel = viewModel()) {
    val saldo = viewModel.saldo.collectAsState().value
    val pesananAktif = viewModel.pesananAktif.collectAsState().value
    val stokMenipis = viewModel.stokMenipis.collectAsState().value
    val konfirmasi = viewModel.konfirmasi.collectAsState().value
    val admin = viewModel.adminCount.collectAsState().value
    val selesai = viewModel.pesananSelesai.collectAsState().value
    val produk = viewModel.totalProduk.collectAsState().value
    val pelanggan = viewModel.pelanggan.collectAsState().value

    AdminDashboardScreen(
        saldo = saldo,
        pesananAktif = pesananAktif,
        stokMenipis = stokMenipis,
        konfirmasiMenunggu = konfirmasi,
        adminCount = admin,
        pesananSelesai = selesai,
        totalProduk = produk,
        pelanggan = pelanggan
    )
}