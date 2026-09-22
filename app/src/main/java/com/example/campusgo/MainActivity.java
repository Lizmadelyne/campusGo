package com.example.campusgo;

import android.content.Intent; // Librería para realizar la navegación entre actividades
import android.os.Bundle; // Clase para pasar datos y gestionar el estado de la instancia
import android.widget.Button; // Componente de la interfaz de usuario para botones interactivos

import androidx.activity.EdgeToEdge; // Facilita el diseño inmersivo usando toda la pantalla
import androidx.appcompat.app.AppCompatActivity; // Clase base para actividades compatibles con características modernas
import androidx.core.graphics.Insets; // Almacena las dimensiones de las barras del sistema (estado/navegación)
import androidx.core.view.ViewCompat; // Proporciona compatibilidad para operaciones de vista en versiones antiguas
import androidx.core.view.WindowInsetsCompat; // Gestión avanzada de los márgenes del sistema

/**
 * Actividad Principal que actúa como el menú de navegación de la aplicación.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Habilita el diseño inmersivo Edge-to-Edge antes de inflar el diseño de la vista
        EdgeToEdge.enable(this);
        
        super.onCreate(savedInstanceState); // Llama al constructor de la clase superior obligatoriamente
        setContentView(R.layout.activity_main); // Enlaza el archivo de diseño XML con la actividad actual

        // Configura un Listener para controlar los márgenes del sistema y evitar superposiciones con los botones/barra de estado
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            // Obtiene los márgenes correspondientes a las barras del sistema (Barra de estado superior y Barra de navegación inferior)
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            // Aplica el relleno (padding) dinámico a la vista raíz utilizando los píxeles calculados
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets; // Retorna los insets modificados para continuar con la propagación en la jerarquía
        });

        // Localiza el botón de evidencias en el layout mediante su identificador único ID
        Button btnIrEvidencia = findViewById(R.id.btnIrEvidencia);
        // Asigna un escuchador de eventos click al botón de evidencias
        btnIrEvidencia.setOnClickListener(v -> {
            // Define una intención explícita para navegar desde el contexto actual hacia activity_evidencia
            Intent intent = new Intent(MainActivity.this, activity_evidencia.class);
            startActivity(intent); // Inicia la nueva actividad en el dispositivo
        });

        // Localiza el botón de notificaciones en el layout mediante su identificador único ID
        Button btnIrNotificacion = findViewById(R.id.btnIrNotificacion);
        // Asigna un escuchador de eventos click al botón de notificaciones
        btnIrNotificacion.setOnClickListener(v -> {
            // Define una intención explícita para navegar desde el contexto actual hacia activity_notificacion
            Intent intent = new Intent(MainActivity.this, activity_notificacion.class);
            startActivity(intent); // Inicia la nueva actividad en el dispositivo
        });
    }
}
