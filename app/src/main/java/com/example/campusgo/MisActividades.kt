package com.example.campusgo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MisActividades : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mis_actividades)

        val btnAgregarActividad = findViewById<Button>(R.id.btnAgregarActividad)
        btnAgregarActividad.setOnClickListener {
            val intent = Intent(this, Registrar_actividad::class.java)
            startActivity(intent)
        }
    }
}

