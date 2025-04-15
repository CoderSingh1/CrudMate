package com.satnamsinghmaggo.crudmate

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SignUpPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_page)
        val etName = findViewById<EditText>(R.id.etFullName)
        val etUserid = findViewById<EditText>(R.id.etUserid)
        val etAge = findViewById<EditText>(R.id.etAge)
        val etCity = findViewById<EditText>(R.id.etCity)
        val etState = findViewById<EditText>(R.id.etState)
        val etPhone = findViewById<EditText>(R.id.etPhone)
        val etAddress = findViewById<EditText>(R.id.etAddress)
        val btnRegister = findViewById<Button>(R.id.btnRegister)

        val dbHelper = UserDatabaseHelper(this)

        val sharedPref = getSharedPreferences("userSession", MODE_PRIVATE)
        val editor = sharedPref.edit()

        btnRegister.setOnClickListener {
            val name = etName.text.toString().trim()
            val Userid = etUserid.text.toString().trim()
            val age = etAge.text.toString().trim()
            val city = etCity.text.toString().trim()
            val state = etState.text.toString().trim()
            val phone = etPhone.text.toString().trim()
            val address = etAddress.text.toString().trim()

            if (name.isEmpty() || Userid.isEmpty() || age.isEmpty() || city.isEmpty()
                || state.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else {
                val user = UserData(
                    name = name,
                    age = age.toInt(),
                    city = city,
                    state = state,
                    phone = phone,
                    address = address
                )

                dbHelper.addUser(user)
                editor.putString("Full Name", name)
                editor.putString("Userid", Userid)
                editor.putBoolean("isLoggedIn", true)
                editor.apply()

                Toast.makeText(this, "Sign up successful", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
