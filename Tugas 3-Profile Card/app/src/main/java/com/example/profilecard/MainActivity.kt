package com.example.profilecard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFF3F4F6)
                ) {
                    ProfileCardModern()
                }
            }
        }
    }
}

private val ItsBluePrimary = Color(0xFF0F4C81)
private val ItsBlueLight = Color(0xFFEFF6FF)

@Composable
fun ProfileCardModern() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(32.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {


                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(ItsBluePrimary)
                        .padding(horizontal = 24.dp, vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.logo_its),
                            contentDescription = "Logo ITS",
                            modifier = Modifier.size(48.dp),
                            contentScale = ContentScale.Fit
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("INSTITUT TEKNOLOGI", color = Color.White, fontSize = 12.sp, letterSpacing = 1.sp)
                            Text("SEPULUH NOPEMBER", color = Color.White, fontSize = 12.sp, letterSpacing = 1.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    Image(
                        painter = painterResource(id = R.drawable.yuwand),
                        contentDescription = "Foto Profil",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(130.dp)
                            .clip(CircleShape)
                            .border(4.dp, Color(0xFF60A5FA), CircleShape)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Yuwand Arteta Hydri Wahyu P",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "NRP : 5053251020",
                        color = Color(0xFF93C5FD),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    InfoRowItem(icon = Icons.Default.MenuBook, title = "Program Studi", value = "Rekayasa Perangkat Lunak")
                    InfoRowItem(icon = Icons.Default.Mail, title = "Email Mahasiswa", value = "ywndtik@gmail.com")
                    InfoRowItem(icon = Icons.Default.School, title = "Angkatan", value = "2025")

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 20.dp),
                        color = Color(0xFFE5E7EB),
                        thickness = 1.5.dp
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = ItsBluePrimary, modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Mahasiswa", fontWeight = FontWeight.Bold, color = ItsBluePrimary, fontSize = 15.sp)
                        }

                        VerticalDivider(modifier = Modifier.height(36.dp), color = Color(0xFFE5E7EB))

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(imageVector = Icons.Default.CalendarMonth, contentDescription = null, tint = ItsBluePrimary, modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Semester 3", fontWeight = FontWeight.Bold, color = ItsBluePrimary, fontSize = 15.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InfoRowItem(icon: ImageVector, title: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .background(ItsBlueLight, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = ItsBluePrimary, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = title, color = Color.Gray, fontSize = 12.sp)
            Text(text = value, fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = Color.DarkGray)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileCardPreview() {
    MaterialTheme {
        ProfileCardModern()
    }
}