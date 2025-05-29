package com.empresa.vclaminationsmantenimiento

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore


class reporte1prev : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reporte1prev) // Vincula el layout `activity_reporte` con esta actividad

        // rirebase base de datos
        val db = FirebaseFirestore.getInstance()


        // Inicializar los elementos del diseño
        val etFecha = findViewById<EditText>(R.id.etFecha)
        val etReporte = findViewById<EditText>(R.id.etReporte)
        val etTurno = findViewById<EditText>(R.id.etTurno)
        val buttonText = intent.getStringExtra("buttonText") ?: "Sin título" // Obtener el texto del botón presionado
        val etMaquina = findViewById<EditText>(R.id.etMaquina) // Referencia al EditText

        etMaquina.setText(buttonText) // Establecer el nombre del botón como texto del EditText

        val etResponsable = findViewById<EditText>(R.id.etResponsable)
        val etTipoReporte = findViewById<EditText>(R.id.etTipoReporte)
        val etParoMaquina = findViewById<EditText>(R.id.etParoMaquina)
        val etTipoFalla = findViewById<EditText>(R.id.etTipoFalla)
        val etCausaRaiz = findViewById<EditText>(R.id.etCausaRaiz)
        val etActividad = findViewById<EditText>(R.id.etActividad)
        val etHoraReporte = findViewById<EditText>(R.id.etHoraReporte)
        val etHoraInicio = findViewById<EditText>(R.id.etHoraInicio)
        val etHoraFinal = findViewById<EditText>(R.id.etHoraFinal)
        val etTiempoTotal = findViewById<EditText>(R.id.etTiempoTotal)
        val etRequerimientos = findViewById<EditText>(R.id.etRequerimientos)
        val etObservaciones = findViewById<EditText>(R.id.etObservaciones)

        val btnEnviarCorreo = findViewById<Button>(R.id.btnDescargar)

        // Configurar la lógica para los botones
        btnEnviarCorreo.setOnClickListener {
            val datos = mapOf(
                "fecha" to etFecha.text.toString(),
                "reporte" to etReporte.text.toString(),
                "turno" to etTurno.text.toString(),
                "maquina" to etMaquina.text.toString(),
                "responsable" to etResponsable.text.toString(),
                "tipoReporte" to etTipoReporte.text.toString(),
                "paroMaquina" to etParoMaquina.text.toString(),
                "tipoFalla" to etTipoFalla.text.toString(),
                "causaRaiz" to etCausaRaiz.text.toString(),
                "actividad" to etActividad.text.toString(),
                "horaReporte" to etHoraReporte.text.toString(),
                "horaInicio" to etHoraInicio.text.toString(),
                "horaFinal" to etHoraFinal.text.toString(),
                "tiempoTotal" to etTiempoTotal.text.toString(),
                "requerimientos" to etRequerimientos.text.toString(),
                "observaciones" to etObservaciones.text.toString()
            )

            // 🔥 GUARDAR EN FIRESTORE
            val nombreColeccion = etMaquina.text.toString().trim()

            if (nombreColeccion.isNotEmpty()) {
                db.collection(nombreColeccion)
                    .add(datos)
                    .addOnSuccessListener {
                        Toast.makeText(
                            this,
                            "Datos guardados en la colección: $nombreColeccion",
                            Toast.LENGTH_SHORT
                        ).show()
                        // 👉 También enviar a Google Sheets
                        enviarDatosAGoogleSheets(datos)
                    }
            }

            // 🔧 GENERAR PDF Y ENVIAR CORREO
            val pdfFile = PdfHelper.generatePdf(this, datos)

            if (pdfFile != null && pdfFile.exists()) {
                EmailHelper.sendEmail(this, "barcenasm342@gmail.com", pdfFile)
                Toast.makeText(this, "Correo enviado con el PDF adjunto", Toast.LENGTH_SHORT).show()
            } else {
                Log.e("PDF", "El archivo no se generó correctamente")
                Toast.makeText(this, "Error al generar el archivo PDF", Toast.LENGTH_SHORT).show()
            }
        }

    }
    private fun enviarDatosAGoogleSheets(datos: Map<String, String>) {
        val urlWebhook = "https://script.google.com/macros/s/AKfycbxalYAZ9hoaoVowvK2SPyaxR1bxdVv7EQ7xPbqo17XP5FVzoPUvWZre_pnZXRidQyL7ag/exec"

        val requestQueue = com.android.volley.toolbox.Volley.newRequestQueue(this)

        val request = object : com.android.volley.toolbox.StringRequest(
            com.android.volley.Request.Method.POST, urlWebhook,
            { response ->
                Toast.makeText(this, "Datos enviados a Google Sheets", Toast.LENGTH_SHORT).show()
                Toast.makeText(this, "Respuesta del servidor: $response", Toast.LENGTH_LONG).show()
            },
            { error ->
                Toast.makeText(this, "Error al enviar a Sheets", Toast.LENGTH_SHORT).show()
                error.printStackTrace()
            }
        ) {
            override fun getParams(): Map<String, String> {
                return datos
            }

            override fun getBodyContentType(): String {
                return "application/x-www-form-urlencoded; charset=UTF-8"
            }
        }
        requestQueue.add(request)
    }

}