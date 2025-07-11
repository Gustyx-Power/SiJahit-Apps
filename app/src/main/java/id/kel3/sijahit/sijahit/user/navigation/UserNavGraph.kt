package id.kel3.sijahit.sijahit.user.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import id.kel3.sijahit.sijahit.user.ui.*
import id.kel3.sijahit.sijahit.user.model.ModelJahitan
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState
import id.kel3.sijahit.sijahit.user.components.UserBottomNav

@Composable
fun UserNavGraphWrapper(navController: NavHostController) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute in listOf(
        "home", "pesanan_user", "bayar", "profil"
    )

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                val currentIndex = when (currentRoute) {
                    "home" -> 0
                    "pesanan_user" -> 1
                    "bayar" -> 2
                    "profil" -> 3
                    else -> 0
                }
                UserBottomNav(currentIndex) { selectedIndex ->
                    when (selectedIndex) {
                        0 -> navController.navigate("home") {
                            popUpTo("home") { inclusive = true }
                        }
                        1 -> navController.navigate("pesanan_user") {
                            popUpTo("home")
                        }
                        2 -> navController.navigate("bayar") {
                            popUpTo("home")
                        }
                        3 -> navController.navigate("profil") {
                            popUpTo("home")
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        UserNavGraph(navController = navController, modifierPadding = innerPadding)
    }
}

@Composable
fun UserNavGraph(navController: NavHostController, modifierPadding: PaddingValues) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            UserHomeScreen(navController)
        }
        composable("pesanan_user") {
            UserPesananScreen(navController)
        }
        composable("form_pesanan_user") {
            FormPesananUserScreen(navController)
        }
        composable("bayar") {
            // Tambahkan layar pembayaran di sini
            Text("Halaman Bayar (coming soon)") // ganti nanti sesuai kebutuhan
        }
        composable("profil") {
            // Tambahkan layar profil di sini
            Text("Halaman Profil (coming soon)") // ganti nanti sesuai kebutuhan
        }
        composable("model_list") {
            ModelListScreen(navController)
        }
        composable(
            route = "form_ukuran/{nama}/{deskripsi}/{gambar}",
            arguments = listOf(
                navArgument("nama") { type = NavType.StringType },
                navArgument("deskripsi") { type = NavType.StringType },
                navArgument("gambar") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val nama = backStackEntry.arguments?.getString("nama") ?: ""
            val deskripsi = backStackEntry.arguments?.getString("deskripsi") ?: ""
            val gambar = backStackEntry.arguments?.getString("gambar") ?: ""

            val model = ModelJahitan(nama, deskripsi, 0, gambar)
            FormUkuranScreen(navController, model)
        }
    }
}