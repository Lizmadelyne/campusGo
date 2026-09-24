package com.example.campusgo;

import android.os.Bundle; // Pasa configuraciones del sistema y controla estados de ciclo de vida
import android.widget.Toast; // Muestra burbujas de texto flotantes e informativas rápidas en pantalla

import androidx.activity.EdgeToEdge; // Permite renderizar vistas completas por debajo de las barras del OS
import androidx.appcompat.app.AppCompatActivity; // Proporciona soporte base y consistencia visual a la actividad
import androidx.appcompat.widget.Toolbar; // Barra de herramientas superior para títulos y navegación
import androidx.core.graphics.Insets; // Gestiona dimensiones de las barras nativas del sistema operativo
import androidx.core.view.ViewCompat; // Compatibilidad unificada para operaciones y eventos sobre vistas
import androidx.core.view.WindowInsetsCompat; // Administra colecciones de inserciones de ventanas de pantalla

import com.google.android.material.materialswitch.MaterialSwitch; // Interruptor moderno con directrices de Material Design 3

/**
 * Actividad encargada de gestionar y parametrizar las alertas temporales de entrega.
 */
public class activity_notificacion extends AppCompatActivity {

    // Variables globales para mapear los interruptores lógicos de la interfaz gráfica
    private MaterialSwitch switchPrincipal; // Interruptor maestro para activar/desactivar todas las alertas
    private MaterialSwitch switch1Dia;
    private MaterialSwitch switch3Dias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Llama al constructor de inicialización de la superclase
        EdgeToEdge.enable(this); // Fuerza el modo inmersivo de dibujo de borde a borde en la pantalla
        setContentView(R.layout.activity_notificacion); // Infla el archivo XML y dibuja la interfaz de notificaciones

        // Configura el ajuste de márgenes internos para que el diseño respete las barras del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars()); // Obtiene dimensiones físicas de las barras
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom); // Aplica rellenos protectores
            return insets; // Retorna las especificaciones calculadas de la ventana
        });

        // Configuración de la Barra Superior de Herramientas (Toolbar) con navegación inversa
        Toolbar toolbar = findViewById(R.id.toolbar); // Localiza el componente Toolbar en el diseño por su ID
        setSupportActionBar(toolbar); // Configura este Toolbar para actuar como la ActionBar principal de la pantalla
        if (getSupportActionBar() != null) { // Valida el éxito del registro de la barra de soporte
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Inserta el botón visual de la flecha de navegación hacia atrás
            getSupportActionBar().setDisplayShowHomeEnabled(true); // Activa el comportamiento interactivo del botón home
        }
        // Registra la acción click sobre la flecha superior llamando al metodo nativo de retroceso
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // Vinculación de los controles de interfaz gráfica con los objetos lógicos Java
        switchPrincipal = findViewById(R.id.switchPrincipal); // Enlaza el interruptor maestro general
        switch1Dia = findViewById(R.id.switch1Dia); // Enlaza el interruptor de alerta para 1 día previo
        switch3Dias = findViewById(R.id.switch3Dias); // Enlaza el interruptor de alerta para 3 días previos

        // Implementación del escuchador de cambios de estado (Checked/Unchecked) para el interruptor maestro
        switchPrincipal.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Modifica el estado de habilitación física de los interruptores secundarios según el valor del principal
            switch1Dia.setEnabled(isChecked); // Si el principal es true se habilita; si es false se deshabilita
            switch3Dias.setEnabled(isChecked); // Aplica la misma condición de habilitación para el interruptor de 3 días

            if (isChecked) { // Si el interruptor principal fue encendido por el usuario
                // Notifica al usuario de forma flotante el encendido del sistema general de alertas
                Toast.makeText(this, "Notificaciones activadas", Toast.LENGTH_SHORT).show();
            } else { // Si el interruptor principal fue apagado de forma explícita
                // Apaga visualmente y fuerza el estado falso en ambos interruptores secundarios por consistencia
                switch1Dia.setChecked(false);
                switch3Dias.setChecked(false);
                // Notifica al usuario de forma flotante la desactivación total del sistema de alertas
                Toast.makeText(this, "Notificaciones desactivadas", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
