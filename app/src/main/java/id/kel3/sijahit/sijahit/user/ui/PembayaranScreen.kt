package id.kel3.sijahit.sijahit.user.ui

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.graphics.drawable.toBitmap

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PembayaranScreen(navController: NavController) {
    val dummyData = "https://dummy.qr/pembayaran-sijahit"
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Pembayaran") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(24.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Scan QR berikut untuk melakukan pembayaran:", style = MaterialTheme.typography.titleMedium)

            Spacer(modifier = Modifier.height(16.dp))

            QRCodeImage(data = dummyData)

            Spacer(modifier = Modifier.height(32.dp))

            Text("Metode: QRIS", style = MaterialTheme.typography.bodyMedium)
            Text("Status: Menunggu Pembayaran", style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun QRCodeImage(data: String) {
    val qrBitmap = remember(data) {
        val writer = QRCodeWriter()
        val bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, 400, 400)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bmp.setPixel(x, y, if (bitMatrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
            }
        }
        bmp
    }

    Image(
        bitmap = qrBitmap.asImageBitmap(),
        contentDescription = "QR Pembayaran",
        modifier = Modifier.size(240.dp)
    )
}