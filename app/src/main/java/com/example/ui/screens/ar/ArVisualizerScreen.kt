package com.example.ui.screens.ar

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ArAssetEntity
import com.example.ui.theme.*

/**
 * AR / Visualisasi Interaktif Screen
 *
 * PANDUAN ARSITEKTUR (ARCHITECTURAL GUARDRAILS):
 * 1. 100% NATIVE JETPACK COMPOSE:
 *    Semua visualisasi interaktif dirender murni menggunakan Jetpack Compose Canvas,
 *    vektor geometris, dan animasi native. JANGAN PERNAH menggunakan WebView atau HTML di sini.
 *    Playground adalah tempat eksekusi web (HTML/CSS/JS via WebView); AR Visualizer adalah
 *    skematik memori native 2D/3D yang ringan, cepat, dan hemat baterai.
 *
 * 2. HANYA UNTUK KONSEP / POTONGAN KODE PENDEK (TRACK C/C++/PYTHON):
 *    Fitur ini difokuskan memvisualisasikan satu konsep mendasar (misal: 1 baris deklarasi pointer,
 *    1 langkah alokasi heap/ref-count, 1 langkah ekspansi buffer dinamis), bukan menjalankan program penuh.
 *
 * 3. TEMPLATE DATA TERSTRUKTUR:
 *    Visualisasi menggunakan template standar yang sudah didefinisikan (misal: "pointer", "memory_alloc",
 *    "loop_flow"). Data dirender dari data terstruktur (posisi kotak memori, label alamat heksadesimal,
 *    garis panah referensi, dan tahapan langkah), bukan file 3D/video raksasa yang di-download dari internet.
 */
@Composable
fun ArVisualizerScreen(
    arAsset: ArAssetEntity?,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (arAsset == null) {
        Box(modifier = Modifier.fillMaxSize().background(BackgroundDark), contentAlignment = Alignment.Center) {
            Text("Asset Visualisasi Interaktif tidak ditemukan.", color = TextSecondaryDark)
        }
        return
    }

    var step by remember { mutableIntStateOf(1) }
    val infiniteTransition = rememberInfiniteTransition(label = "ar_pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )

    // Normalize visualization type
    val normalizedType = remember(arAsset.visualizationType) {
        when (arAsset.visualizationType.lowercase()) {
            "pointer", "ptr" -> "pointer"
            "memory", "memory_alloc", "alloc" -> "memory_alloc"
            "flow", "loop_flow", "loop" -> "loop_flow"
            else -> "pointer"
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = BackgroundDark,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.clip(CircleShape).background(SurfaceDark)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = TextPrimaryDark)
                }

                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("🔬", fontSize = 20.sp)
                    Text(
                        text = arAsset.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryDark,
                        maxLines = 1
                    )
                }

                Box(modifier = Modifier.size(40.dp))
            }
        },
        bottomBar = {
            Surface(
                color = SurfaceDark,
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.verticalGradient(listOf(BorderDark, SurfaceDark))),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 14.dp)
                        .navigationBarsPadding(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = { if (step > 1) step-- },
                        enabled = step > 1,
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Langkah Sebelumnya")
                    }

                    Button(
                        onClick = { if (step < 3) step++ else step = 1 },
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
                    ) {
                        Text(if (step < 3) "Langkah Berikutnya ➡️" else "Ulangi Simulasi 🔄", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Visualisasi Konsep Native",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = SecondaryTeal
                    )
                    Text(
                        text = "100% Native Compose Canvas • Mode Skematik Ringan",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondaryDark
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = PrimaryPurple.copy(alpha = 0.2f),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(PrimaryPurple, SecondaryTeal)))
                ) {
                    Text(
                        text = arAsset.codeLanguage.uppercase(),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = SecondaryTeal
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = arAsset.description,
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondaryDark
            )

            Spacer(modifier = Modifier.height(16.dp))

            // AR Native Canvas Viewport (100% Jetpack Compose Canvas)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(270.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CodeBg),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(PrimaryPurple, SecondaryTeal)))
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawGridWireframe(size)

                        when (normalizedType) {
                            "pointer" -> drawPointerVisualization(step, pulseAlpha)
                            "memory_alloc" -> drawMemoryAllocVisualization(step, pulseAlpha)
                            "loop_flow" -> drawLoopFlowVisualization(step, pulseAlpha)
                            else -> drawPointerVisualization(step, pulseAlpha)
                        }
                    }

                    // Interactive Overlay Labels
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(14.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color.Black.copy(alpha = 0.7f)
                        ) {
                            Text(
                                text = when (normalizedType) {
                                    "pointer" -> "Stack Frame: 0x7FFEE4B2 (C Pointer Address)"
                                    "memory_alloc" -> "Heap Pool: 0xHEAP_B72 (Python Object Ref)"
                                    else -> "Contiguous Buffer: 0xMEM_VEC (C++ std::vector)"
                                },
                                color = SecondaryTeal,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        // Code snippet for current step
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color.Black.copy(alpha = 0.8f),
                            border = CardDefaults.outlinedCardBorder().copy(
                                brush = Brush.horizontalGradient(listOf(SecondaryTealDark, PrimaryPurple))
                            )
                        ) {
                            Text(
                                text = when (normalizedType) {
                                    "pointer" -> when (step) {
                                        1 -> "int a = 10;"
                                        2 -> "int *ptr = &a; // ptr -> 0x7FFEE4B2"
                                        else -> "*ptr = 25; // nilai a dimutasi via pointer"
                                    }
                                    "memory_alloc" -> when (step) {
                                        1 -> "data = [1, 2] # Stack name terdaftar"
                                        2 -> "# PyObject dibuat di Heap, ref_count = 1"
                                        else -> "alias = data # ref_count bertambah = 2"
                                    }
                                    else -> when (step) {
                                        1 -> "vec.push_back(10); // size: 1, cap: 2"
                                        2 -> "vec.push_back(20); // size: 2, cap: 2 (penuh)"
                                        else -> "vec.push_back(30); // Realokasi memori x2! Cap: 4"
                                    }
                                },
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                color = AccentWarning,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Step Explanation Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(BorderDark, BorderDark)))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = SecondaryTeal.copy(alpha = 0.2f),
                            modifier = Modifier.size(28.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("$step", fontWeight = FontWeight.Bold, color = SecondaryTeal, fontSize = 14.sp)
                            }
                        }
                        Text(
                            text = "Penjelasan Langkah $step dari 3",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = SecondaryTeal
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = when (normalizedType) {
                            "pointer" -> when (step) {
                                1 -> "Variabel 'a' dialokasikan pada Stack Frame di alamat 0x7FFEE4B2 dan menyimpan nilai integer 10."
                                2 -> "Pointer 'ptr' dideklarasikan dan menyimpan alamat memori dari 'a' (0x7FFEE4B2). Panah vektor putus-putus menunjukkan keterikatan pointer langsung ke sel memori 'a'."
                                else -> "Melalui dereferensi (*ptr = 25), nilai yang ada di alamat 0x7FFEE4B2 langsung diubah menjadi 25 tanpa memanggil nama variabel 'a' secara langsung."
                            }
                            "memory_alloc" -> when (step) {
                                1 -> "Variabel 'data' didefinisikan di lingkungan eksekusi (Stack). Pada titik ini, Python menyiapkan penunjuk ke blok memori bebas."
                                2 -> "Struktur data list dialokasikan di Memori Heap. PyObject dibentuk dengan reference counter bernilai 1."
                                else -> "Ketika variabel baru 'alias' menunjuk ke objek yang sama, PyObject TIDAK diduplikasi di memori, melainkan reference count bertambah menjadi 2 (hemat memori)."
                            }
                            else -> when (step) {
                                1 -> "std::vector dialokasikan di stack dengan pointer internal menunjuk ke buffer heap berkapasitas 2 elemen. Elemen pertama (10) dimasukkan."
                                2 -> "Elemen kedua (20) dimasukkan ke slot berikutnya. Buffer kini terisi penuh (size == capacity)."
                                else -> "Saat elemen ketiga (30) dimasukkan, vector secara otomatis menggandakan kapasitasnya menjadi 4, memindahkan elemen lama ke blok memori kontigu baru, dan membebaskan buffer lama."
                            }
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextPrimaryDark,
                        lineHeight = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

/**
 * Native Canvas Drawing: Background Grid Wireframe
 */
private fun DrawScope.drawGridWireframe(canvasSize: Size) {
    val stepPx = 30f
    for (x in 0..(canvasSize.width / stepPx).toInt()) {
        drawLine(
            color = Color(0xFF1E222B),
            start = Offset(x * stepPx, 0f),
            end = Offset(x * stepPx, canvasSize.height),
            strokeWidth = 1f
        )
    }
    for (y in 0..(canvasSize.height / stepPx).toInt()) {
        drawLine(
            color = Color(0xFF1E222B),
            start = Offset(0f, y * stepPx),
            end = Offset(canvasSize.width, y * stepPx),
            strokeWidth = 1f
        )
    }
}

/**
 * Template 1: Pointer & Address Dereference (C / C++)
 */
private fun DrawScope.drawPointerVisualization(step: Int, pulseAlpha: Float) {
    val boxWidth = 130.dp.toPx()
    val boxHeight = 65.dp.toPx()

    // Variable 'a' Block (Left side)
    drawRoundRect(
        color = PrimaryPurple.copy(alpha = if (step >= 1) 0.85f else 0.2f),
        topLeft = Offset(40f, 60f),
        size = Size(boxWidth, boxHeight),
        cornerRadius = CornerRadius(16f, 16f)
    )
    drawRoundRect(
        color = if (step == 3) AccentWarning else SecondaryTeal,
        topLeft = Offset(40f, 60f),
        size = Size(boxWidth, boxHeight),
        cornerRadius = CornerRadius(16f, 16f),
        style = Stroke(width = if (step == 3) 3f else 2f)
    )

    // Pointer 'ptr' Block (Right side)
    if (step >= 2) {
        val ptrTopLeft = Offset(size.width - boxWidth - 40f, 130f)
        drawRoundRect(
            color = AccentWarning.copy(alpha = 0.75f),
            topLeft = ptrTopLeft,
            size = Size(boxWidth, boxHeight),
            cornerRadius = CornerRadius(16f, 16f)
        )
        drawRoundRect(
            color = Color.White.copy(alpha = 0.6f),
            topLeft = ptrTopLeft,
            size = Size(boxWidth, boxHeight),
            cornerRadius = CornerRadius(16f, 16f),
            style = Stroke(width = 1.5f)
        )

        // Animated Pointer Arrow Line from ptr to a
        drawLine(
            color = SecondaryTeal.copy(alpha = pulseAlpha),
            start = Offset(ptrTopLeft.x, ptrTopLeft.y + boxHeight / 2),
            end = Offset(40f + boxWidth, 60f + boxHeight / 2),
            strokeWidth = 3f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
        )

        // Arrow Head Target Circle
        drawCircle(
            color = SecondaryTeal,
            radius = 7f,
            center = Offset(40f + boxWidth, 60f + boxHeight / 2)
        )
    }
}

/**
 * Template 2: Heap Allocation & Reference Counting (Python / C dynamic alloc)
 */
private fun DrawScope.drawMemoryAllocVisualization(step: Int, pulseAlpha: Float) {
    val blockWidth = 110.dp.toPx()
    val blockHeight = 55.dp.toPx()

    // Stack Variable 'data'
    drawRoundRect(
        color = PrimaryPurple.copy(alpha = 0.8f),
        topLeft = Offset(30f, 50f),
        size = Size(blockWidth, blockHeight),
        cornerRadius = CornerRadius(12f, 12f)
    )
    drawRoundRect(
        color = SecondaryTeal,
        topLeft = Offset(30f, 50f),
        size = Size(blockWidth, blockHeight),
        cornerRadius = CornerRadius(12f, 12f),
        style = Stroke(width = 2f)
    )

    // Stack Variable 'alias' (Step 3)
    if (step >= 3) {
        drawRoundRect(
            color = PrimaryPurple.copy(alpha = 0.8f),
            topLeft = Offset(30f, 140f),
            size = Size(blockWidth, blockHeight),
            cornerRadius = CornerRadius(12f, 12f)
        )
        drawRoundRect(
            color = SecondaryTeal,
            topLeft = Offset(30f, 140f),
            size = Size(blockWidth, blockHeight),
            cornerRadius = CornerRadius(12f, 12f),
            style = Stroke(width = 2f)
        )
    }

    // Heap Object Block (Step 2 & 3)
    if (step >= 2) {
        val heapX = size.width - blockWidth - 40f
        val heapY = 85f
        val heapSize = Size(blockWidth + 20f, blockHeight + 25f)

        drawRoundRect(
            color = Color(0xFF00CEC9).copy(alpha = 0.25f),
            topLeft = Offset(heapX, heapY),
            size = heapSize,
            cornerRadius = CornerRadius(14f, 14f)
        )
        drawRoundRect(
            color = SecondaryTeal,
            topLeft = Offset(heapX, heapY),
            size = heapSize,
            cornerRadius = CornerRadius(14f, 14f),
            style = Stroke(width = 2f)
        )

        // Arrow from 'data' to Heap Object
        drawLine(
            color = SecondaryTeal.copy(alpha = pulseAlpha),
            start = Offset(30f + blockWidth, 50f + blockHeight / 2),
            end = Offset(heapX, heapY + heapSize.height / 3),
            strokeWidth = 2.5f
        )
        drawCircle(
            color = SecondaryTeal,
            radius = 6f,
            center = Offset(heapX, heapY + heapSize.height / 3)
        )

        // Arrow from 'alias' to same Heap Object (Step 3)
        if (step >= 3) {
            drawLine(
                color = AccentWarning.copy(alpha = pulseAlpha),
                start = Offset(30f + blockWidth, 140f + blockHeight / 2),
                end = Offset(heapX, heapY + (heapSize.height * 2) / 3),
                strokeWidth = 2.5f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 8f), 0f)
            )
            drawCircle(
                color = AccentWarning,
                radius = 6f,
                center = Offset(heapX, heapY + (heapSize.height * 2) / 3)
            )
        }
    }
}

/**
 * Template 3: Loop Flow & Dynamic Buffer Expansion (C++ std::vector / Loop iteration)
 */
private fun DrawScope.drawLoopFlowVisualization(step: Int, pulseAlpha: Float) {
    val slotWidth = 48.dp.toPx()
    val slotHeight = 52.dp.toPx()
    val startY = 85f

    val totalSlots = if (step == 3) 4 else 2
    val filledSlots = when (step) {
        1 -> 1
        2 -> 2
        else -> 3
    }

    val totalWidth = totalSlots * (slotWidth + 10f)
    val startX = (size.width - totalWidth) / 2f

    // Draw contiguous array slots
    for (i in 0 until totalSlots) {
        val x = startX + i * (slotWidth + 10f)
        val isFilled = i < filledSlots
        val isHighlighted = i == filledSlots - 1

        drawRoundRect(
            color = when {
                isHighlighted -> AccentWarning.copy(alpha = 0.7f)
                isFilled -> PrimaryPurple.copy(alpha = 0.8f)
                else -> Color(0xFF1E222B)
            },
            topLeft = Offset(x, startY),
            size = Size(slotWidth, slotHeight),
            cornerRadius = CornerRadius(10f, 10f)
        )

        drawRoundRect(
            color = if (isHighlighted) AccentWarning else if (isFilled) SecondaryTeal else Color(0xFF333842),
            topLeft = Offset(x, startY),
            size = Size(slotWidth, slotHeight),
            cornerRadius = CornerRadius(10f, 10f),
            style = Stroke(width = if (isHighlighted) 2.5f else 1.5f)
        )
    }

    // Capacity boundary indicator
    val boundaryX = startX + totalSlots * (slotWidth + 10f) - 5f
    drawLine(
        color = SecondaryTeal.copy(alpha = pulseAlpha),
        start = Offset(boundaryX, startY - 15f),
        end = Offset(boundaryX, startY + slotHeight + 15f),
        strokeWidth = 2f,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f), 0f)
    )
}
