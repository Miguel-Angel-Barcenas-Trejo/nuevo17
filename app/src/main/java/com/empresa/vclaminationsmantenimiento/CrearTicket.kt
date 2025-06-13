package com.empresa.vclaminationsmantenimiento

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

class CrearTicket : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_ticket) // Cambia esto por el nombre real de tu XML

        val spinnerMantenimiento: Spinner = findViewById(R.id.spinnerMantenimiento)
        val spinnerPrioridad: Spinner = findViewById(R.id.spinnerPrioridad)

        // Lista de opciones
        val tiposMantenimiento = listOf("Preventivo", "Correctivo")
        val nivelesPrioridad = listOf("Urgente", "Alta", "Media", "Baja")

        // Adaptador para el spinner de mantenimiento
        val adapterMantenimiento = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            tiposMantenimiento
        )
        adapterMantenimiento.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMantenimiento.adapter = adapterMantenimiento

        // Adaptador para el spinner de prioridad
        val adapterPrioridad = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            nivelesPrioridad
        )
        adapterPrioridad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPrioridad.adapter = adapterPrioridad
    }
}
