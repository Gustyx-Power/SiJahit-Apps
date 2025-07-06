package id.kel3.sijahit.sijahit.auth

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import id.kel3.sijahit.sijahit.R
import id.kel3.sijahit.sijahit.user.UserHomeActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnRegister = findViewById(R.id.btnRegister)

        btnRegister.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Isi semua data", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.length < 6) {
                Toast.makeText(this, "Password minimal 6 karakter", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    simpanSebagaiUser(email)
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Gagal mendaftar: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }

    }

    private fun simpanSebagaiUser(email: String) {
        val emailKey = email.replace(".", "_").replace("@", "_").lowercase()
        val roleRef = FirebaseDatabase.getInstance().getReference("roles")

        roleRef.child(emailKey).setValue("user")
            .addOnSuccessListener {
                startActivity(Intent(this, UserHomeActivity::class.java))
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Gagal menyimpan role", Toast.LENGTH_SHORT).show()
            }
    }
}