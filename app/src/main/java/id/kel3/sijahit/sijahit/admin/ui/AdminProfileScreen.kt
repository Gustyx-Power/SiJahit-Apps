package id.kel3.sijahit.sijahit.admin.ui

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import id.kel3.sijahit.sijahit.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

data class PromoItem(
    val id: String = "",
    val gambar: String = "",
    val judul: String = ""
)

@Composable
fun AdminProfileScreen() {
    var imageUrl by remember { mutableStateOf("") }
    var promoTitle by remember { mutableStateOf("") }
    var previewValid by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    var promoList by remember { mutableStateOf<List<PromoItem>>(emptyList()) }

    val user = FirebaseAuth.getInstance().currentUser
    val email = user?.email ?: "admin@sijahit.com"
    val dbRef = FirebaseDatabase.getInstance().getReference("promosi")

    // Fetch promo list on start
    LaunchedEffect(Unit) {
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val promos = mutableListOf<PromoItem>()
                for (child in snapshot.children) {
                    val item = child.getValue(PromoItem::class.java)
                    if (item != null) {
                        promos.add(item.copy(id = child.key ?: UUID.randomUUID().toString()))
                    }
                }
                promoList = promos
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(Modifier.height(16.dp))

        // Logo Admin
        Image(
            imageVector = Icons.Default.AdminPanelSettings,
            contentDescription = "Admin Icon",
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
        )

        Spacer(Modifier.height(12.dp))

        // Badge Admin
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Admin",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(Modifier.width(6.dp))
            Icon(
                imageVector = Icons.Default.VerifiedUser,
                contentDescription = "Verified",
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Text(text = email, style = MaterialTheme.typography.bodyMedium)

        Divider(modifier = Modifier.padding(vertical = 24.dp))

        // Input Form Promosi
        Text("Tambah Gambar Promosi", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = imageUrl,
            onValueChange = {
                imageUrl = it
                previewValid = it.startsWith("http") && (it.endsWith(".jpg") || it.endsWith(".png"))
            },
            label = { Text("Link Gambar (rasio 16:9)") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = promoTitle,
            onValueChange = { promoTitle = it },
            label = { Text("Judul Promosi") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
        )

        Spacer(Modifier.height(12.dp))

        if (previewValid) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                Image(
                    painter = rememberAsyncImagePainter(imageUrl),
                    contentDescription = "Preview",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .matchParentSize()
                        .graphicsLayer {
                            alpha = 0.98f
                        }
                        .drawWithCache {
                            val shimmer = Brush.linearGradient(
                                listOf(
                                    Color.Transparent,
                                    Color.White.copy(alpha = 0.4f),
                                    Color.Transparent
                                )
                            )
                            onDrawWithContent {
                                drawContent()
                                drawRect(shimmer)
                            }
                        }
                )
            }
        }

        if (showError) {
            Text(
                "Link gambar atau judul tidak valid!",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(Modifier.height(16.dp))

        Button(onClick = {
            if (imageUrl.isNotBlank() && promoTitle.isNotBlank() && previewValid) {
                val id = UUID.randomUUID().toString()
                val promo = mapOf(
                    "gambar" to imageUrl,
                    "judul" to promoTitle
                )
                dbRef.child(id).setValue(promo)
                imageUrl = ""
                promoTitle = ""
                previewValid = false
                showError = false
            } else {
                showError = true
            }
        }) {
            Text("Simpan Promosi")
        }

        Divider(modifier = Modifier.padding(vertical = 24.dp))

        // List Promo
        Text("Daftar Promosi", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(12.dp))

        if (promoList.isEmpty()) {
            Text("Belum ada promosi ditambahkan", style = MaterialTheme.typography.bodyMedium)
        } else {
            promoList.forEach { promo ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(promo.gambar),
                            contentDescription = promo.judul,
                            modifier = Modifier
                                .width(100.dp)
                                .height(56.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(promo.judul, style = MaterialTheme.typography.bodyMedium)
                        }

                        IconButton(onClick = {
                            dbRef.child(promo.id).removeValue()
                        }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Hapus",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }
}