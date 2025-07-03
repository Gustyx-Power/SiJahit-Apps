package id.kel3.sijahit.sijahit.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase
import id.kel3.sijahit.sijahit.R
import id.kel3.sijahit.sijahit.admin.AdminHomeActivity
import id.kel3.sijahit.sijahit.user.UserHomeActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var login: Button
    private lateinit var register: TextView
    private lateinit var google: ImageButton
    private lateinit var googleSignInClient: GoogleSignInClient

    private val RC_GOOGLE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Inisialisasi FirebaseAuth
        auth = FirebaseAuth.getInstance()

        // Inisialisasi komponen UI
        email = findViewById(R.id.etEmail)
        password = findViewById(R.id.etPassword)
        login = findViewById(R.id.btnLogin)
        register = findViewById(R.id.tvDaftarSekarang)
        google = findViewById(R.id.btnGoogleLogin)

        // Konfigurasi Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))  // dari strings.xml
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Tombol Login Email
        login.setOnClickListener {
            val emailText = email.text.toString().trim()
            val passText = password.text.toString().trim()

            if (emailText.isEmpty() || passText.isEmpty()) {
                Toast.makeText(this, "Isi email dan password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(emailText, passText)
                .addOnSuccessListener {
                    Log.d("LOGIN_FLOW", "Login Email/Password berhasil")
                    cekRole(emailText)
                }
                .addOnFailureListener {
                    Log.e("LOGIN_FLOW", "Login Email/Password gagal: ${it.message}")
                    Toast.makeText(this, getString(R.string.loginfail), Toast.LENGTH_SHORT).show()
                }
        }

        // Tombol Google Sign-In
        google.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_GOOGLE)
        }

        // Tombol Daftar
        register.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.d(
            "LOGIN_FLOW",
            "onActivityResult called with requestCode=$requestCode resultCode=$resultCode"
        )

        if (requestCode == RC_GOOGLE) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    Log.d("LOGIN_FLOW", "Google Sign-In berhasil: ${account.email}")
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)

                    auth.signInWithCredential(credential)
                        .addOnSuccessListener {
                            Log.d("LOGIN_FLOW", "signInWithCredential berhasil")
                            val emailText = account.email ?: ""
                            cekRole(emailText)
                        }
                        .addOnFailureListener {
                            Log.e("LOGIN_FLOW", "signInWithCredential gagal: ${it.message}")
                            Toast.makeText(this, "Login Google gagal", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Log.e("LOGIN_FLOW", "Account Google null")
                }

            } catch (e: ApiException) {
                Log.e("LOGIN_FLOW", "Google Sign-In error: ${e.message}")
                Toast.makeText(this, "Login Google dibatalkan", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun cekRole(email: String) {
        val emailKey = email.replace(".", "_").replace("@", "_").lowercase()
        val rolesRef = FirebaseDatabase.getInstance().getReference("roles")

        Log.d("ROLE_CHECK", "Cek role untuk: $email → key: $emailKey")

        rolesRef.child(emailKey).get().addOnSuccessListener { snapshot ->
            Log.d("ROLE_CHECK", "Snapshot exists: ${snapshot.exists()}")
            Log.d("ROLE_CHECK", "Snapshot value: ${snapshot.value}")

            val role = snapshot.getValue(String::class.java)
            Log.d("ROLE_CHECK", "Role: $role")

            when (role?.lowercase()) {
                "admin" -> {
                    Log.d("ROLE_CHECK", "Arahkan ke AdminHomeActivity")
                    startActivity(Intent(this, AdminHomeActivity::class.java))
                    finish()
                }

                "user", null -> {
                    Log.d("ROLE_CHECK", "Arahkan ke UserHomeActivity (default)")
                    startActivity(Intent(this, UserHomeActivity::class.java))
                    finish()
                }

                else -> {
                    Log.e("ROLE_CHECK", "Role tidak dikenal: $role")
                    Toast.makeText(this, "Role tidak dikenali: $role", Toast.LENGTH_SHORT).show()
                }
            }
        }.addOnFailureListener {
            Log.e("ROLE_CHECK", "Gagal mengambil role: ${it.message}")
            Toast.makeText(this, "Gagal mengambil role", Toast.LENGTH_SHORT).show()
        }
    }
}