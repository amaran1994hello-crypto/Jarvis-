package com.example.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.components.ArcReactorCanvas
import com.example.ui.theme.JarvisBackground
import com.example.ui.theme.JarvisCardBorder
import com.example.ui.theme.JarvisCyan
import com.example.ui.theme.JarvisCyanGlow
import com.example.ui.theme.JarvisGold
import com.example.ui.theme.JarvisGreenOnline
import com.example.ui.theme.JarvisRedAlert
import com.example.ui.theme.JarvisSurface
import com.example.ui.theme.JarvisSurfaceVariant
import com.example.ui.theme.JarvisTextPrimary
import com.example.ui.theme.JarvisTextSecondary
import com.example.viewmodel.JarvisViewModel

@Composable
fun CoreHudScreen(viewModel: JarvisViewModel) {
    val diagnostics by viewModel.diagnostics.collectAsState()
    val isListening by viewModel.voiceEngine.isListening.collectAsState()
    val isSpeaking by viewModel.voiceEngine.isSpeaking.collectAsState()
    val amplitude by viewModel.voiceEngine.rmsAmplitude.collectAsState()
    val lastSpokenText by viewModel.voiceEngine.lastSpokenText.collectAsState()
    val settings by viewModel.settings.collectAsState()
    val telecomRegistered by viewModel.telecomHelper.isTelecomRegistered.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(JarvisBackground)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Futuristic Top Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(JarvisSurface)
                .border(1.dp, JarvisCardBorder, RoundedCornerShape(16.dp))
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "JARVIS 4.0 // PROFESSIONAL AI",
                    color = JarvisCyan,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp
                )
                Text(
                    text = "COMMANDER: ${settings.founderName.uppercase()} (FOUNDER)",
                    color = JarvisGold,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(if (telecomRegistered) JarvisGreenOnline else JarvisRedAlert)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (telecomRegistered) "TELECOM ONLINE" else "INITIALIZING",
                    color = if (telecomRegistered) JarvisGreenOnline else JarvisRedAlert,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Centerpiece Animated Arc Reactor
        Box(
            modifier = Modifier.padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            ArcReactorCanvas(
                amplitude = amplitude,
                isListening = isListening,
                isSpeaking = isSpeaking
            )
        }

        // Live Voice Status Indicator
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(
                    if (isListening) JarvisGold.copy(alpha = 0.2f)
                    else if (isSpeaking) JarvisCyan.copy(alpha = 0.2f)
                    else JarvisSurfaceVariant
                )
                .border(
                    1.dp,
                    if (isListening) JarvisGold else if (isSpeaking) JarvisCyan else JarvisCardBorder,
                    RoundedCornerShape(20.dp)
                )
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (isSpeaking) Icons.AutoMirrored.Filled.VolumeUp else Icons.Default.Mic,
                contentDescription = null,
                tint = if (isListening) JarvisGold else if (isSpeaking) JarvisCyan else JarvisTextSecondary,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = when {
                    isListening -> "Listening to Founder Amar..."
                    isSpeaking -> "JARVIS Synthesizing Voice..."
                    else -> "All Subsystems Nominal // Ready"
                },
                color = if (isListening) JarvisGold else if (isSpeaking) JarvisCyan else JarvisTextPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Voice Response Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(JarvisSurface)
                .border(1.dp, JarvisCardBorder, RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "JARVIS VOCAL SYNTHESIS",
                        color = JarvisCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "AI-AUDIO-DSP",
                        color = JarvisTextSecondary,
                        fontSize = 10.sp
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "\"$lastSpokenText\"",
                    color = JarvisTextPrimary,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Hero Direct Call to Amar Button
        Button(
            onClick = { viewModel.triggerCallToAmar() },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .testTag("call_founder_button"),
            colors = ButtonDefaults.buttonColors(
                containerColor = JarvisCyan,
                contentColor = Color(0xFF001A24)
            ),
            shape = RoundedCornerShape(14.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Call,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "CALL FOUNDER AMAR'S DEVICE",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Voice Command Microphone FAB Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(68.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = if (isListening) listOf(JarvisGold, Color(0xFF9E7000))
                            else listOf(JarvisCyan, Color(0xFF006880))
                        )
                    )
                    .clickable { viewModel.toggleVoiceListening() }
                    .testTag("voice_listen_fab"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isListening) Icons.Default.MicOff else Icons.Default.Mic,
                    contentDescription = "Voice Command",
                    tint = Color(0xFF00121C),
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Quick Directives Grid
        Text(
            text = "TACTICAL DIRECTIVES",
            color = JarvisTextSecondary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            DirectivePill(
                title = "Diagnostics",
                subtitle = "${diagnostics.batteryPct}% • ${diagnostics.ramUsedMb}MB",
                onClick = { viewModel.processUserCommand("Check system diagnostics", isVoice = false) },
                modifier = Modifier.weight(1f)
            )
            DirectivePill(
                title = "Protocol Omega",
                subtitle = "Security Shield",
                onClick = { viewModel.processUserCommand("Activate Protocol Omega", isVoice = false) },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            DirectivePill(
                title = "Remind Amar",
                subtitle = "Schedule Protocol",
                onClick = { viewModel.processUserCommand("Set reminder review project architecture", isVoice = false) },
                modifier = Modifier.weight(1f)
            )
            DirectivePill(
                title = "Export Package",
                subtitle = "JARVIS ZIP",
                onClick = { viewModel.processUserCommand("Download ZIP package", isVoice = false) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun DirectivePill(
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(JarvisSurface)
            .border(1.dp, JarvisCardBorder, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 12.dp)
    ) {
        Column {
            Text(
                text = title,
                color = JarvisCyan,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = subtitle,
                color = JarvisTextSecondary,
                fontSize = 11.sp
            )
        }
    }
}
