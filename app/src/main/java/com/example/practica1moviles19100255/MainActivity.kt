package com.example.practica1moviles19100255

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.practica1moviles19100255.ui.theme.PRACTICA1MOVILES19100255Theme
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PRACTICA1MOVILES19100255Theme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "main_menu",
        modifier = modifier
    ) {
        composable("main_menu") {
            MainMenu(navController = navController)
        }
        composable("water_consumption_calculator") {
            WaterConsumptionCalculator(navController = navController)
        }
        composable("physical_activity_log") {
            PhysicalActivityLog(navController = navController)
        }
        composable("sports_car_catalog") {
            SportsCarCatalog(navController = navController)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainMenu(navController: NavController, modifier: Modifier = Modifier) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Menú Principal") })
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = { navController.navigate("water_consumption_calculator") }) {
                Text("Calculadora de consumo de agua")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.navigate("physical_activity_log") }) {
                Text("Registro de actividad física")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.navigate("sports_car_catalog") }) {
                Text("Catálogo de Autos deportivos")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WaterConsumptionCalculator(navController: NavController, modifier: Modifier = Modifier) {
    var name by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    val genderOptions = listOf("Masculino", "Femenino", "Sin especificar")
    var selectedGender by remember { mutableStateOf(genderOptions[2]) }
    var result by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Consumo de Agua") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Calculadora de Consumo de Agua",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(24.dp))
            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre de la persona") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = weight,
                onValueChange = { weight = it },
                label = { Text("Peso corporal (en kg)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Género:")
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                genderOptions.forEach { gender ->
                    Row(
                        Modifier
                            .selectable(
                                selected = (selectedGender == gender),
                                onClick = { selectedGender = gender }
                            )
                            .padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (selectedGender == gender),
                            onClick = { selectedGender = gender }
                        )
                        Text(text = gender, modifier = Modifier.padding(start = 4.dp))
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = {
                val weightValue = weight.toDoubleOrNull()
                when {
                    name.isBlank() -> {
                        scope.launch { snackbarHostState.showSnackbar("El nombre es obligatorio.") }
                    }
                    weightValue == null -> {
                        scope.launch { snackbarHostState.showSnackbar("El peso debe ser un número.") }
                    }
                    weightValue !in 5.0..200.0 -> {
                        scope.launch { snackbarHostState.showSnackbar("El peso debe estar entre 5 y 200 kg.") }
                    }
                    else -> {
                        val genderFactor = when (selectedGender) {
                            "Masculino" -> 1.02
                            "Femenino" -> 1.01
                            else -> 1.00
                        }
                        val recommendedLitters = weightValue * 0.035 * genderFactor
                        val df = DecimalFormat("#.##")
                        result =
                            "$name debe beber aproximadamente ${df.format(recommendedLitters)} litros de agua al día"
                    }
                }
            }) {
                Text("Calcular")
            }
            if (result.isNotEmpty()) {
                Spacer(modifier = Modifier.height(24.dp))
                Text(result, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhysicalActivityLog(navController: NavController, modifier: Modifier = Modifier) {
    val activityOptions = listOf("Correr", "Caminar", "Nadar", "Ciclismo", "Yoga")
    var selectedActivity by remember { mutableStateOf(activityOptions[0]) }
    var duration by remember { mutableStateOf("") }
    val intensityOptions = listOf("Baja", "Media", "Alta")
    var selectedIntensity by remember { mutableStateOf(intensityOptions[1]) }
    var result by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Actividad Física") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                "Registro de Actividad Física",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(24.dp))

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                TextField(
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    readOnly = true,
                    value = selectedActivity,
                    onValueChange = {},
                    label = { Text("Tipo de actividad") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    colors = ExposedDropdownMenuDefaults.textFieldColors(),
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    activityOptions.forEach { activity ->
                        DropdownMenuItem(
                            text = { Text(activity) },
                            onClick = {
                                selectedActivity = activity
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = duration,
                onValueChange = { duration = it },
                label = { Text("Duración (en minutos)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Intensidad:")
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                intensityOptions.forEach { intensity ->
                    Row(
                        Modifier
                            .selectable(
                                selected = (selectedIntensity == intensity),
                                onClick = { selectedIntensity = intensity }
                            )
                            .padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (selectedIntensity == intensity),
                            onClick = { selectedIntensity = intensity }
                        )
                        Text(text = intensity, modifier = Modifier.padding(start = 4.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = {
                val durationValue = duration.toIntOrNull()
                if (durationValue == null || durationValue <= 0) {
                    scope.launch { snackbarHostState.showSnackbar("La duración debe ser un número entero positivo.") }
                } else {
                    val caloriesPerMinute = when (selectedActivity) {
                        "Correr" -> 10
                        "Caminar" -> 5
                        "Nadar" -> 8
                        "Ciclismo" -> 7
                        else -> 4 // Yoga
                    }
                    val intensityFactor = when (selectedIntensity) {
                        "Baja" -> 0.8
                        "Media" -> 1.0
                        else -> 1.2 // Alta
                    }
                    val caloriesBurned = caloriesPerMinute * durationValue * intensityFactor
                    val df = DecimalFormat("#.##")
                    result = "Calorías quemadas: ${df.format(caloriesBurned)}"
                }
            }) {
                Text("Calcular Calorías")
            }

            if (result.isNotEmpty()) {
                Spacer(modifier = Modifier.height(24.dp))
                Text(result, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

data class Car(
    val brand: String,
    val model: String,
    val price: Double,
    val imageUrl: String
)

val carList = listOf(
    Car("Ferrari", "SF90 Stradale", 524815.0, "https://cdn.motor1.com/images/mgl/y8kAn/s1/ferrari-sf90-stradale.jpg"),
    Car("Lamborghini", "Huracán EVO", 261274.0, "https://cdn.motor1.com/images/mgl/pVAk4/s1/lamborghini-huracan-evo-rwd.jpg"),
    Car("Porsche", "911 GT3", 161100.0, "https://cdn.motor1.com/images/mgl/Lp4B0/s1/porsche-911-gt3-touring-2022.jpg"),
    Car("McLaren", "720S", 299000.0, "https://cdn.motor1.com/images/mgl/0y7xZ/s1/mclaren-720s-coupe-by-mso-apex-collection.jpg"),
    Car("Bugatti", "Chiron", 3300000.0, "https://cdn.motor1.com/images/mgl/J6mGg/s1/bugatti-chiron-super-sport-300.jpg")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SportsCarCatalog(navController: NavController, modifier: Modifier = Modifier) {
    val totalCost = carList.sumOf { it.price }
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale.US)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Catálogo de Autos Deportivos") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
                    }
                }
            )
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(carList) { car ->
                    CarCard(car = car)
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Costo Total: ${currencyFormat.format(totalCost)}",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}

@Composable
fun CarCard(car: Car) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column {
            AsyncImage(
                model = car.imageUrl,
                contentDescription = "${car.brand} ${car.model}",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "${car.brand} ${car.model}", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Precio: ${NumberFormat.getCurrencyInstance(Locale.US).format(car.price)}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PRACTICA1MOVILES19100255Theme {
        AppNavigation()
    }
}
