package id.kel3.sijahit.sijahit.user

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import id.kel3.sijahit.sijahit.R

class UserHomeActivity : AppCompatActivity() {

    private lateinit var navHome: LinearLayout
    private lateinit var navPesan: LinearLayout
    private lateinit var navRiwayat: LinearLayout
    private lateinit var navProfil: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_home)

        // Inisialisasi footer navigation
        navHome = findViewById(R.id.nav_home)
        navPesan = findViewById(R.id.nav_pesan)
        navRiwayat = findViewById(R.id.nav_riwayat)
        navProfil = findViewById(R.id.nav_profil)

        // Home tidak melakukan apa-apa karena sudah di sini
        navHome.setOnClickListener {
            // Do nothing
        }

        navPesan.setOnClickListener {
            startActivity(Intent(this, FormActivity::class.java))
            overridePendingTransition(0, 0)
        }

        navRiwayat.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
            overridePendingTransition(0, 0)
        }

        navProfil.setOnClickListener {
            startActivity(Intent(this, ProfileUser::class.java))
            overridePendingTransition(0, 0)
        }
    }
}