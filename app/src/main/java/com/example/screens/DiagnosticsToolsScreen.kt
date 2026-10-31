package com.example.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.JarvisReminder
import com.example.ui.theme.JarvisBackground
import com.example.ui.theme.JarvisCardBorder
import com.example.ui.theme.JarvisCyan
import com.example.ui.theme.JarvisGold
import com.example.ui.theme.JarvisGreenOnline
import com.example.ui.theme.JarvisRedAlert
import com.example.ui.theme.JarvisSurface
import com.example.ui.theme.JarvisSurfaceVariant
import com.example.ui.theme.JarvisTextPrimary
import com.example.ui.theme.JarvisTextSecondary
import com.example.viewmodel.JarvisViewModel

@Composable
fun DiagnosticsToolsScreen(viewModel: JarvisViewModel) {
    val diagnostics by viewModel.diagnostics.collectAsState()
    val reminders by viewModel.reminders.collectAsState()
    val settings by viewModel.settings.collectAsState()

    var showAddReminder by remember { mutableStateOf(false) }
    var newReminderText by remember { mutableStateOf("") }
    var newReminderTime by remember { mutableStateOf("17:00") }

    var founderName by remember { mutableStateOf(settings.founderName) }
    var founderPhone by remember { mutableStateOf(settings.founderPhoneNumber) }
    var telecomApiKey by remember { mutableStateOf(settings.telecomApiKey) }
    var geminiApiKey by remember { mutableStateOf(settings.geminiApiKey) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(JarvisBackground)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Telemetry Grid
        item {
            Text(
                text = "REAL-TIME HARDWARE TELEMETRY",
                color = JarvisCyan,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TelemetryCard(
                    icon = Icons.Default.BatteryChargingFull,
                    title = "Battery Core",
                    value = "${diagnostics.batteryPct}%",
                    subtitle = if (diagnostics.isCharging) "Charging (AC)" else "Discharging",
                    color = if (diagnostics.batteryPct > 20) JarvisGreenOnline else JarvisRedAlert,
                    modifier = Modifier.weight(1f)
                )

                TelemetryCard(
                    icon = Icons.Default.Memory,
                    title = "RAM Matrix",
                    value = "${diagnostics.ramUsedMb} MB",
                    subtitle = "of ${diagnostics.ramTotalMb} MB Total",
                    color = JarvisCyan,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TelemetryCard(
                    icon = Icons.Default.NetworkCheck,
                    title = "Uplink Link",
                    value = diagnostics.networkType,
                    subtitle = if (diagnostics.isNetworkConnected) "Quantum Active" else "Disconnected",
                    color = JarvisGold,
                    modifier = Modifier.weight(1f)
                )

                TelemetryCard(
                    icon = Icons.Default.Timer,
                    title = "System Uptime",
                    value = diagnostics.uptimeFormatted,
                    subtitle = "Host: Android OS",
                    color = JarvisCyan,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Reminders & Task Protocol Section
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "TASK REMINDERS & PROTOCOLS",
                    color = JarvisCyan,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )

                Button(
                    onClick = { showAddReminder = !showAddReminder },
                    modifier = Modifier.testTag("add_reminder_toggle"),
                    colors = ButtonDefaults.buttonColors(containerColor = JarvisSurfaceVariant, contentColor = JarvisCyan),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (showAddReminder) "Close" else "Add New", fontSize = 11.sp)
                }
            }
        }

        if (showAddReminder) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(JarvisSurface)
                        .border(1.dp, JarvisCyan.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                        .padding(14.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = newReminderText,
                            onValueChange = { newReminderText = it },
                            placeholder = { Text("Reminder description...", color = JarvisTextSecondary) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = JarvisTextPrimary,
                                unfocusedTextColor = JarvisTextPrimary
                            )
                        )

                        OutlinedTextField(
                            value = newReminderTime,
                            onValueChange = { newReminderTime = it },
                            placeholder = { Text("Time (e.g. Today at 18:00)", color = JarvisTextSecondary) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = JarvisTextPrimary,
                                unfocusedTextColor = JarvisTextPrimary
                            )
                        )

                        Button(
                            onClick = {
                                if (newReminderText.isNotBlank()) {
                                    viewModel.addReminder(newReminderText, newReminderTime)
                                    newReminderText = ""
                                    showAddReminder = false
                                }
                            },
                            modifier = Modifier.fillMaxWidth().testTag("save_reminder_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = JarvisCyan, contentColor = Color.Black)
                        ) {
                            Text("Dispatch & Schedule Alert", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        items(reminders) { item ->
            ReminderItemRow(item = item, onToggle = { viewModel.toggleReminder(item.id) })
        }

        // Credentials & Settings Configuration
        item {
            Text(
                text = "IDENTITY & CREDENTIAL SUBSYSTEM",
                color = JarvisCyan,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }

        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(JarvisSurface)
                    .border(1.dp, JarvisCardBorder, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "FOUNDER PROFILE & TELECOM AUTHENTICATION",
                        color = JarvisGold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        value = founderName,
                        onValueChange = { founderName = it },
                        label = { Text("Founder Name", color = JarvisTextSecondary) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = JarvisTextPrimary, unfocusedTextColor = JarvisTextPrimary)
                    )

                    OutlinedTextField(
                        value = founderPhone,
                        onValueChange = { founderPhone = it },
                        label = { Text("Founder Device Phone Number", color = JarvisTextSecondary) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = JarvisTextPrimary, unfocusedTextColor = JarvisTextPrimary)
                    )

                    OutlinedTextField(
                        value = telecomApiKey,
                        onValueChange = { telecomApiKey = it },
                        label = { Text("Telecom Device API Key", color = JarvisTextSecondary) },
                        placeholder = { Text("API Key to call by own device", color = JarvisTextSecondary) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = JarvisTextPrimary, unfocusedTextColor = JarvisTextPrimary)
                    )

                    OutlinedTextField(
                        value = geminiApiKey,
                        onValueChange = { geminiApiKey = it },
                        label = { Text("Gemini AI API Key", color = JarvisTextSecondary) },
                        placeholder = { Text("Configured via Secrets Panel", color = JarvisTextSecondary) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = JarvisTextPrimary, unfocusedTextColor = JarvisTextPrimary)
                    )

                    Button(
                        onClick = {
                            viewModel.updateSettings(
                                settings.copy(
                                    founderName = founderName,
                                    founderPhoneNumber = founderPhone,
                                    telecomApiKey = telecomApiKey,
                                    geminiApiKey = geminiApiKey
                                )
                            )
                            viewModel.voiceEngine.speak("Credentials and founder profile synchronized, Mr. $founderName.")
                        },
                        modifier = Modifier.fillMaxWidth().testTag("save_settings_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = JarvisGold, contentColor = Color.Black),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Save Configurations", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun TelemetryCard(
    icon: ImageVector,
    title: String,
    value: String,
    subtitle: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(JarvisSurface)
            .border(1.dp, JarvisCardBorder, RoundedCornerShape(14.dp))
            .padding(14.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = title, color = JarvisTextSecondary, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = value, color = JarvisTextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = subtitle, color = color, fontSize = 10.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
private fun ReminderItemRow(
    item: JarvisReminder,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(JarvisSurface)
            .border(1.dp, JarvisCardBorder, RoundedCornerShape(12.dp))
            .clickable(onClick = onToggle)
            .padding(14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = if (item.isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                contentDescription = null,
                tint = if (item.isCompleted) JarvisGreenOnline else JarvisTextSecondary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = item.title,
                    color = if (item.isCompleted) JarvisTextSecondary else JarvisTextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = item.timeFormatted,
                    color = JarvisCyan,
                    fontSize = 11.sp
                )
            }
        }

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(if (item.priority == "CRITICAL") JarvisRedAlert.copy(alpha = 0.2f) else JarvisGold.copy(alpha = 0.2f))
                .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
            Text(
                text = item.priority,
                color = if (item.priority == "CRITICAL") JarvisRedAlert else JarvisGold,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
