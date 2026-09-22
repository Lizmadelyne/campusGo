package com.example.campusgo

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        // Botones
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnRegister.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    // Menú superior
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_inicio, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.nav_inicio -> {
                mostrarInicio()
                true
            }

            R.id.nav_login -> {
                mostrarLogin()
                true
            }

            R.id.nav_registro -> {
                mostrarRegistro()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun mostrarRegistro() {
        startActivity(Intent(this, RegistroActivity::class.java ))
    }

    private fun mostrarLogin() {
        startActivity(Intent(this, LoginActivity::class.java ))
    }

    private fun mostrarInicio() {
        startActivity(Intent(this, HomeActivity::class.java ))
    }

}
