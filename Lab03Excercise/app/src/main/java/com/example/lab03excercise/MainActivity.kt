package com.example.lab03excercise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import java.net.URLEncoder
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "pantalla1"
    ) {
        composable("pantalla1") {
            Pantalla1(
                onNavigate = { texto ->
                    // ✅ Importante: codificamos el texto para evitar errores con espacios o caracteres especiales
                    val encoded = URLEncoder.encode(texto, StandardCharsets.UTF_8.toString())
                    navController.navigate("pantalla2/$encoded")
                }
            )
        }

        composable(
            route = "pantalla2/{mensaje}",
            arguments = listOf(navArgument("mensaje") { type = NavType.StringType })
        ) { backStackEntry ->
            val encodedMsg = backStackEntry.arguments?.getString("mensaje") ?: ""
            val mensaje = URLDecoder.decode(encodedMsg, StandardCharsets.UTF_8.toString())
            Pantalla2(mensaje)
        }
    }
}

@Composable
fun Pantalla1(onNavigate: (String) -> Unit) {
    var inputText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
    ) {
        OutlinedTextField(
            value = inputText,
            onValueChange = { inputText = it },
            label = { Text("Escribe un mensaje") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                if (inputText.isNotBlank()) {
                    onNavigate(inputText)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ir a Pantalla 2")
        }
    }
}

@Composable
fun Pantalla2(mensaje: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Mensaje recibido:",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = mensaje,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
