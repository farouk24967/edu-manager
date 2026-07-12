package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// --- Premium Card Container ---
@Composable
fun PremiumCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    borderGradient: List<Color> = listOf(Color(0xFF2E3035), Color(0xFF1B1D20)),
    backgroundGradient: List<Color> = listOf(Color(0xFF16181B), Color(0xFF0F1012)),
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = RoundedCornerShape(16.dp)
    val clickModifier = if (onClick != null) {
        Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = ripple(),
            onClick = onClick
        )
    } else Modifier

    Box(
        modifier = modifier
            .testTag("premium_card")
            .clip(shape)
            .background(Brush.linearGradient(borderGradient))
            .padding(1.dp) // Border thickness
            .clip(shape)
            .background(Brush.verticalGradient(backgroundGradient))
            .then(clickModifier)
            .padding(20.dp)
    ) {
        Column {
            content()
        }
    }
}

// --- Status Badge ---
@Composable
fun StatusBadge(
    text: String,
    status: String, // SUCCESS, WARNING, ERROR, NEUTRAL
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor) = when (status.uppercase()) {
        "SUCCESS" -> Color(0xFF07241A) to Color(0xFF4ADE80)
        "WARNING" -> Color(0xFF2C1E0A) to Color(0xFFFBBF24)
        "ERROR" -> Color(0xFF2E1014) to Color(0xFFF87171)
        else -> Color(0xFF1F2226) to Color(0xFF9CA3AF)
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

// --- Custom Modern Metric Card ---
@Composable
fun MetricCard(
    title: String,
    value: String,
    changeText: String? = null,
    isPositive: Boolean = true,
    icon: ImageVector? = null,
    iconColor: Color = Color(0xFF6366F1),
    modifier: Modifier = Modifier
) {
    PremiumCard(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF9CA3AF)
            )
            if (icon != null) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(iconColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        modifier = Modifier.size(18.dp),
                        tint = iconColor
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.SemiBold,
                letterSpacing = (-0.5).sp
            ),
            color = Color.White
        )
        if (changeText != null) {
            Spacer(modifier = Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = if (isPositive) "↑" else "↓",
                    color = if (isPositive) Color(0xFF34D399) else Color(0xFFF87171),
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(end = 4.dp)
                )
                Text(
                    text = changeText,
                    color = if (isPositive) Color(0xFF34D399) else Color(0xFFF87171),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = " vs mois dernier",
                    color = Color(0xFF6B7280),
                    fontSize = 12.sp
                )
            }
        }
    }
}

// --- CHART 1: Stripe-style Revenue Line Chart ---
@Composable
fun RevenueLineChart(
    dataPoints: List<Float>,
    labels: List<String>,
    modifier: Modifier = Modifier,
    lineColor: Color = Color(0xFF6366F1)
) {
    if (dataPoints.isEmpty()) return

    Box(modifier = modifier.height(180.dp)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val spacing = width / (dataPoints.size - 1)
            val maxVal = dataPoints.maxOrNull() ?: 1f
            val minVal = dataPoints.minOrNull() ?: 0f
            val range = if (maxVal - minVal == 0f) 1f else (maxVal - minVal)

            val points = dataPoints.mapIndexed { index, valAt ->
                val x = index * spacing
                val y = height - ((valAt - minVal) / range) * (height * 0.8f) - (height * 0.1f)
                Offset(x, y)
            }

            // Draw Background Grid
            val gridLines = 4
            for (i in 0..gridLines) {
                val y = height * 0.1f + (height * 0.8f / gridLines) * i
                drawLine(
                    color = Color(0xFF2A2D34),
                    start = Offset(0f, y),
                    end = Offset(width, y),
                    strokeWidth = 1f
                )
            }

            // Draw Area Gradient
            val path = Path().apply {
                moveTo(0f, height)
                points.forEachIndexed { idx, point ->
                    if (idx == 0) {
                        lineTo(point.x, point.y)
                    } else {
                        // Smooth curves
                        val prev = points[idx - 1]
                        cubicTo(
                            (prev.x + point.x) / 2, prev.y,
                            (prev.x + point.x) / 2, point.y,
                            point.x, point.y
                        )
                    }
                }
                lineTo(width, height)
                close()
            }
            drawPath(
                path = path,
                brush = Brush.verticalGradient(
                    colors = listOf(lineColor.copy(alpha = 0.3f), lineColor.copy(alpha = 0.0f)),
                    startY = 0f,
                    endY = height
                )
            )

            // Draw Line
            val linePath = Path().apply {
                points.forEachIndexed { idx, point ->
                    if (idx == 0) moveTo(point.x, point.y)
                    else {
                        val prev = points[idx - 1]
                        cubicTo(
                            (prev.x + point.x) / 2, prev.y,
                            (prev.x + point.x) / 2, point.y,
                            point.x, point.y
                        )
                    }
                }
            }
            drawPath(
                path = linePath,
                color = lineColor,
                style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
            )

            // Draw Points
            points.forEach { point ->
                drawCircle(
                    color = lineColor,
                    radius = 5.dp.toPx(),
                    center = point
                )
                drawCircle(
                    color = Color(0xFF0F1012),
                    radius = 2.5.dp.toPx(),
                    center = point
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        labels.forEach { label ->
            Text(
                text = label,
                color = Color(0xFF6B7280),
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// --- CHART 2: Linear-style Registrations Bar Chart ---
@Composable
fun RegistrationsBarChart(
    dataPoints: List<Int>,
    labels: List<String>,
    modifier: Modifier = Modifier,
    barColor: Color = Color(0xFF3B82F6)
) {
    if (dataPoints.isEmpty()) return

    Box(modifier = modifier.height(180.dp)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val barCount = dataPoints.size
            val spacing = 20.dp.toPx()
            val totalSpacing = spacing * (barCount - 1)
            val barWidth = (width - totalSpacing) / barCount
            val maxVal = dataPoints.maxOrNull()?.toFloat() ?: 1f

            dataPoints.forEachIndexed { idx, value ->
                val barHeight = (value.toFloat() / maxVal) * (height * 0.85f)
                val x = idx * (barWidth + spacing)
                val y = height - barHeight

                // Draw background light bar track
                drawRoundRect(
                    color = Color(0xFF1E2125),
                    topLeft = Offset(x, 0f),
                    size = Size(barWidth, height),
                    cornerRadius = CornerRadius(4.dp.toPx())
                )

                // Draw filled bar
                drawRoundRect(
                    brush = Brush.verticalGradient(
                        listOf(barColor.copy(alpha = 0.9f), barColor.copy(alpha = 0.4f))
                    ),
                    topLeft = Offset(x, y),
                    size = Size(barWidth, barHeight),
                    cornerRadius = CornerRadius(4.dp.toPx())
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        labels.forEach { label ->
            Text(
                text = label,
                color = Color(0xFF6B7280),
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// --- CHART 3: Apple-style Attendance Radial Ring ---
@Composable
fun AttendanceRadialRing(
    percentage: Float, // 0.0f to 1.0f
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    ringColor: Color = Color(0xFF10B981)
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier.size(100.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val strokeWidth = 12.dp.toPx()
                val diameter = size.minDimension - strokeWidth
                val topLeft = Offset(
                    (size.width - diameter) / 2,
                    (size.height - diameter) / 2
                )

                // Under ring
                drawArc(
                    color = Color(0xFF22262B),
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    topLeft = topLeft,
                    size = Size(diameter, diameter),
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )

                // Filled arc
                drawArc(
                    color = ringColor,
                    startAngle = -90f,
                    sweepAngle = percentage * 360f,
                    useCenter = false,
                    topLeft = topLeft,
                    size = Size(diameter, diameter),
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
            }
            Text(
                text = "${(percentage * 100).toInt()}%",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Color.White
            )
        }
        Spacer(modifier = Modifier.width(20.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = Color.White
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF9CA3AF)
            )
        }
    }
}

// --- Payments Progress Tracker ---
@Composable
fun PaymentsProgressCard(
    received: Double,
    pending: Double,
    modifier: Modifier = Modifier
) {
    val total = received + pending
    val fraction = if (total == 0.0) 0f else (received / total).toFloat()

    PremiumCard(modifier = modifier) {
        Text(
            text = "État des paiements (DZD)",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            color = Color.White
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = "Reçu", fontSize = 12.sp, color = Color(0xFF9CA3AF))
                Text(
                    text = "${received.toInt()} DZD",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF34D399)
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(text = "En attente", fontSize = 12.sp, color = Color(0xFF9CA3AF))
                Text(
                    text = "${pending.toInt()} DZD",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFBBF24)
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        // Progress Track
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(CircleShape)
                .background(Color(0xFF22262B))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(fraction)
                    .clip(CircleShape)
                    .background(Color(0xFF10B981))
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "${(fraction * 100).toInt()}% des règlements ont été effectués.",
            fontSize = 12.sp,
            color = Color(0xFF9CA3AF)
        )
    }
}

// --- Empty State UI ---
@Composable
fun EmptyStateView(
    title: String,
    description: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(Color(0xFF1F2226)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier.size(32.dp),
                tint = Color(0xFF6366F1)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            color = Color.White
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF9CA3AF),
            modifier = Modifier.fillMaxWidth(0.85f),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}
