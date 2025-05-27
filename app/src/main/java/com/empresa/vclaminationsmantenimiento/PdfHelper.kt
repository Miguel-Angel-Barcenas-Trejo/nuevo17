package com.empresa.vclaminationsmantenimiento

import android.content.Context
import com.itextpdf.text.pdf.*
import java.io.*

object PdfHelper {
    fun generatePdf(context: Context, datos: Map<String, String>): File {
        val inputStream = context.assets.open("base_template.pdf")
        val tempPdfFile = File(context.externalCacheDir, "Reporte.pdf")

        val reader = PdfReader(inputStream)
        val stamper = PdfStamper(reader, FileOutputStream(tempPdfFile))
        val form = stamper.acroFields

        for ((clave, valor) in datos) {
            form.setField(clave, valor)
        }

        stamper.setFormFlattening(true)
        stamper.close()
        reader.close()

        return tempPdfFile
    }
}


