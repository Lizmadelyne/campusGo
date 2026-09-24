package com.example.campusgo;

import android.Manifest; // Acceso a las constantes de permisos del manifiesto del sistema
import android.content.Intent; // Permite crear peticiones e intercambiar flujos de pantalla
import android.content.pm.PackageManager; // Verifica el estado de concesión de permisos del sistema
import android.graphics.Bitmap; // Maneja mapas de bits en memoria para las imágenes capturadas
import android.os.Bundle; // Guarda estados de la aplicación e inicios de ciclo de vida
import android.provider.MediaStore; // Ofrece accesos directos a intents multimedia predefinidos como la cámara
import android.view.View; // Clase base para componentes visuales
import android.widget.Button; // Componente botón de la interfaz
import android.widget.ImageView; // Componente para desplegar imágenes en pantalla
import android.widget.LinearLayout; // Contenedor lineal para posicionar elementos de forma consecutiva
import android.widget.Toast; // Muestra notificaciones flotantes rápidas e informativas en pantalla

import androidx.activity.EdgeToEdge; // Permite un renderizado completo aprovechando barras de estado
import androidx.activity.result.ActivityResultLauncher; // Lanzador oficial seguro para peticiones asíncronas
import androidx.activity.result.contract.ActivityResultContracts; // Contratos estándar prefabricados para llamadas de actividades
import androidx.appcompat.app.AppCompatActivity; // Clase base para retrocompatibilidad global
import androidx.appcompat.widget.Toolbar; // Barra de herramientas personalizada para navegación superior
import androidx.cardview.widget.CardView; // Contenedor con bordes redondeados y sombreado estético
import androidx.core.content.ContextCompat; // Validador de permisos compatible con múltiples versiones de Android
import androidx.core.graphics.Insets; // Administra las medidas de inserción de barras del OS
import androidx.core.view.ViewCompat; // Soporte modular de insets para vistas
import androidx.core.view.WindowInsetsCompat; // Tipos de inserciones de pantalla

import java.util.ArrayList; // Colección basada en arreglos redimensionables
import java.util.List; // Interfaz estándar para el manejo secuencial de listas de objetos

/**
 * Actividad encargada de capturar imágenes por medio de la cámara y listarlas en una galería dinámica.
 */
public class activity_evidencia extends AppCompatActivity {

    // Componentes interactivos y contenedores de la interfaz de usuario
    private CardView cardCamara; // Botón circular que activa el flujo de la cámara
    private ImageView imgVistaPrevia; // Espacio para previsualizar la foto antes de guardarla
    private Button btnGuardar; // Botón para añadir la foto previsualizada a la galería
    private LinearLayout contenedorGaleria; // Contenedor donde se inyectan las imágenes dinámicamente

    // Lista en memoria para almacenar las referencias Bitmap de las fotos capturadas
    private final List<Bitmap> listaFotos = new ArrayList<>();
    private Bitmap fotoActual; // Variable temporal para resguardar la última foto capturada sin guardar

    // Lanzadores asíncronos para evitar el uso del metodo obsoleto startActivityForResult
    private ActivityResultLauncher<Intent> cameraLauncher; // Maneja el resultado devuelto por la aplicación de cámara
    private ActivityResultLauncher<String> requestPermissionLauncher; // Maneja la respuesta del usuario ante la solicitud de permisos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this); // Activa el modo inmersivo de pantalla completa para la actividad
        super.onCreate(savedInstanceState); // Ejecuta el proceso básico de creación heredado
        setContentView(R.layout.activity_evidencia); // Infla y asigna el archivo XML de diseño correspondiente

        // Aplica márgenes de seguridad automáticos para que el contenido no quede oculto bajo las barras del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars()); // Extrae las dimensiones de las barras
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom); // Agrega relleno compensatorio
            return insets; // Retorna las especificaciones de la ventana sin cambios adicionales
        });

        // Configuración y personalización de la Barra de Herramientas superior (Toolbar)
        Toolbar toolbar = findViewById(R.id.toolbarEvidencia); // Encuentra el componente Toolbar por su ID
        setSupportActionBar(toolbar); // Registra el Toolbar para actuar como la ActionBar oficial de esta pantalla
        if (getSupportActionBar() != null) { // Valida que la barra de soporte se haya configurado de forma correcta
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Habilita de forma visual la flecha de regreso a casa (atrás)
            getSupportActionBar().setDisplayShowHomeEnabled(true); // Fuerza a que se muestre el botón en la barra superior
        }
        // Asigna la acción de retroceso nativa del sistema cuando el usuario pulsa la flecha de navegación
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // Vinculación de los objetos Java con sus contrapartes definidas en el diseño XML
        cardCamara = findViewById(R.id.cardCamara); // Vincula el disparador de la cámara
        imgVistaPrevia = findViewById(R.id.imgVistaPrevia); // Vincula el cuadro de vista previa de imagen
        btnGuardar = findViewById(R.id.btnGuardar); // Vincula el botón encargado de almacenar la foto
        contenedorGaleria = findViewById(R.id.contenedorGaleria); // Vincula el contenedor dinámico para las imágenes horizontales

        configurarLanzadores(); // Inicializa los registradores obligatorios de resultados para la cámara y permisos

        // Establece el evento click en el botón circular para iniciar la verificación e inicio de la cámara
        cardCamara.setOnClickListener(v -> verificarPermisosYAbrirCamara());

        // Establece el evento click en el botón Guardar para procesar la inserción de la foto actual
        btnGuardar.setOnClickListener(v -> {
            if (fotoActual != null) { // Asegura que exista una foto válida cargada en la vista previa
                guardarFotoEnMatriz(fotoActual); // Procede a guardar e inyectar la foto en la galería
            }
        });
    }

    /**
     * Prepara y registra los callbacks que procesarán los retornos de la cámara y las respuestas de permisos.
     */
    private void configurarLanzadores() {
        // Registra el contrato para recibir resultados desde una actividad externa (Aplicación nativa de Cámara)
        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    // Verifica si la captura fue exitosa y si la respuesta contiene intenciones con datos adjuntos
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Bundle extras = result.getData().getExtras(); // Obtiene el paquete de datos extras del intent
                        if (extras != null) { // Valida la existencia del paquete extras
                            Bitmap imageBitmap = (Bitmap) extras.get("data"); // Extrae la miniatura (Thumbnail) de la foto tomada
                            if (imageBitmap != null) { // Confirma la integridad del mapa de bits extraído
                                fotoActual = imageBitmap; // Almacena la referencia en la variable global temporal
                                imgVistaPrevia.setImageBitmap(imageBitmap); // Carga el mapa de bits dentro del visor de pantalla
                                imgVistaPrevia.setVisibility(View.VISIBLE); // Cambia el estado del visor a visible en la pantalla
                                btnGuardar.setVisibility(View.VISIBLE); // Despliega el botón de guardado en la interfaz
                            }
                        }
                    }
                }
        );

        // Registra el contrato especializado en la solicitud de un único permiso en tiempo de ejecución
        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) { // Si el usuario concedió el permiso de uso de hardware de cámara
                        abrirCamara(); // Procede directamente a disparar el flujo del intent de captura
                    } else { // Si el usuario denegó expresamente el acceso al hardware solicitado
                        Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show(); // Informa la denegación mediante un Toast
                    }
                }
        );
    }

    /**
     * Almacena el Bitmap actual dentro de la colección interna y genera un control visual ImageView dinámicamente.
     */
    private void guardarFotoEnMatriz(Bitmap foto) {
        listaFotos.add(foto); // Agrega la referencia de la imagen a la lista secuencial dinámica en memoria

        ImageView nuevaImagen = new ImageView(this); // Instancia un nuevo componente de imagen en tiempo de ejecución
        
        // Convierte medidas estables dp a píxeles puros basándose en los ratios de densidad del hardware actual
        int tamanoPx = (int) (90 * getResources().getDisplayMetrics().density); // Calcula 90dp para ancho y alto de la miniatura
        int margenPx = (int) (4 * getResources().getDisplayMetrics().density); // Calcula 4dp para separación perimetral

        // Configura las propiedades de diseño de tipo LinearLayout para estructurar las dimensiones físicas de la vista
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(tamanoPx, tamanoPx);
        params.setMargins(margenPx, margenPx, margenPx, margenPx); // Asigna los márgenes computados uniformemente
        nuevaImagen.setLayoutParams(params); // Incorpora las especificaciones físicas al nuevo ImageView
        
        nuevaImagen.setScaleType(ImageView.ScaleType.CENTER_CROP); // Ajusta la imagen recortándola al centro de forma proporcional
        nuevaImagen.setBackgroundColor(0xFFE0E0E0); // Aplica un fondo gris neutro sólido por defecto
        nuevaImagen.setImageBitmap(foto); // Modifica el origen de datos cargando el Bitmap guardado en el control

        contenedorGaleria.addView(nuevaImagen); // Inserta el ImageView configurado dentro del contenedor de scroll horizontal
        
        imgVistaPrevia.setVisibility(View.GONE); // Oculta el componente de previsualización intermedia
        btnGuardar.setVisibility(View.GONE); // Oculta el botón guardar ya que la foto actual fue procesada con éxito
        fotoActual = null; // Libera la referencia temporal preparándola para una nueva captura fotográfica
        
        // Muestra un mensaje flotante de éxito detallando la posición física de la imagen almacenada
        Toast.makeText(this, "Foto guardada #" + listaFotos.size(), Toast.LENGTH_SHORT).show();
    }

    /**
     * Evalúa si la aplicación ya cuenta con los permisos necesarios del sistema; en caso negativo, los solicita.
     */
    private void verificarPermisosYAbrirCamara() {
        // Compara el estado del permiso CAMERA contra los registros de privilegios otorgados por el OS
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            abrirCamara(); // El permiso está activo, se salta la petición e inicia la cámara directamente
        } else {
            // El permiso no existe, ejecuta el lanzador asíncrono para desplegar el cuadro de diálogo oficial
            requestPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    /**
     * Construye y ejecuta un Intent implícito para invocar al sistema de captura fotográfica del dispositivo.
     */
    private void abrirCamara() {
        // Instancia un intent configurando la acción estándar global de captura multimedia
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Valida que el dispositivo cuente con al menos una aplicación de sistema capaz de responder al intent multimedia
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            cameraLauncher.launch(takePictureIntent); // Dispara la actividad externa esperando su resultado por medio del lanzador
        }
    }
}
