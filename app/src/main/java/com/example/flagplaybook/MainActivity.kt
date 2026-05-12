package com.example.flagplaybook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flagplaybook.model.PlayerRoute
import com.example.flagplaybook.model.RouteType
import com.example.flagplaybook.ui.PlaybookCanvas

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    PlaybookScreen()
                }
            }
        }
    }
}

@Composable
fun PlaybookScreen() {
    var players by remember {
        mutableStateOf(
            listOf(
                PlayerRoute(1, Color.Red, 0.15f),   // WR1
                PlayerRoute(2, Color.Blue, 0.35f),  // WR2
                PlayerRoute(3, Color.Yellow, 0.65f),// WR3
                PlayerRoute(4, Color.Green, 0.85f)  // WR4
            )
        )
    }

    var selectedPlayerId by remember { mutableStateOf(1) }

    Column(modifier = Modifier.fillMaxSize()) {
        // 1. Selector de Jugador (Top Bar)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            players.forEach { player ->
                val isSelected = selectedPlayerId == player.id
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(if (isSelected) player.color else player.color.copy(alpha = 0.3f))
                        .clickable { selectedPlayerId = player.id }
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "WR${player.id}",
                        color = if (isSelected) Color.White else Color.Black,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            Button(
                onClick = {
                    players = players.map { it.copy(routeType = null) }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
            ) {
                Text("Borrar")
            }
        }

        // 2. Campo (Canvas)
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            PlaybookCanvas(players = players)
        }

        // 3. Selector de Rutas (Bottom Panel)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.DarkGray)
                .padding(16.dp)
        ) {
            Text(
                "Selecciona Ruta para WR$selectedPlayerId",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.height(200.dp)
            ) {
                items(RouteType.values()) { routeType ->
                    Button(
                        onClick = {
                            players = players.map { p ->
                                if (p.id == selectedPlayerId) p.copy(routeType = routeType) else p
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (players.find { it.id == selectedPlayerId }?.routeType == routeType) 
                                Color.White.copy(alpha = 0.2f) else Color.Black.copy(alpha = 0.3f)
                        )
                    ) {
                        Text(routeType.displayName, fontSize = 10.sp, color = Color.White)
                    }
                }
            }
        }
    }
}
