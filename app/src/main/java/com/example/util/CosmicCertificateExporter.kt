package com.example.util

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RadialGradient
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.Typeface
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import androidx.core.content.FileProvider
import com.example.R
import com.example.data.local.CertificateEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

/**
 * Renders a 1920x1200 high-resolution "Cosmic Observatory" Deep-Space & Champagne Gold-Foil
 * graduation diploma PNG for saving to the device Gallery or sharing via FileProvider.
 */
object CosmicCertificateExporter {

    private const val WIDTH = 1920
    private const val HEIGHT = 1200

    data class CertificateRenderLabels(
        val eyebrow: String,
        val mainTitle: String,
        val awardedTo: String,
        val completionDesc: String,
        val predicate: String,
        val verifiedStamp: String,
        val scoreLabel: String,
        val dateLabel: String,
        val idLabel: String,
        val issuerName: String,
        val issuerRole: String,
        val footerVerify: String
    )

    fun resolveLabels(context: Context, scorePercent: Int): CertificateRenderLabels {
        val predicate = when {
            scorePercent >= 90 -> context.getString(R.string.cert_cosmic_pred_distinction)
            scorePercent >= 80 -> context.getString(R.string.cert_cosmic_pred_very_good)
            else -> context.getString(R.string.cert_cosmic_pred_good)
        }
        return CertificateRenderLabels(
            eyebrow = context.getString(R.string.cert_cosmic_eyebrow),
            mainTitle = context.getString(R.string.cert_view_header_title),
            awardedTo = context.getString(R.string.cert_view_awarded_to),
            completionDesc = context.getString(R.string.cert_view_completion_desc),
            predicate = predicate,
            verifiedStamp = context.getString(R.string.cert_view_verified_stamp),
            scoreLabel = context.getString(R.string.cert_view_label_score),
            dateLabel = context.getString(R.string.cert_view_label_date),
            idLabel = context.getString(R.string.cert_view_label_id),
            issuerName = context.getString(R.string.cert_cosmic_issuer_name),
            issuerRole = context.getString(R.string.cert_cosmic_issuer_role),
            footerVerify = context.getString(R.string.cert_cosmic_verify_footer)
        )
    }

    suspend fun renderCosmicDiplomaBitmap(
        certificate: CertificateEntity,
        labels: CertificateRenderLabels
    ): Bitmap = withContext(Dispatchers.Default) {
        val bmp = Bitmap.createBitmap(WIDTH, HEIGHT, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmp)
        val cx = WIDTH / 2f
        val cy = HEIGHT / 2f

        // 1. Deep-space base radial gradient
        val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            shader = RadialGradient(
                cx, cy * 0.82f, WIDTH * 0.72f,
                intArrayOf(
                    Color.parseColor("#16234A"),
                    Color.parseColor("#0B132B"),
                    Color.parseColor("#050816")
                ),
                floatArrayOf(0f, 0.55f, 1f),
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawRect(0f, 0f, WIDTH.toFloat(), HEIGHT.toFloat(), bgPaint)

        // Subtle cyan & violet nebula glows
        drawNebulaGlow(canvas, WIDTH * 0.18f, HEIGHT * 0.22f, 520f, Color.parseColor("#2438BDF8"))
        drawNebulaGlow(canvas, WIDTH * 0.84f, HEIGHT * 0.78f, 560f, Color.parseColor("#208B5CF6"))
        drawNebulaGlow(canvas, cx, HEIGHT * 0.14f, 440f, Color.parseColor("#1EF5D77F"))

        // 2. Deterministic starfield with subtle diffraction flares
        val rng = Random(certificate.certId.hashCode().toLong() xor 0x4C05A1CL)
        val starPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        for (i in 0 until 170) {
            val sx = rng.nextFloat() * WIDTH
            val sy = rng.nextFloat() * HEIGHT
            val r = 0.8f + rng.nextFloat() * 2.1f
            val alpha = (55 + rng.nextInt(185)).coerceIn(40, 240)
            starPaint.color = when {
                i % 7 == 0 -> Color.argb(alpha, 245, 215, 127)
                i % 11 == 0 -> Color.argb(alpha, 186, 230, 253)
                else -> Color.argb(alpha, 248, 250, 252)
            }
            canvas.drawCircle(sx, sy, r, starPaint)

            if (i % 23 == 0) {
                val flare = r * 4.5f
                starPaint.strokeWidth = 1.1f
                canvas.drawLine(sx - flare, sy, sx + flare, sy, starPaint)
                canvas.drawLine(sx, sy - flare, sx, sy + flare, starPaint)
            }
        }

        // 3. Central Astrolabe / Armillary Watermark
        drawAstrolabeWatermark(canvas, cx, cy * 0.96f)

        // 4. Double Champagne-Gold Geometric Frame + Corner Constellations
        val goldFoilShader = LinearGradient(
            60f, 60f, WIDTH - 60f, HEIGHT - 60f,
            intArrayOf(
                Color.parseColor("#D4AF37"),
                Color.parseColor("#FEF3C7"),
                Color.parseColor("#EAB308"),
                Color.parseColor("#F5D77F")
            ),
            floatArrayOf(0f, 0.35f, 0.7f, 1f),
            Shader.TileMode.CLAMP
        )
        val outerFramePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 3.5f
            shader = goldFoilShader
        }
        val innerFramePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 1.4f
            color = Color.argb(125, 245, 215, 127)
        }
        canvas.drawRoundRect(RectF(48f, 48f, WIDTH - 48f, HEIGHT - 48f), 28f, 28f, outerFramePaint)
        canvas.drawRoundRect(RectF(68f, 68f, WIDTH - 68f, HEIGHT - 68f), 18f, 18f, innerFramePaint)

        drawCornerConstellations(canvas)

        // 5. Top-Center Polaris Orbital Gold Seal
        drawPolarisSeal(canvas, cx, 164f, 54f)

        // 6. Typography Hierarchy
        val eyebrowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#F5D77F")
            textSize = 23f
            letterSpacing = 0.28f
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText(labels.eyebrow.uppercase(), cx, 266f, eyebrowPaint)

        val titlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            textSize = 58f
            letterSpacing = 0.12f
            typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText(labels.mainTitle.uppercase(), cx, 344f, titlePaint)

        // Fine gold divider under title with tiny center diamond
        val divPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.argb(165, 245, 215, 127)
            strokeWidth = 1.8f
        }
        canvas.drawLine(cx - 260f, 376f, cx - 18f, 376f, divPaint)
        canvas.drawLine(cx + 18f, 376f, cx + 260f, 376f, divPaint)
        drawDiamond(canvas, cx, 376f, 8f, Color.parseColor("#F5D77F"))

        val awardedPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#CBD5E1")
            textSize = 26f
            typeface = Typeface.create(Typeface.SERIF, Typeface.ITALIC)
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText(labels.awardedTo, cx, 434f, awardedPaint)

        // Recipient Name in Metallic Gold Foil
        val namePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = if (certificate.userName.length > 22) 64f else 76f
            typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
            textAlign = Paint.Align.CENTER
            shader = LinearGradient(
                cx - 420f, 460f, cx + 420f, 540f,
                intArrayOf(
                    Color.parseColor("#D4AF37"),
                    Color.parseColor("#FFFBEB"),
                    Color.parseColor("#F5D77F"),
                    Color.parseColor("#D4AF37")
                ),
                null,
                Shader.TileMode.CLAMP
            )
            setShadowLayer(18f, 0f, 4f, Color.argb(110, 245, 215, 127))
        }
        canvas.drawText(certificate.userName, cx, 530f, namePaint)

        // Subtle gold flourish rule below recipient name
        val subRulePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.argb(100, 245, 215, 127)
            strokeWidth = 1.4f
        }
        canvas.drawLine(cx - 340f, 558f, cx + 340f, 558f, subRulePaint)

        // Completion Description (wrapped cleanly)
        val descPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#CBD5E1")
            textSize = 24f
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
        }
        drawCenteredMultilineText(canvas, labels.completionDesc, descPaint, cx, 588f, 1260)

        // Course Title
        val coursePaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            textSize = 40f
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
        }
        drawCenteredMultilineText(canvas, certificate.courseTitle, coursePaint, cx, 678f, 1380)

        // Score & Predicate Cosmic Pill
        val badgeText = "★  ${certificate.scorePercent}%  •  ${labels.predicate}  •  ${labels.verifiedStamp}"
        val badgeTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#FEF3C7")
            textSize = 22f
            letterSpacing = 0.06f
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            textAlign = Paint.Align.CENTER
        }
        val badgeWidth = (badgeTextPaint.measureText(badgeText) + 72f).coerceAtMost(1320f)
        val pillRect = RectF(cx - badgeWidth / 2f, 774f, cx + badgeWidth / 2f, 830f)
        val pillBg = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.argb(42, 245, 215, 127)
        }
        val pillBorder = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 1.6f
            color = Color.argb(150, 245, 215, 127)
        }
        canvas.drawRoundRect(pillRect, 28f, 28f, pillBg)
        canvas.drawRoundRect(pillRect, 28f, 28f, pillBorder)
        canvas.drawText(badgeText, cx, 810f, badgeTextPaint)

        // 7. Bottom Footer: 3 Columns (Date | Credential ID | Academic Board Signature)
        val footerTop = 918f
        val footerCardRect = RectF(132f, footerTop, WIDTH - 132f, 1086f)
        val footerBg = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.argb(120, 8, 15, 36)
        }
        val footerBorder = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 1.2f
            color = Color.argb(80, 148, 163, 184)
        }
        canvas.drawRoundRect(footerCardRect, 20f, 20f, footerBg)
        canvas.drawRoundRect(footerCardRect, 20f, 20f, footerBorder)

        val colLabelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#94A3B8")
            textSize = 17f
            letterSpacing = 0.14f
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            textAlign = Paint.Align.CENTER
        }
        val colValuePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            textSize = 24f
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            textAlign = Paint.Align.CENTER
        }
        val monoPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#38BDF8")
            textSize = 24f
            typeface = Typeface.create(Typeface.MONOSPACE, Typeface.BOLD)
            textAlign = Paint.Align.CENTER
        }

        val col1X = 390f
        val col2X = cx
        val col3X = WIDTH - 390f

        // Vertical hairline separators
        val sepPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.argb(65, 245, 215, 127)
            strokeWidth = 1.2f
        }
        canvas.drawLine(670f, footerTop + 26f, 670f, 1060f, sepPaint)
        canvas.drawLine(WIDTH - 670f, footerTop + 26f, WIDTH - 670f, 1060f, sepPaint)

        // Column 1: Issued Date
        canvas.drawText(labels.dateLabel.uppercase(), col1X, footerTop + 56f, colLabelPaint)
        canvas.drawText(certificate.issueDate, col1X, footerTop + 104f, colValuePaint)

        // Column 2: Credential ID
        canvas.drawText(labels.idLabel.uppercase(), col2X, footerTop + 56f, colLabelPaint)
        canvas.drawText(certificate.certId, col2X, footerTop + 104f, monoPaint)

        // Column 3: Issuer Signature
        canvas.drawText(labels.issuerRole.uppercase(), col3X, footerTop + 56f, colLabelPaint)
        val sigPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#F5D77F")
            textSize = 26f
            typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD_ITALIC)
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText(labels.issuerName, col3X, footerTop + 104f, sigPaint)

        // Bottom Micro Verification Line
        val microPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#64748B")
            textSize = 17f
            letterSpacing = 0.08f
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText(labels.footerVerify, cx, 1134f, microPaint)

        bmp
    }

    suspend fun saveCertificateToGallery(
        context: Context,
        bitmap: Bitmap,
        certId: String
    ): Boolean = withContext(Dispatchers.IO) {
        val safeId = certId.replace(Regex("[^A-Za-z0-9_-]"), "_")
        val fileName = "CodeAcademy_Certificate_${safeId}.png"
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val values = ContentValues().apply {
                    put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
                    put(MediaStore.Images.Media.MIME_TYPE, "image/png")
                    put(MediaStore.Images.Media.RELATIVE_PATH, "${Environment.DIRECTORY_PICTURES}/CodeAcademy")
                    put(MediaStore.Images.Media.IS_PENDING, 1)
                }
                val resolver = context.contentResolver
                val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                    ?: return@withContext false
                resolver.openOutputStream(uri)?.use { out ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                } ?: return@withContext false
                values.clear()
                values.put(MediaStore.Images.Media.IS_PENDING, 0)
                resolver.update(uri, values, null, null)
                true
            } else {
                val dir = File(
                    context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    "CodeAcademy"
                ).apply { mkdirs() }
                val file = File(dir, fileName)
                FileOutputStream(file).use { out ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                }
                true
            }
        } catch (_: Throwable) {
            false
        }
    }

    suspend fun prepareShareableImageIntent(
        context: Context,
        bitmap: Bitmap,
        certId: String,
        caption: String
    ): Intent? = withContext(Dispatchers.IO) {
        try {
            val safeId = certId.replace(Regex("[^A-Za-z0-9_-]"), "_")
            val certDir = File(context.cacheDir, "certificates").apply { mkdirs() }
            val pngFile = File(certDir, "CodeAcademy_${safeId}.png")
            FileOutputStream(pngFile).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                pngFile
            )
            Intent(Intent.ACTION_SEND).apply {
                type = "image/png"
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_TEXT, caption)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
        } catch (_: Throwable) {
            null
        }
    }

    private fun drawNebulaGlow(canvas: Canvas, x: Float, y: Float, radius: Float, centerColor: Int) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            shader = RadialGradient(
                x, y, radius,
                intArrayOf(centerColor, Color.TRANSPARENT),
                floatArrayOf(0f, 1f),
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawCircle(x, y, radius, paint)
    }

    private fun drawAstrolabeWatermark(canvas: Canvas, cx: Float, cy: Float) {
        val ringPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            color = Color.argb(30, 245, 215, 127)
            strokeWidth = 1.6f
        }
        val fineRingPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            color = Color.argb(20, 148, 163, 184)
            strokeWidth = 1.1f
        }
        val radii = floatArrayOf(190f, 290f, 385f, 470f)
        radii.forEachIndexed { idx, r ->
            canvas.drawCircle(cx, cy, r, if (idx % 2 == 0) ringPaint else fineRingPaint)
        }

        // Tilted ecliptic orbital ellipses
        canvas.save()
        canvas.rotate(-22f, cx, cy)
        canvas.drawOval(RectF(cx - 560f, cy - 175f, cx + 560f, cy + 175f), ringPaint)
        canvas.restore()

        canvas.save()
        canvas.rotate(18f, cx, cy)
        canvas.drawOval(RectF(cx - 500f, cy - 140f, cx + 500f, cy + 140f), fineRingPaint)
        canvas.restore()

        // Degree ticks along outer ring
        for (deg in 0 until 360 step 10) {
            val rad = Math.toRadians(deg.toDouble())
            val inner = if (deg % 30 == 0) 454f else 462f
            val x1 = cx + inner * cos(rad).toFloat()
            val y1 = cy + inner * sin(rad).toFloat()
            val x2 = cx + 470f * cos(rad).toFloat()
            val y2 = cy + 470f * sin(rad).toFloat()
            canvas.drawLine(x1, y1, x2, y2, ringPaint)
        }
    }

    private fun drawCornerConstellations(canvas: Canvas) {
        val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.argb(95, 245, 215, 127)
            strokeWidth = 1.5f
        }
        val dotPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#F5D77F")
        }

        fun drawCluster(pts: List<Pair<Float, Float>>) {
            for (i in 0 until pts.lastIndex) {
                canvas.drawLine(pts[i].first, pts[i].second, pts[i + 1].first, pts[i + 1].second, linePaint)
            }
            pts.forEachIndexed { idx, (x, y) ->
                canvas.drawCircle(x, y, if (idx == 0 || idx == pts.lastIndex) 4.2f else 3f, dotPaint)
            }
        }

        // Top-left constellation
        drawCluster(listOf(108f to 160f, 146f to 116f, 206f to 134f, 260f to 104f))
        // Top-right constellation
        drawCluster(listOf(WIDTH - 108f to 160f, WIDTH - 146f to 116f, WIDTH - 206f to 134f, WIDTH - 260f to 104f))
        // Bottom-left constellation
        drawCluster(listOf(108f to HEIGHT - 156f, 152f to HEIGHT - 114f, 214f to HEIGHT - 128f))
        // Bottom-right constellation
        drawCluster(listOf(WIDTH - 108f to HEIGHT - 156f, WIDTH - 152f to HEIGHT - 114f, WIDTH - 214f to HEIGHT - 128f))
    }

    private fun drawPolarisSeal(canvas: Canvas, cx: Float, cy: Float, r: Float) {
        // Outer glow
        drawNebulaGlow(canvas, cx, cy, r * 2.1f, Color.argb(75, 245, 215, 127))

        val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.argb(230, 11, 19, 43)
        }
        val ringPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 3f
            color = Color.parseColor("#F5D77F")
        }
        val innerRingPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 1.2f
            color = Color.argb(160, 254, 243, 199)
        }
        canvas.drawCircle(cx, cy, r, fillPaint)
        canvas.drawCircle(cx, cy, r, ringPaint)
        canvas.drawCircle(cx, cy, r * 0.78f, innerRingPaint)

        // Tilted mini orbital ring
        canvas.save()
        canvas.rotate(-25f, cx, cy)
        canvas.drawOval(RectF(cx - r * 1.38f, cy - r * 0.34f, cx + r * 1.38f, cy + r * 0.34f), innerRingPaint)
        canvas.restore()

        // 4-point Polaris Star
        val starPath = Path().apply {
            val outer = r * 0.56f
            val inner = r * 0.16f
            moveTo(cx, cy - outer)
            lineTo(cx + inner, cy - inner)
            lineTo(cx + outer, cy)
            lineTo(cx + inner, cy + inner)
            lineTo(cx, cy + outer)
            lineTo(cx - inner, cy + inner)
            lineTo(cx - outer, cy)
            lineTo(cx - inner, cy - inner)
            close()
        }
        val starFill = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#FEF3C7")
        }
        canvas.drawPath(starPath, starFill)
    }

    private fun drawDiamond(canvas: Canvas, cx: Float, cy: Float, r: Float, color: Int) {
        val path = Path().apply {
            moveTo(cx, cy - r)
            lineTo(cx + r, cy)
            lineTo(cx, cy + r)
            lineTo(cx - r, cy)
            close()
        }
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply { this.color = color }
        canvas.drawPath(path, paint)
    }

    private fun drawCenteredMultilineText(
        canvas: Canvas,
        text: String,
        paint: TextPaint,
        centerX: Float,
        topY: Float,
        maxWidth: Int
    ) {
        val layout = StaticLayout.Builder
            .obtain(text, 0, text.length, paint, maxWidth)
            .setAlignment(Layout.Alignment.ALIGN_CENTER)
            .setIncludePad(false)
            .build()
        canvas.save()
        canvas.translate(centerX - maxWidth / 2f, topY)
        layout.draw(canvas)
        canvas.restore()
    }
}
