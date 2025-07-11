package id.kel3.sijahit.sijahit.user.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import id.kel3.sijahit.sijahit.user.ui.UserHomeScreen
import id.kel3.sijahit.sijahit.user.ui.FormUkuranScreen
import id.kel3.sijahit.sijahit.user.ui.ModelListScreen
import id.kel3.sijahit.sijahit.user.model.ModelJahitan // atau dari admin.viewmodel jika di sana
import id.kel3.sijahit.sijahit.user.ui.FormInputPesananScreen
import id.kel3.sijahit.sijahit.user.ui.FormPesananUserScreen
import id.kel3.sijahit.sijahit.user.ui.PembayaranScreen
import id.kel3.sijahit.sijahit.user.ui.ProfileScreen
import id.kel3.sijahit.sijahit.user.ui.UserPesananScreen

@Composable
fun UserNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            UserHomeScreen(navController)
        }
        composable("model_list") {
            ModelListScreen(navController)
        }
        composable("form_ukuran/{nama}/{deskripsi}/{gambar}") { backStackEntry -> }
        composable("form_input_custom") {
            FormInputPesananScreen(navController)
        }
        // Tambahkan composable untuk FormUkuranScreen jika belum ada
        composable("form_ukuran") {
            FormUkuranScreen(navController = navController, model = ModelJahitan("", "", 0, "")) // Sediakan model default atau ambil dari ViewModel
        }
        composable("pesanan_user") {
            UserPesananScreen(navController)
        }
        composable("form_pesanan_user") {
            FormPesananUserScreen(navController)
        }
        composable("pembayaran") {
            PembayaranScreen(navController)
        }
        composable("profil") {
            ProfileScreen(navController)
        }
    }
}

