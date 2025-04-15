package com.satnamsinghmaggo.crudmate

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LoginPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)

        val textview = findViewById<TextView>(R.id.tvSignup)
        val editText = findViewById<EditText>(R.id.emailET)
        val editText2 = findViewById<EditText>(R.id.passwordET)
        val loginButton = findViewById<Button>(R.id.btnLogin)

        val checkBox = findViewById<CheckBox>(R.id.rememberMeCB)
        val sharedPref = getSharedPreferences("userSession", MODE_PRIVATE)

// Pre-fill if Remember Me was checked before
        val rememberMe = sharedPref.getBoolean("rememberMe", false)
        if (rememberMe) {
            val savedName = sharedPref.getString("Full Name", null)
            val savedUserid = sharedPref.getString("Userid", null)
            editText.setText(savedName)
            editText2.setText(savedUserid)
            checkBox.isChecked = true
        }

// Login button logic
        loginButton.setOnClickListener {
            val enteredName = editText.text.toString().trim()
            val enteredUserid = editText2.text.toString().trim()

            if (enteredName.isEmpty() || enteredUserid.isEmpty()) {
                Toast.makeText(this, "Please enter Full name and Userid", Toast.LENGTH_SHORT).show()
            } else {
                val savedName = sharedPref.getString("Full Name", null)
                val savedUserid = sharedPref.getString("Userid", null)

                if (savedName == null || savedUserid == null) {
                    Toast.makeText(this, "No account found. Please sign up first.", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, SignUpPage::class.java))
                } else if (enteredName == savedName && enteredUserid == savedUserid) {
                    with(sharedPref.edit()) {
                        putBoolean("isLoggedIn", true)
                        putBoolean("rememberMe", checkBox.isChecked)
                        if (checkBox.isChecked) {
                            putString("Full Name", enteredName)
                            putString("Userid", enteredUserid)
                        }
                        apply()
                    }

                    Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Invalid Name or UserId", Toast.LENGTH_SHORT).show()
                }
            }
        }



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
