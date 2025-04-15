package com.satnamsinghmaggo.crudmate

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class UpdateDetailsActivity : AppCompatActivity() {
    private lateinit var dbHelper: UserDatabaseHelper
    private var userId: Int = -1

    private lateinit var etName: EditText
    private lateinit var etAge: EditText
    private lateinit var etCity: EditText
    private lateinit var etState: EditText
    private lateinit var etPhone: EditText
    private lateinit var etAddress: EditText

    private lateinit var btnSave: Button
    private lateinit var btnDelete: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_details)

        dbHelper = UserDatabaseHelper(this)
        userId = intent.getIntExtra("userId", -1)

        // Initialize EditTexts
        etName = findViewById(R.id.etName)
        etAge = findViewById(R.id.etAge)
        etCity = findViewById(R.id.etCity)
        etState = findViewById(R.id.etState)
        etPhone = findViewById(R.id.etPhone)
        etAddress = findViewById(R.id.etAddress)

        // Initialize Buttons
        btnSave = findViewById(R.id.btnSave)
        btnDelete = findViewById(R.id.btnDelete)

        // Load user data to EditTexts
        if (userId != -1) {
            val user = dbHelper.getUserById(userId)
            user?.let {
                etName.setText(it.name)
                etAge.setText(it.age.toString())
                etCity.setText(it.city)
                etState.setText(it.state)
                etPhone.setText(it.phone)
                etAddress.setText(it.address)
            }
        }

        // Save button to update user details
        btnSave.setOnClickListener {
            val updatedUser = UserData(
                id = userId,
                name = etName.text.toString(),
                age = etAge.text.toString().toInt(),
                city = etCity.text.toString(),
                state = etState.text.toString(),
                phone = etPhone.text.toString(),
                address = etAddress.text.toString()
            )
            dbHelper.updateUser(updatedUser)
            setResult(RESULT_OK)
            Toast.makeText(this, "User updated", Toast.LENGTH_SHORT).show()
            finish()
        }

        // Delete button to delete the user
        btnDelete.setOnClickListener {
            dbHelper.deleteUser(userId)
            Toast.makeText(this, "User deleted", Toast.LENGTH_SHORT).show()
            finish() // Close this activity and return to UserDetails
        }
    }
}
