package com.satnamsinghmaggo.crudmate

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isEmpty
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: UserDatabaseHelper
    private lateinit var userList: List<UserData>
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: UserAdapter

    //main actvity of the app
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = UserDatabaseHelper(this)
        recyclerView = findViewById(R.id.recyclerViewRV)
        val textview : TextView = findViewById(R.id.tvNotify)
        recyclerView.layoutManager = LinearLayoutManager(this)


        textview.setOnClickListener {
            startActivity(Intent(this, SignUpPage::class.java))
        }

        loadUserData()

        val drawerLayout = findViewById<DrawerLayout>(R.id.main)
        val navigationView = findViewById<NavigationView>(R.id.navigationView)

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_profile -> {
                    val sharedPref = getSharedPreferences("userSession", MODE_PRIVATE)
                    val currentUserId = sharedPref.getInt("userId", -1)

                    if (currentUserId != -1) {
                        val intent = Intent(this, UserDetails::class.java)
                        intent.putExtra("userId", currentUserId)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
                    }

                    drawerLayout.closeDrawers()
                    true
                }

                R.id.nav_delete -> {
                    Toast.makeText(this, "Delete clicked", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_profile -> {
                    // Maybe reload or show UserDetails
                    startActivity(Intent(this, UserDetails::class.java))
                    finish()
                    true
                }
                R.id.nav_logout -> {
                    val sharedPref = getSharedPreferences("userSession", MODE_PRIVATE)
                    sharedPref.edit().clear().apply()
                    startActivity(Intent(this, LoginPage::class.java))
                    finish()
                    true
                }
                else -> false
            }

        }

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()





        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun loadUserData() {
        userList = dbHelper.getAllUsers()
        adapter = UserAdapter(userList) { user ->
            val intent = Intent(this, UserDetails::class.java)
            intent.putExtra("userId", user.id)
            startActivity(intent)
        }
        recyclerView.adapter = adapter

        val textView: TextView = findViewById(R.id.tvNotify)

        if (adapter.itemCount == 0) {
            textView.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show()
        } else {
            textView.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
    }


    override fun onResume() {
        super.onResume()
        loadUserData() // Refresh user list when returning
    }
}
