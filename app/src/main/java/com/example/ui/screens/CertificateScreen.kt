package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.R
import com.example.data.local.CertificateEntity
import com.example.util.CosmicCertificateExporter
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

private val CosmicVoid = Color(0xFF050816)
private val CosmicMidnight = Color(0xFF0B132B)
private val CosmicIndigo = Color(0xFF16234A)
private val ChampagneGold = Color(0xFFF5D77F)
private val AntiqueGold = Color(0xFFD4AF37)
private val StarlightCream = Color(0xFFFEF3C7)
private val NebulaCyan = Color(0xFF38BDF8)
private val NebulaViolet = Color(0xFF8B5CF6)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CertificateScreen(
    certificate: CertificateEntity?,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var isExporting by remember { mutableStateOf(false) }
    var showFullscreenDialog by remember { mutableStateOf(false) }
    var landscapePreviewBitmap by remember(certificate?.certId) { mutableStateOf<Bitmap?>(null) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.cert_view_top_title),
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.cert_view_back_cd)
                        )
                    }
                },
                actions = {
                    if (certificate != null) {
                        IconButton(onClick = { showFullscreenDialog = true }) {
                            Icon(
                                imageVector = Icons.Default.Fullscreen,
                                contentDescription = stringResource(R.string.cert_cosmic_btn_fullscreen)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        modifier = modifier
    ) { innerPadding ->
        if (certificate == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.cert_view_not_found),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            return@Scaffold
        }

        val predicateLabel = when {
            certificate.scorePercent >= 90 -> stringResource(R.string.cert_cosmic_pred_distinction)
            certificate.scorePercent >= 80 -> stringResource(R.string.cert_cosmic_pred_very_good)
            else -> stringResource(R.string.cert_cosmic_pred_good)
        }

        val copyCredentialId: () -> Unit = {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText(
                context.getString(R.string.cert_view_label_id),
                certificate.certId
            )
            clipboard.setPrimaryClip(clip)
            Toast.makeText(
                context,
                context.getString(R.string.cert_id_copied_toast_format, certificate.certId),
                Toast.LENGTH_SHORT
            ).show()
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Living Animated Cosmic Observatory Diploma Card
            CosmicDiplomaCard(
                certificate = certificate,
                predicateLabel = predicateLabel,
                onCopyId = copyCredentialId
            )

            // Primary Action Row: Save HD PNG + Share Certificate
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        if (isExporting) return@OutlinedButton
                        coroutineScope.launch {
                            isExporting = true
                            val labels = CosmicCertificateExporter.resolveLabels(context, certificate.scorePercent)
                            val bmp = landscapePreviewBitmap
                                ?: CosmicCertificateExporter.renderCosmicDiplomaBitmap(certificate, labels)
                                    .also { landscapePreviewBitmap = it }
                            val saved = CosmicCertificateExporter.saveCertificateToGallery(
                                context,
                                bmp,
                                certificate.certId
                            )
                            isExporting = false
                            Toast.makeText(
                                context,
                                if (saved) context.getString(R.string.cert_cosmic_saved_toast)
                                else context.getString(R.string.cert_cosmic_save_err_toast),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    },
                    enabled = !isExporting,
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, AntiqueGold.copy(alpha = 0.6f)),
                    contentPadding = PaddingValues(vertical = 13.dp, horizontal = 12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    if (isExporting) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp,
                            color = AntiqueGold
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = null,
                            tint = AntiqueGold,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isExporting) {
                            stringResource(R.string.cert_view_btn_saving)
                        } else {
                            stringResource(R.string.cert_cosmic_btn_save_png)
                        },
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Button(
                    onClick = {
                        if (isExporting) return@Button
                        coroutineScope.launch {
                            isExporting = true
                            val caption = context.getString(
                                R.string.cert_cosmic_share_text_format,
                                certificate.courseTitle,
                                certificate.userName,
                                certificate.scorePercent,
                                certificate.certId
                            )
                            val labels = CosmicCertificateExporter.resolveLabels(context, certificate.scorePercent)
                            val bmp = landscapePreviewBitmap
                                ?: CosmicCertificateExporter.renderCosmicDiplomaBitmap(certificate, labels)
                                    .also { landscapePreviewBitmap = it }
                            val imageIntent = CosmicCertificateExporter.prepareShareableImageIntent(
                                context,
                                bmp,
                                certificate.certId,
                                caption
                            )
                            isExporting = false
                            val finalIntent = imageIntent ?: Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, caption)
                            }
                            runCatching {
                                context.startActivity(
                                    Intent.createChooser(
                                        finalIntent,
                                        context.getString(R.string.cert_view_share_chooser_title)
                                    )
                                )
                            }.onFailure {
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.cert_view_share_error_toast),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    },
                    enabled = !isExporting,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CosmicMidnight,
                        contentColor = ChampagneGold
                    ),
                    shape = RoundedCornerShape(14.dp),
                    contentPadding = PaddingValues(vertical = 13.dp, horizontal = 14.dp),
                    modifier = Modifier.weight(1.1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = null,
                        tint = ChampagneGold,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = stringResource(R.string.cert_view_share_cd),
                        fontWeight = FontWeight.Bold,
                        color = ChampagneGold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Secondary Row: Fullscreen Landscape Preview & Copy ID
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = { showFullscreenDialog = true },
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                    contentPadding = PaddingValues(vertical = 10.dp, horizontal = 12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Fullscreen,
                        contentDescription = null,
                        modifier = Modifier.size(17.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = stringResource(R.string.cert_cosmic_btn_fullscreen),
                        style = MaterialTheme.typography.labelLarge,
                        maxLines = 1
                    )
                }

                OutlinedButton(
                    onClick = copyCredentialId,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                    contentPadding = PaddingValues(vertical = 10.dp, horizontal = 12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = null,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = stringResource(R.string.cert_cosmic_btn_copy_id),
                        style = MaterialTheme.typography.labelLarge,
                        maxLines = 1
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
        }

        // Fullscreen HD Landscape Diploma Preview Dialog
        if (showFullscreenDialog) {
            LaunchedEffect(certificate.certId) {
                if (landscapePreviewBitmap == null) {
                    val labels = CosmicCertificateExporter.resolveLabels(context, certificate.scorePercent)
                    landscapePreviewBitmap = CosmicCertificateExporter.renderCosmicDiplomaBitmap(certificate, labels)
                }
            }
            Dialog(
                onDismissRequest = { showFullscreenDialog = false },
                properties = DialogProperties(usePlatformDefaultWidth = false)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xEE030612))
                        .padding(14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(R.string.cert_cosmic_subtitle),
                                color = ChampagneGold,
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.2.sp
                            )
                            IconButton(
                                onClick = { showFullscreenDialog = false },
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.1f))
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = stringResource(R.string.common_close),
                                    tint = Color.White
                                )
                            }
                        }

                        val bmp = landscapePreviewBitmap
                        if (bmp != null) {
                            Image(
                                bitmap = bmp.asImageBitmap(),
                                contentDescription = certificate.courseTitle,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(16f / 10f)
                                    .clip(RoundedCornerShape(14.dp))
                                    .border(
                                        1.5.dp,
                                        ChampagneGold.copy(alpha = 0.7f),
                                        RoundedCornerShape(14.dp)
                                    )
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(16f / 10f),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(color = ChampagneGold)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CosmicDiplomaCard(
    certificate: CertificateEntity,
    predicateLabel: String,
    onCopyId: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "cosmic_cert")
    val twinklePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 6.283185f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "twinkle_phase"
    )
    val orbitRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 48000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "orbit_rotation"
    )
    val sealGlow by infiniteTransition.animateFloat(
        initialValue = 0.65f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "seal_glow"
    )

    val stars = remember(certificate.certId) {
        val rng = Random(certificate.certId.hashCode().toLong() xor 0x57A12L)
        List(84) { idx ->
            CosmicStar(
                xFrac = rng.nextFloat(),
                yFrac = rng.nextFloat(),
                radiusDp = 0.7f + rng.nextFloat() * 1.4f,
                phaseOffset = rng.nextFloat() * 6.283185f,
                tintType = idx % 9
            )
        }
    }

    val goldFoilBrush = remember {
        Brush.linearGradient(
            listOf(AntiqueGold, StarlightCream, ChampagneGold, AntiqueGold)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .border(
                width = 1.8.dp,
                brush = goldFoilBrush,
                shape = RoundedCornerShape(24.dp)
            )
    ) {
        // Animated Cosmic Deep-Space Canvas Backdrop
        Canvas(modifier = Modifier.matchParentSize()) {
            val w = size.width
            val h = size.height
            val center = Offset(w / 2f, h * 0.44f)

            // Deep space radial nebula base
            drawRect(
                brush = Brush.radialGradient(
                    colors = listOf(CosmicIndigo, CosmicMidnight, CosmicVoid),
                    center = Offset(w * 0.5f, h * 0.38f),
                    radius = maxOf(w, h) * 0.85f
                )
            )

            // Atmospheric Cyan & Violet Nebula Clouds
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(NebulaCyan.copy(alpha = 0.14f), Color.Transparent),
                    center = Offset(w * 0.16f, h * 0.2f),
                    radius = w * 0.52f
                ),
                center = Offset(w * 0.16f, h * 0.2f),
                radius = w * 0.52f
            )
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(NebulaViolet.copy(alpha = 0.13f), Color.Transparent),
                    center = Offset(w * 0.86f, h * 0.78f),
                    radius = w * 0.55f
                ),
                center = Offset(w * 0.86f, h * 0.78f),
                radius = w * 0.55f
            )

            // Procedural Twinkling Starfield
            stars.forEach { star ->
                val pulse = 0.45f + 0.55f * ((sin((twinklePhase + star.phaseOffset).toDouble()).toFloat() + 1f) / 2f)
                val color = when (star.tintType) {
                    0 -> ChampagneGold.copy(alpha = pulse * 0.9f)
                    1 -> NebulaCyan.copy(alpha = pulse * 0.8f)
                    else -> Color.White.copy(alpha = pulse * 0.75f)
                }
                drawCircle(
                    color = color,
                    radius = star.radiusDp.dp.toPx(),
                    center = Offset(star.xFrac * w, star.yFrac * h)
                )
            }

            // Subtle Rotating Central Astrolabe Watermark
            val baseR = w * 0.34f
            drawCircle(
                color = ChampagneGold.copy(alpha = 0.09f),
                radius = baseR,
                center = center,
                style = Stroke(width = 1.dp.toPx())
            )
            drawCircle(
                color = ChampagneGold.copy(alpha = 0.06f),
                radius = baseR * 0.72f,
                center = center,
                style = Stroke(width = 0.8.dp.toPx())
            )

            rotate(degrees = orbitRotation, pivot = center) {
                for (deg in 0 until 360 step 15) {
                    val rad = Math.toRadians(deg.toDouble())
                    val rIn = baseR * if (deg % 45 == 0) 0.91f else 0.95f
                    drawLine(
                        color = ChampagneGold.copy(alpha = 0.12f),
                        start = Offset(
                            center.x + rIn * cos(rad).toFloat(),
                            center.y + rIn * sin(rad).toFloat()
                        ),
                        end = Offset(
                            center.x + baseR * cos(rad).toFloat(),
                            center.y + baseR * sin(rad).toFloat()
                        ),
                        strokeWidth = 0.9.dp.toPx()
                    )
                }
            }

            // Tilted Ecliptic Orbital Ellipse
            rotate(degrees = -22f, pivot = center) {
                drawOval(
                    color = ChampagneGold.copy(alpha = 0.09f),
                    topLeft = Offset(center.x - w * 0.44f, center.y - w * 0.13f),
                    size = Size(w * 0.88f, w * 0.26f),
                    style = Stroke(width = 1.dp.toPx())
                )
            }

            // Inner Hairline Gold Border
            val inset = 12.dp.toPx()
            drawRoundRect(
                color = ChampagneGold.copy(alpha = 0.35f),
                topLeft = Offset(inset, inset),
                size = Size(w - inset * 2, h - inset * 2),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(16.dp.toPx(), 16.dp.toPx()),
                style = Stroke(width = 0.9.dp.toPx())
            )

            // Corner Constellations (Top-Left & Top-Right)
            val lineColor = ChampagneGold.copy(alpha = 0.36f)
            val nodeColor = ChampagneGold.copy(alpha = 0.85f)
            val tl = listOf(
                Offset(24.dp.toPx(), 42.dp.toPx()),
                Offset(38.dp.toPx(), 26.dp.toPx()),
                Offset(58.dp.toPx(), 32.dp.toPx()),
                Offset(74.dp.toPx(), 22.dp.toPx())
            )
            val tr = tl.map { Offset(w - it.x, it.y) }
            listOf(tl, tr).forEach { cluster ->
                for (i in 0 until cluster.lastIndex) {
                    drawLine(lineColor, cluster[i], cluster[i + 1], strokeWidth = 0.8.dp.toPx())
                }
                cluster.forEach { pt ->
                    drawCircle(nodeColor, radius = 2.dp.toPx(), center = pt)
                }
            }
        }

        // Foreground Diploma Content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Polaris Orbital Gold Seal
            Box(
                modifier = Modifier.size(72.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val c = Offset(size.width / 2f, size.height / 2f)
                    val r = size.minDimension * 0.36f

                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(ChampagneGold.copy(alpha = 0.28f * sealGlow), Color.Transparent),
                            center = c,
                            radius = size.minDimension * 0.5f
                        )
                    )
                    drawCircle(color = CosmicMidnight, radius = r, center = c)
                    drawCircle(
                        color = ChampagneGold,
                        radius = r,
                        center = c,
                        style = Stroke(width = 1.6.dp.toPx())
                    )
                    rotate(degrees = -26f, pivot = c) {
                        drawOval(
                            color = StarlightCream.copy(alpha = 0.55f),
                            topLeft = Offset(c.x - r * 1.38f, c.y - r * 0.34f),
                            size = Size(r * 2.76f, r * 0.68f),
                            style = Stroke(width = 0.9.dp.toPx())
                        )
                    }
                    // 4-point Polaris Star
                    val outer = r * 0.58f
                    val inner = r * 0.17f
                    val star = Path().apply {
                        moveTo(c.x, c.y - outer)
                        lineTo(c.x + inner, c.y - inner)
                        lineTo(c.x + outer, c.y)
                        lineTo(c.x + inner, c.y + inner)
                        lineTo(c.x, c.y + outer)
                        lineTo(c.x - inner, c.y + inner)
                        lineTo(c.x - outer, c.y)
                        lineTo(c.x - inner, c.y - inner)
                        close()
                    }
                    drawPath(star, color = StarlightCream)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = stringResource(R.string.cert_cosmic_eyebrow),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = ChampagneGold,
                letterSpacing = 2.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = stringResource(R.string.cert_view_header_title),
                fontSize = 21.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                letterSpacing = 1.2.sp,
                textAlign = TextAlign.Center
            )

            // Fine gold hairline divider with center star dot
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth(0.55f)
                    .padding(vertical = 12.dp)
            ) {
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = ChampagneGold.copy(alpha = 0.45f)
                )
                Box(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .size(5.dp)
                        .clip(CircleShape)
                        .background(ChampagneGold)
                )
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = ChampagneGold.copy(alpha = 0.45f)
                )
            }

            Text(
                text = stringResource(R.string.cert_view_awarded_to),
                fontSize = 12.sp,
                fontFamily = FontFamily.Serif,
                fontStyle = FontStyle.Italic,
                color = Color(0xFFCBD5E1),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Metallic Gold Foil Recipient Name
            Text(
                text = certificate.userName,
                style = TextStyle(
                    brush = goldFoilBrush,
                    fontSize = 26.sp,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.padding(horizontal = 4.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = stringResource(R.string.cert_view_completion_desc),
                fontSize = 11.5.sp,
                color = Color(0xFF94A3B8),
                textAlign = TextAlign.Center,
                lineHeight = 17.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = certificate.courseTitle,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Score + Predicate Cosmic Pill
            Surface(
                shape = RoundedCornerShape(999.dp),
                color = ChampagneGold.copy(alpha = 0.14f),
                border = BorderStroke(1.dp, ChampagneGold.copy(alpha = 0.5f))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = null,
                        tint = ChampagneGold,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${certificate.scorePercent}% • $predicateLabel",
                        color = StarlightCream,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Frosted Midnight-Glass Verification & Signature Strip
            Surface(
                color = CosmicVoid.copy(alpha = 0.68f),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, ChampagneGold.copy(alpha = 0.25f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = stringResource(R.string.cert_view_label_date),
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.8.sp,
                                color = Color(0xFF94A3B8)
                            )
                            Text(
                                text = certificate.issueDate,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }
                        Column(
                            horizontalAlignment = Alignment.End,
                            modifier = Modifier.weight(1.2f)
                        ) {
                            Text(
                                text = stringResource(R.string.cert_cosmic_issuer_role),
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.4.sp,
                                color = Color(0xFF94A3B8),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = stringResource(R.string.cert_cosmic_issuer_name),
                                fontSize = 12.sp,
                                fontFamily = FontFamily.Serif,
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.SemiBold,
                                color = ChampagneGold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    HorizontalDivider(color = ChampagneGold.copy(alpha = 0.16f))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .clickable(onClick = onCopyId)
                            .padding(vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = stringResource(R.string.cert_view_label_id),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.8.sp,
                                color = Color(0xFF94A3B8)
                            )
                            Text(
                                text = certificate.certId,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                color = NebulaCyan,
                                fontSize = 12.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = stringResource(R.string.cert_cosmic_btn_copy_id),
                            tint = ChampagneGold,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

private data class CosmicStar(
    val xFrac: Float,
    val yFrac: Float,
    val radiusDp: Float,
    val phaseOffset: Float,
    val tintType: Int
)
