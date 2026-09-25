package com.example.campusgo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PanelEstudiante : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_panel_estudiante)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnMisActividades = findViewById<Button>(R.id.btnMisActividades)
        btnMisActividades.setOnClickListener {
            val intent = Intent(this, MisActividades::class.java)
            startActivity(intent)
        }
        val btnPerfil = findViewById<Button>(R.id.btnPerfil)
        btnPerfil.setOnClickListener {
            val intent = Intent(this, PerfilEstudiante::class.java)
            startActivity(intent)
        }
        val btnEvidencias = findViewById<Button>(R.id.btnEvidencias)
        btnEvidencias.setOnClickListener {
            val intent = Intent(this, evidencias::class.java)
            startActivity(intent)
        }
        val btnNotificaciones = findViewById<Button>(R.id.btnNotificaciones)
        btnNotificaciones.setOnClickListener {
            val intent = Intent(this, notificaciones::class.java)
            startActivity(intent)
        }


    }
}