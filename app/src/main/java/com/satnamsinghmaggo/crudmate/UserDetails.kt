package com.satnamsinghmaggo.crudmate

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class UserDetails : AppCompatActivity() {
    private lateinit var dbHelper: UserDatabaseHelper
    private var userId: Int = -1

    private lateinit var tvName: TextView
    private lateinit var tvAge: TextView
    private lateinit var tvCity: TextView
    private lateinit var tvState: TextView
    private lateinit var tvPhone: TextView
    private lateinit var tvAddress: TextView

    private lateinit var btnEdit: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_details)

        dbHelper = UserDatabaseHelper(this)
        userId = intent.getIntExtra("userId", -1)

        // Initialize TextViews
        tvName = findViewById(R.id.tvName)
        tvAge = findViewById(R.id.tvAge)
        tvCity = findViewById(R.id.tvCity)
        tvState = findViewById(R.id.tvState)
        tvPhone = findViewById(R.id.tvPhone)
        tvAddress = findViewById(R.id.tvAddress)

        // Initialize Edit Button
        btnEdit = findViewById(R.id.btnEdit)

        if (userId != -1) {
            val user = dbHelper.getUserById(userId)
            if (user != null) {
                // populate TextViews with user data
            } else {
                Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
        }

        // Load user details
        if (userId != -1) {
            val user = dbHelper.getUserById(userId)
            user?.let {
                tvName.text = it.name
                tvAge.text = it.age.toString()
                tvCity.text = it.city
                tvState.text = it.state
                tvPhone.text = it.phone
                tvAddress.text = it.address
            }
        }

        // Edit button opens UpdateDetailsActivity
        btnEdit.setOnClickListener {
            val intent = Intent(this, UpdateDetailsActivity::class.java)
            intent.putExtra("userId", userId)
            startActivityForResult(intent, 1001)

        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1001 && resultCode == RESULT_OK) {
            // Reload the updated user from DB
            val updatedUser = dbHelper.getUserById(userId)
            updatedUser?.let {
                tvName.text = it.name
                tvAge.text = it.age.toString()
                tvCity.text = it.city
                tvState.text = it.state
                tvPhone.text = it.phone
                tvAddress.text = it.address
            }
        }
    }

    fun getCurrentUserId(): Int {
        val sharedPref = getSharedPreferences("userSession", MODE_PRIVATE)
        return sharedPref.getInt("userId", -1)
    }


}
