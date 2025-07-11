package id.kel3.sijahit.sijahit.user.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.firebase.database.*
import kotlinx.coroutines.delay

data class Promo(
    val gambar: String = "",
    val judul: String = ""
)

@Composable
fun PromoCarousel() {
    var promoList by remember { mutableStateOf<List<Promo>>(emptyList()) }

    val pagerState = rememberPagerState(initialPage = 0, pageCount = { promoList.size })

    // Fetch promosi dari Firebase
    LaunchedEffect(Unit) {
        val dbRef = FirebaseDatabase.getInstance().getReference("promosi")
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val promos = mutableListOf<Promo>()
                for (item in snapshot.children) {
                    val promo = item.getValue(Promo::class.java)
                    promo?.let {
                        if (it.gambar.isNotBlank()) promos.add(it)
                    }
                }
                promoList = promos
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    // Auto-scroll tiap 3 detik
    LaunchedEffect(promoList) {
        while (promoList.isNotEmpty()) {
            delay(3000)
            val nextPage = (pagerState.currentPage + 1) % promoList.size
            pagerState.animateScrollToPage(nextPage)
        }
    }

    if (promoList.isNotEmpty()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .padding(horizontal = 16.dp)
        ) { page ->
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp)),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                AsyncImage(
                    model = promoList[page].gambar,
                    contentDescription = promoList[page].judul,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}