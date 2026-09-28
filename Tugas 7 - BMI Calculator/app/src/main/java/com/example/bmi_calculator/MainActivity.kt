package com.example.bmi_calculator

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bmi_calculator.ui.theme.BMI_calculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BMI_calculatorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BmiApp()
                }
                }
            }
        }
    }

@Composable
fun BmiApp() {
    var weightInput by remember { mutableStateOf("") }
    var heightInput by remember { mutableStateOf("") }
    var bmiResult by remember { mutableDoubleStateOf(0.0) }
    var bmiCategory by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "BMI Calculator",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = weightInput,
            onValueChange = { weightInput = it },
            label = { Text("Berat Badan (kg)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = heightInput,
            onValueChange = { heightInput = it },
            label = { Text("Tinggi Badan (cm)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Tombol 1: Hitung BMI
        Button(
            onClick = {
                val weight = weightInput.toDoubleOrNull() ?: 0.0
                val heightCm = heightInput.toDoubleOrNull() ?: 0.0

                if (weight > 0 && heightCm > 0) {
                    val heightM = heightCm / 100
                    bmiResult = weight / (heightM * heightM)

                    bmiCategory = when {
                        bmiResult < 18.5 -> "Kekurangan Berat Badan"
                        bmiResult < 24.9 -> "Normal"
                        bmiResult < 29.9 -> "Kelebihan Berat Badan"
                        else -> "Obesitas"
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EA))
        ) {
            Text("Hitung BMI")
        }

        Spacer(modifier = Modifier.height(16.dp))


        OutlinedButton(
            onClick = {
                weightInput = ""
                heightInput = ""
                bmiResult = 0.0
                bmiCategory = ""
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Reset")
        }

        Spacer(modifier = Modifier.height(16.dp))


        Button(
            onClick = {
                Toast.makeText(context, "Kurus < 18.5 | Normal 18.5 - 24.9 | Gemuk > 25", Toast.LENGTH_LONG).show()
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
        ) {
            Text("Lihat Kategori", color = Color.White)
        }

        Spacer(modifier = Modifier.height(32.dp))

        if (bmiResult > 0.0) {
            Text(text = "Hasil BMI", fontSize = 16.sp)
            Text(
                text = String.format("%.1f", bmiResult),
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = bmiCategory,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BMI_calculatorTheme {
        BmiApp()
    }
}