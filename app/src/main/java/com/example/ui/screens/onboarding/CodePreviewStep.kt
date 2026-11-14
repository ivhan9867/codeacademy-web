package com.example.ui.screens.onboarding

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.SmartButton
import androidx.compose.material.icons.rounded.Title
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CodeDarkBg
import com.example.ui.theme.CodeDarkBorder
import com.example.ui.theme.CodeDarkSurface
import com.example.ui.theme.CodeSyntaxAttr
import com.example.ui.theme.CodeSyntaxString
import com.example.ui.theme.CodeSyntaxTag
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TealPrimary

private data class SandboxPreset(
    val id: Int,
    val label: String,
    val icon: ImageVector,
    val headingText: String,
    val showTealAccent: Boolean,
    val showButton: Boolean
)

@Composable
fun CodePreviewStep(
    areAnimationsEnabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val borderCol = if (isDark) MaterialTheme.colorScheme.outlineVariant else Color(0xFFD5E3E2)

    val presets = remember {
        listOf(
            SandboxPreset(
                id = 0,
                label = "Judul Utama",
                icon = Icons.Rounded.Title,
                headingText = "Halo, Web Developer!",
                showTealAccent = false,
                showButton = false
            ),
            SandboxPreset(
                id = 1,
                label = "Tema Teal",
                icon = Icons.Rounded.Palette,
                headingText = "Desain Bersih & Rapi",
                showTealAccent = true,
                showButton = false
            ),
            SandboxPreset(
                id = 2,
                label = "Tambah Tombol",
                icon = Icons.Rounded.SmartButton,
                headingText = "Siap Interaktif!",
                showTealAccent = true,
                showButton = true
            )
        )
    }

    var selectedPresetIdx by remember { mutableIntStateOf(0) }
    val active = presets[selectedPresetIdx]

    val previewBg by animateColorAsState(
        targetValue = if (active.showTealAccent) Color(0xFFEEF6F5) else Color.White,
        animationSpec = tween(250),
        label = "previewBg"
    )
    val previewTitleCol by animateColorAsState(
        targetValue = if (active.showTealAccent) Color(0xFF104246) else Color(0xFF0D2B2E),
        animationSpec = tween(250),
        label = "previewTitleCol"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 4.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Coba Ubah Kode Secara Langsung",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Ketuk salah satu preset di bawah untuk melihat bagaimana perubahan HTML & CSS langsung memperbarui tampilan web.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Interactive preset selector chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            presets.forEachIndexed { idx, preset ->
                val isSelected = idx == selectedPresetIdx
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (isSelected) Color(0xFF104246) else (if (isDark) MaterialTheme.colorScheme.surface else Color.White),
                    border = BorderStroke(1.dp, if (isSelected) Color(0xFF104246) else borderCol),
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { selectedPresetIdx = idx }
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = preset.icon,
                            contentDescription = null,
                            tint = if (isSelected) Color.White else TealPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = preset.label,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                            maxLines = 1
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Code Card
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = CodeDarkBg,
            border = BorderStroke(1.dp, CodeDarkBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(CodeDarkSurface)
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Box(modifier = Modifier.size(9.dp).clip(CircleShape).background(Color(0xFFE26D45)))
                        Box(modifier = Modifier.size(9.dp).clip(CircleShape).background(Color(0xFFF0B429)))
                        Box(modifier = Modifier.size(9.dp).clip(CircleShape).background(SuccessGreen))
                    }
                    Text(
                        text = "index.html",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        color = Color(0xFF9FB3B5)
                    )
                }

                Column(modifier = Modifier.padding(14.dp)) {
                    val codeAnnotated = buildAnnotatedString {
                        withStyle(SpanStyle(color = CodeSyntaxTag)) { append("<section") }
                        if (active.showTealAccent) {
                            withStyle(SpanStyle(color = CodeSyntaxAttr)) { append(" class") }
                            withStyle(SpanStyle(color = Color.White)) { append("=") }
                            withStyle(SpanStyle(color = CodeSyntaxString)) { append("\"kartu-teal\"") }
                        }
                        withStyle(SpanStyle(color = CodeSyntaxTag)) { append(">\n  <h1>") }
                        withStyle(SpanStyle(color = Color.White)) { append(active.headingText) }
                        withStyle(SpanStyle(color = CodeSyntaxTag)) { append("</h1>") }
                        if (active.showButton) {
                            withStyle(SpanStyle(color = CodeSyntaxTag)) { append("\n  <button>") }
                            withStyle(SpanStyle(color = Color.White)) { append("Mulai") }
                            withStyle(SpanStyle(color = CodeSyntaxTag)) { append("</button>") }
                        }
                        withStyle(SpanStyle(color = CodeSyntaxTag)) { append("\n</section>") }
                    }
                    Text(
                        text = codeAnnotated,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 12.5.sp,
                        lineHeight = 20.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Live Preview Card
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = previewBg,
            border = BorderStroke(1.dp, if (active.showTealAccent) TealPrimary else borderCol),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Pratinjau Live",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4F6668)
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Rounded.CheckCircle,
                            contentDescription = null,
                            tint = SuccessGreen,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Tersinkron",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = SuccessGreen
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = active.headingText,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = previewTitleCol
                )

                if (active.showButton) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = {},
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF104246))
                    ) {
                        Text("Mulai", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}
