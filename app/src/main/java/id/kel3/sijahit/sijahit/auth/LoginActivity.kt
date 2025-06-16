package id.kel3.sijahit.sijahit.auth

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import id.kel3.sijahit.sijahit.R
import id.kel3.sijahit.sijahit.admin.AdminHomeActivity
import id.kel3.sijahit.sijahit.user.UserHomeActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var login: Button
    private lateinit var register: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        email = findViewById(R.id.etEmail)
        password = findViewById(R.id.etPassword)
        login = findViewById(R.id.btnLogin)
        register = findViewById(R.id.tvDaftarSekarang)

        login.setOnClickListener {
            val emailText = email.text.toString()
            val passText = password.text.toString()

            if (emailText == getString(R.string.account)) {
                // Admin login manual
                startActivity(Intent(this, AdminHomeActivity::class.java))
                finish()
            } else {
                auth.signInWithEmailAndPassword(emailText, passText)
                    .addOnSuccessListener {
                        startActivity(Intent(this, UserHomeActivity::class.java))
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, getString(R.string.loginfail), Toast.LENGTH_SHORT).show()
                    }
            }
        }

        register.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
