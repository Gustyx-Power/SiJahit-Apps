package id.kel3.sijahit.sijahit.auth

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import id.kel3.sijahit.sijahit.R
import id.kel3.sijahit.sijahit.user.UserHomeActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var register: Button
    private lateinit var back: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()
        email = findViewById(R.id.etEmail)
        password = findViewById(R.id.etPassword)
        register = findViewById(R.id.btnRegister)
        back = findViewById(R.id.btnBack)

        register.setOnClickListener {
            val emailText = email.text.toString()
            val passText = password.text.toString()

            auth.createUserWithEmailAndPassword(emailText, passText)
                .addOnSuccessListener {
                    startActivity(Intent(this, UserHomeActivity::class.java))
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, getString(R.string.fail_register), Toast.LENGTH_SHORT)
                        .show()
                }
        }
        back.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}
