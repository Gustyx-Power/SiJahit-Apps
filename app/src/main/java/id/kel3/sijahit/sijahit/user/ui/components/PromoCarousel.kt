package id.kel3.sijahit.sijahit.user.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import id.kel3.sijahit.sijahit.R
import kotlinx.coroutines.delay

@Composable
fun PromoCarousel() {
    val promoImages = listOf(
        R.drawable.promo1,
        R.drawable.promo2,
        R.drawable.promo3
    )

    val pagerState = rememberPagerState(initialPage = 0, pageCount = { promoImages.size })

    // Coroutine loop auto-scroll
    LaunchedEffect(Unit) {
        while (true) {
            delay(3000)
            val nextPage = (pagerState.currentPage + 1) % promoImages.size
            pagerState.animateScrollToPage(nextPage)
        }
    }

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
            Image(
                painter = painterResource(id = promoImages[page]),
                contentDescription = "Promo $page",
                contentScale = ContentScale.Crop
            )
        }
    }
}