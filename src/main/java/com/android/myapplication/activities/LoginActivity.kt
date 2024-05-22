package com.android.myapplication.activities

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.myapplication.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etName = findViewById<EditText>(R.id.text_username)
        val etPassword = findViewById<EditText>(R.id.text_password)
        val btnLogin = findViewById<Button>(R.id.login)


        btnLogin.setOnClickListener {

            val user: String = etName.text.toString()
            val password: String = etPassword.text.toString()

            if (user.isEmpty()) {
                etName.error = "Username is required"
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                etPassword.error = "Password is required"
                return@setOnClickListener
            }

            val preferences: SharedPreferences = getSharedPreferences("MYPREFS", MODE_PRIVATE)
            val userDetails: String? = preferences.getString(
                user + password + "data",
                "Username or Password is Incorrect."
            )
            val editor: SharedPreferences.Editor = preferences.edit()
            editor.putString("display", userDetails)
            editor.commit()

            val displayScreen = Intent(this@LoginActivity, MenuActivity::class.java)
            startActivity(displayScreen)
        }
    }

    }

