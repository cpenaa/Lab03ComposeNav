package com.example.mynewaplication

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.foundation.text.KeyboardOptions

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                SaldoScreen()
            }
        }
    }
}

@Composable
fun SaldoScreen() {
    var nombre by remember { mutableStateOf("") }
    var monto by remember { mutableStateOf("") }
    var saldo by remember { mutableStateOf(1000.00) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Variables para mostrar confirmación
    var nombreEnviado by remember { mutableStateOf<String?>(null) }
    var montoEnviado by remember { mutableStateOf<Double?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre destinatario") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = monto,
            onValueChange = { monto = it },
            label = { Text("Monto a enviar S/") },
            isError = errorMessage != null,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.fillMaxWidth()
        )

        errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = MaterialTheme.shapes.extraSmall
                )
                .padding(16.dp)
        ) {
            Text(
                text = "Monto actual: S/$saldo",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Button(
            onClick = {
                val montoIngresado = monto.toDoubleOrNull()
                when {
                    nombre.isBlank() -> {
                        errorMessage = "Debe ingresar un nombre"
                    }
                    montoIngresado == null -> {
                        errorMessage = "Ingrese un monto válido"
                    }
                    montoIngresado <= 0 -> {
                        errorMessage = "El monto debe ser mayor a 0"
                    }
                    montoIngresado > saldo -> {
                        errorMessage = "El monto excede el saldo disponible"
                    }
                    else -> {
                        saldo -= montoIngresado
                        Log.d("SaldoScreen", "Nombre: $nombre - Monto: $montoIngresado")

                        nombreEnviado = nombre
                        montoEnviado = montoIngresado

                        nombre = ""
                        monto = ""
                        errorMessage = null
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("REGISTRAR")
        }

        if (nombreEnviado != null && montoEnviado != null) {
            Text(
                text = "Se envió: $nombreEnviado, $montoEnviado",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}