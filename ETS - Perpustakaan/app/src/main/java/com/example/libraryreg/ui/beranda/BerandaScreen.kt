package com.example.libraryreg.ui.beranda

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.libraryreg.ui.theme.DarkBluePrimary

@Composable
fun BerandaScreen(
    onNavigateToForm: () -> Unit,
    onNavigateToList: () -> Unit,
    onNavigateToSearch: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo / Header Icon Box
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(DarkBluePrimary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PersonAdd,
                    contentDescription = "Logo LibraryReg",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(48.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "LibraryReg",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = DarkBluePrimary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Aplikasi Pendaftaran Anggota Perpustakaan",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(36.dp))

            // Menu Cards
            MenuButton(
                title = "Pendaftaran Anggota",
                icon = Icons.Default.PersonAdd,
                onClick = onNavigateToForm
            )

            Spacer(modifier = Modifier.height(16.dp))

            MenuButton(
                title = "Daftar Anggota",
                icon = Icons.Default.List,
                onClick = onNavigateToList
            )

            Spacer(modifier = Modifier.height(16.dp))

            MenuButton(
                title = "Pencarian Data",
                icon = Icons.Default.Search,
                onClick = onNavigateToSearch
            )

            Spacer(modifier = Modifier.height(16.dp))

            MenuButton(
                title = "Kelola Data",
                icon = Icons.Default.Settings,
                onClick = onNavigateToList
            )
        }
    }
}

@Composable
fun MenuButton(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = DarkBluePrimary
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(28.dp)
                )
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}
