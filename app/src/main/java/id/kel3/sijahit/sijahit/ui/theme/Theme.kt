// file: ui/theme/Theme.kt
package id.kel3.sijahit.sijahit.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@Composable
fun SiJahitTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(), // bisa disesuaikan
        typography = Typography(),
        content = content
    )
}