package com.example.flagplaybook.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.flagplaybook.model.PlayerRoute
import com.example.flagplaybook.model.getRoutePoints
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun PlaybookCanvas(
    players: List<PlayerRoute>,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val yardSize = canvasHeight / 20 // Vista de 20 yardas de profundidad

        // 1. Dibujar el Campo (Fondo verde oscuro táctico)
        drawRect(color = Color(0xFF2E7D32))

        // 2. Líneas de yardas y Scrimmage
        val losY = canvasHeight * 0.9f // Línea de Scrimmage cerca del fondo
        
        // Línea de Scrimmage (Blanca gruesa)
        drawLine(
            color = Color.White,
            start = Offset(0f, losY),
            end = Offset(canvasWidth, losY),
            strokeWidth = 4.dp.toPx()
        )

        // Marcas de 5, 10, 15 yardas (Líneas tenues)
        for (i in 1..3) {
            val y = losY - (yardSize * 5 * i)
            drawLine(
                color = Color.White.copy(alpha = 0.3f),
                start = Offset(0f, y),
                end = Offset(canvasWidth, y),
                strokeWidth = 1.dp.toPx()
            )
        }

        // 3. Dibujar Rutas
        players.forEach { player ->
            player.routeType?.let { type ->
                val startPos = Offset(canvasWidth * player.startX, losY)
                val points = getRoutePoints(type, startPos, canvasWidth, yardSize)
                
                if (points.size > 1) {
                    val path = Path().apply {
                        moveTo(points[0].x, points[0].y)
                        for (i in 1 until points.size) {
                            lineTo(points[i].x, points[i].y)
                        }
                    }

                    // Dibujar línea de la ruta
                    drawPath(
                        path = path,
                        color = player.color,
                        style = Stroke(width = 3.dp.toPx())
                    )

                    // Dibujar Punta de Flecha al final
                    drawArrowHead(
                        points[points.size - 2],
                        points.last(),
                        player.color
                    )
                }
                
                // Dibujar posición del jugador (Círculo)
                drawCircle(
                    color = player.color,
                    radius = 8.dp.toPx(),
                    center = startPos
                )
            }
        }
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawArrowHead(
    from: Offset,
    to: Offset,
    color: Color
) {
    val angle = atan2(to.y - from.y, to.x - from.x)
    val arrowSize = 15.dp.toPx()
    val arrowAngle = Math.PI / 6

    val path = Path().apply {
        moveTo(to.x, to.y)
        lineTo(
            to.x - arrowSize * cos(angle - arrowAngle).toFloat(),
            to.y - arrowSize * sin(angle - arrowAngle).toFloat()
        )
        moveTo(to.x, to.y)
        lineTo(
            to.x - arrowSize * cos(angle + arrowAngle).toFloat(),
            to.y - arrowSize * sin(angle + arrowAngle).toFloat()
        )
    }
    drawPath(path = path, color = color, style = Stroke(width = 3.dp.toPx()))
}
