package id.kel3.sijahit.sijahit.auth

import id.kel3.sijahit.sijahit.user.UserHomeActivity
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import id.kel3.sijahit.sijahit.R
import id.kel3.sijahit.sijahit.admin.AdminHomeActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private val splashDelay = 3000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            val currentUser = FirebaseAuth.getInstance().currentUser

            if (currentUser != null) {
                // Sudah login → cek role
                val emailKey = currentUser.email
                    ?.replace(".", "_")
                    ?.replace("@", "_")
                    ?.lowercase()

                val rolesRef = FirebaseDatabase.getInstance().getReference("roles")
                rolesRef.child(emailKey!!).get().addOnSuccessListener { snapshot ->
                    val role = snapshot.getValue(String::class.java)
                    when (role) {
                        "admin" -> {
                            startActivity(Intent(this, AdminHomeActivity::class.java))
                            finish()
                        }
                        "user", null -> {
                            startActivity(Intent(this, UserHomeActivity::class.java))
                            finish()
                        }
                        else -> {
                            // Role aneh → fallback ke login
                            startActivity(Intent(this, LoginActivity::class.java))
                            finish()
                        }
                    }
                }.addOnFailureListener {
                    // Gagal ambil role → fallback ke login
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }

            } else {
                // Belum login
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }

        }, splashDelay)
    }
}