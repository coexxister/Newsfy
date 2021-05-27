package com.example.newsfy_rework

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignupActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_page)

        //Get view items
        progressBar = findViewById(R.id.progressBar)
        val inputEmail: EditText = findViewById(R.id.username)
        val inputPassword: EditText = findViewById(R.id.password)
        val btnSignIn: Button = findViewById(R.id.login)
        val btnSignUp: Button = findViewById(R.id.register)

        //Get Firebase auth instance
        val auth: FirebaseAuth = FirebaseAuth.getInstance()

        //Set listeners on button/text fields clicks
        btnSignIn.setOnClickListener {
            startActivity(Intent(this@SignupActivity, LoginActivity::class.java))
            finish() }
        btnSignUp.setOnClickListener {
            val email = inputEmail.text.toString().trim { it <= ' ' }
            val password = inputPassword.text.toString().trim { it <= ' ' }
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(applicationContext, "Enter email address!", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(applicationContext, "Enter password!", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            if (password.length < 6) {
                Toast.makeText(
                    applicationContext,
                    "Password too short, enter minimum 6 characters!",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            progressBar.visibility = View.VISIBLE
            //create user
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    this@SignupActivity
                ) { task: Task<AuthResult?> ->
                    Toast.makeText(
                        this@SignupActivity, "createUserWithEmail:onComplete:" +
                                task.isSuccessful, Toast.LENGTH_SHORT
                    ).show()
                    progressBar.visibility = View.GONE
                    // If sign in fails, display a message to the user. If sign in succeeds
                    // the auth state listener will be notified and logic to handle the
                    // signed in user can be handled in the listener.
                    if (!task.isSuccessful) {
                        Toast.makeText(
                            this@SignupActivity, "Authentication failed." + task.exception,
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        writeUserInfo(auth.uid.toString(),
                            intent.getStringArrayListExtra("preferences") as ArrayList<String>
                        )
                        startActivity(Intent(this@SignupActivity, Main::class.java))
                        finish()
                    }
                }
        }
    }

    override fun onResume() {
        super.onResume()
        progressBar.visibility = View.GONE
    }

    private fun writeUserInfo(userId: String, preferences: ArrayList<String>){
        database = FirebaseDatabase.getInstance().getReference("users")
        database.child(userId).setValue(preferences)
    }
}