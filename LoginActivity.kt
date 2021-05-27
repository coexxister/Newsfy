package com.example.newsfy_rework

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page)

        //Get view items
        progressBar = findViewById(R.id.progressBar)
        val inputEmail: EditText = findViewById(R.id.username)
        val inputPassword: EditText = findViewById(R.id.password)
        val btnLogin: Button = findViewById(R.id.login)
        val btnSignUp: Button = findViewById(R.id.register)

        //Get Firebase auth instance
        val auth: FirebaseAuth = FirebaseAuth.getInstance()

        //Set listeners on button/text fields clicks
        btnSignUp.setOnClickListener {
            startActivity(Intent(this@LoginActivity, PreferencesActivity::class.java))
        }

        btnLogin.setOnClickListener(View.OnClickListener {
            val email = inputEmail.text.toString()
            val password = inputPassword.text.toString()
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(
                    applicationContext,
                    "Enter email address!",
                    Toast.LENGTH_SHORT
                ).show()
                return@OnClickListener
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(
                    applicationContext,
                    "Enter password!",
                    Toast.LENGTH_SHORT
                ).show()
                return@OnClickListener
            }
            progressBar.visibility = View.VISIBLE
            //authenticate user
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    this@LoginActivity
                ) { task ->
                    // If sign in fails, display a message to the user. If sign in succeeds
                    // the auth state listener will be notified and logic to handle the
                    // signed in user can be handled in the listener.
                    progressBar.visibility = View.GONE
                    if (!task.isSuccessful) {
                        // there was an error
                        if (password.length < 6) {
                            inputPassword.error = getString(R.string.minimum_password)
                        } else {
                            Toast.makeText(
                                this@LoginActivity,
                                getString(R.string.auth_failed),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } else {
                        val intent =
                            Intent(this@LoginActivity, Main::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
        })
    }
}
