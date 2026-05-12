package com.example.flagplaybook.model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

enum class RouteType(val displayName: String) {
    GO("Go"),
    OUT("Out"),
    IN("In"),
    SLANT("Slant"),
    POST("Post"),
    CORNER("Corner"),
    CURL("Curl"),
    HITCH("Hitch"),
    COMEBACK("Comeback"),
    FLAT("Flat"),
    WHEEL("Wheel"),
    CROSS("Cross")
}

data class PlayerRoute(
    val id: Int,
    val color: Color,
    val startX: Float, // Porcentaje de ancho (0.0 a 1.0)
    var routeType: RouteType? = null
)

fun getRoutePoints(type: RouteType, startPos: Offset, canvasWidth: Float, yardSize: Float): List<Offset> {
    val points = mutableListOf(startPos)
    val isRightSide = startPos.x > canvasWidth / 2

    when (type) {
        RouteType.GO -> {
            points.add(startPos.copy(y = startPos.y - yardSize * 15))
        }
        RouteType.OUT -> {
            val vertical = startPos.y - yardSize * 5
            points.add(Offset(startPos.x, vertical))
            val direction = if (isRightSide) 1f else -1f
            points.add(Offset(startPos.x + yardSize * 5 * direction, vertical))
        }
        RouteType.IN -> {
            val vertical = startPos.y - yardSize * 5
            points.add(Offset(startPos.x, vertical))
            val direction = if (isRightSide) -1f else 1f
            points.add(Offset(startPos.x + yardSize * 5 * direction, vertical))
        }
        RouteType.SLANT -> {
            val direction = if (isRightSide) -1f else 1f
            points.add(Offset(startPos.x + yardSize * 6 * direction, startPos.y - yardSize * 6))
        }
        RouteType.POST -> {
            val vertical = startPos.y - yardSize * 8
            points.add(Offset(startPos.x, vertical))
            val direction = if (isRightSide) -1f else 1f
            points.add(Offset(startPos.x + yardSize * 7 * direction, vertical - yardSize * 7))
        }
        RouteType.CORNER -> {
            val vertical = startPos.y - yardSize * 8
            points.add(Offset(startPos.x, vertical))
            val direction = if (isRightSide) 1f else -1f
            points.add(Offset(startPos.x + yardSize * 7 * direction, vertical - yardSize * 7))
        }
        RouteType.CURL -> {
            val vertical = startPos.y - yardSize * 8
            points.add(Offset(startPos.x, vertical))
            val direction = if (isRightSide) -1f else 1f
            points.add(Offset(startPos.x + yardSize * 2 * direction, vertical + yardSize * 2))
        }
        RouteType.HITCH -> {
            val vertical = startPos.y - yardSize * 5
            points.add(Offset(startPos.x, vertical))
            points.add(Offset(startPos.x, vertical + yardSize * 1.5f))
        }
        RouteType.COMEBACK -> {
            val vertical = startPos.y - yardSize * 10
            points.add(Offset(startPos.x, vertical))
            val direction = if (isRightSide) 1f else -1f
            points.add(Offset(startPos.x + yardSize * 3 * direction, vertical + yardSize * 3))
        }
        RouteType.FLAT -> {
            val direction = if (isRightSide) 1f else -1f
            points.add(Offset(startPos.x + yardSize * 6 * direction, startPos.y - yardSize * 2))
        }
        RouteType.WHEEL -> {
            val direction = if (isRightSide) 1f else -1f
            points.add(Offset(startPos.x + yardSize * 4 * direction, startPos.y - yardSize * 1))
            points.add(Offset(startPos.x + yardSize * 5 * direction, startPos.y - yardSize * 12))
        }
        RouteType.CROSS -> {
            val direction = if (isRightSide) -1f else 1f
            points.add(Offset(startPos.x + yardSize * 15 * direction, startPos.y - yardSize * 8))
        }
    }
    return points
}
