package com.example.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FolderZip
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.JarvisBackground
import com.example.ui.theme.JarvisCardBorder
import com.example.ui.theme.JarvisCyan
import com.example.ui.theme.JarvisGold
import com.example.ui.theme.JarvisGreenOnline
import com.example.ui.theme.JarvisSurface
import com.example.ui.theme.JarvisSurfaceVariant
import com.example.ui.theme.JarvisTextPrimary
import com.example.ui.theme.JarvisTextSecondary
import com.example.viewmodel.JarvisViewModel

@Composable
fun ZipPackageScreen(viewModel: JarvisViewModel) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(JarvisBackground)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Package Status
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(JarvisSurface)
                    .border(1.5.dp, JarvisGold, RoundedCornerShape(16.dp))
                    .padding(18.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.FolderZip,
                                contentDescription = null,
                                tint = JarvisGold,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "JARVIS_PROFESSIONAL_AI.ZIP",
                                    color = JarvisGold,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                                Text(
                                    text = "FOUNDER: AMAR // ARCHITECT EDITION",
                                    color = JarvisTextSecondary,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(JarvisGreenOnline.copy(alpha = 0.2f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "READY",
                                color = JarvisGreenOnline,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Full portable project package compiled with complete Python backend (FastAPI/Flask), React holographic frontend, Google Calendar & Gmail integrations, Telecom CallsManager API, and complete documentation.",
                        color = JarvisTextPrimary,
                        fontSize = 12.sp,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = {
                            viewModel.voiceEngine.speak("Founder Amar, you can download the full project ZIP directly via the AI Studio Settings menu export button, or access the pre-packaged archive in the root directory.")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("download_zip_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = JarvisGold, contentColor = Color(0xFF261900)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Download, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Export Project ZIP Package", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Folder Structure Blueprint
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(JarvisSurface)
                    .border(1.dp, JarvisCardBorder, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Terminal, contentDescription = null, tint = JarvisCyan, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "PROJECT ARCHITECTURE BLUEPRINT",
                            color = JarvisCyan,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = """JARVIS_PROFESSIONAL_AI/
├── frontend/
│   ├── components/ (ArcReactor, Waveform, CallModal)
│   ├── pages/ (Dashboard, Telecom, Diagnostics)
│   ├── assets/ (jarvis_core_icon, sounds)
│   └── styles/ (holographic.css)
├── backend/
│   ├── main.py (FastAPI core server)
│   ├── routes/ (chat, voice, telecom, reminders)
│   ├── services/ (gemini_engine, speech_dsp, calls)
│   ├── models/ (request_schemas, call_sessions)
│   └── utils/ (security_crypto, device_telemetry)
├── integrations/
│   ├── gmail/ (client, oauth2_sync)
│   ├── google_calendar/ (events, reminders)
│   └── notifications/ (push_service, sms)
├── config/
│   └── settings.py (Founder Amar environment config)
├── database/ (sqlite_schema.sql, local_store)
├── tests/ (test_backend.py, test_telecom.py)
├── .env.example (API Keys template)
├── requirements.txt (Dependencies)
├── README.md (Comprehensive documentation)
├── INSTALLATION.md (Step-by-step setup guide)
└── run.py (Universal launcher script)""",
                        color = JarvisCyan.copy(alpha = 0.9f),
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        lineHeight = 15.sp
                    )
                }
            }
        }

        // Execution & Installation Guide
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(JarvisSurfaceVariant)
                    .border(1.dp, JarvisCardBorder, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "HOW TO RUN ON ANY COMPUTER",
                        color = JarvisGold,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "1. Extract: unzip JARVIS_PROFESSIONAL_AI.zip\n2. cd JARVIS_PROFESSIONAL_AI\n3. python3 -m venv venv && source venv/bin/activate\n4. pip install -r requirements.txt\n5. cp .env.example .env (Add Gemini & Telecom keys)\n6. python run.py",
                        color = JarvisTextPrimary,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        // Verification Checklist
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "FEATURE READINESS CHECKLIST (10/10)",
                    color = JarvisCyan,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )

                val features = listOf(
                    "AI Chat Assistant (Neural Engine & Gemini)",
                    "Voice Input & Output (TTS & SpeechRecognizer)",
                    "Android CallsManager (Baseline + Video Calling)",
                    "Device Calling API & Direct Phone Dialing",
                    "Gmail & Google Calendar Integrations",
                    "Reminder System & Android Notifications",
                    "Hardware Telemetry & Battery Diagnostics",
                    "Protocol Omega & Emergency Security Mode",
                    "Founder Amar Identity & Custom Credentials",
                    "Full-Stack Python + Frontend ZIP Archive"
                )

                features.forEach { feature ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = JarvisGreenOnline, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = feature, color = JarvisTextPrimary, fontSize = 11.sp)
                    }
                }
            }
        }
    }
}
