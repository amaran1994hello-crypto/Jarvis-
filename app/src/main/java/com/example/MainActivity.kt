package com.example

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.FolderZip
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.PhoneInTalk
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.components.ActiveCallDialog
import com.example.screens.CoreHudScreen
import com.example.screens.DiagnosticsToolsScreen
import com.example.screens.NeuralChatScreen
import com.example.screens.TelecomCallsScreen
import com.example.screens.ZipPackageScreen
import com.example.ui.theme.JarvisBackground
import com.example.ui.theme.JarvisCardBorder
import com.example.ui.theme.JarvisCyan
import com.example.ui.theme.JarvisGold
import com.example.ui.theme.JarvisSurface
import com.example.ui.theme.JarvisTextPrimary
import com.example.ui.theme.JarvisTextSecondary
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.JarvisTab
import com.example.viewmodel.JarvisViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: JarvisViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                // Request critical permissions at startup
                RequestRuntimePermissions()

                val currentTab by viewModel.currentTab.collectAsState()
                val activeCall by viewModel.activeSimulatedCall.collectAsState()

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(JarvisBackground)
                ) {
                    Scaffold(
                        modifier = Modifier
                            .fillMaxSize()
                            .statusBarsPadding()
                            .navigationBarsPadding(),
                        containerColor = JarvisBackground,
                        bottomBar = {
                            JarvisBottomNavigation(
                                currentTab = currentTab,
                                onSelectTab = { viewModel.selectTab(it) }
                            )
                        }
                    ) { innerPadding ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) {
                            when (currentTab) {
                                JarvisTab.CORE_HUD -> CoreHudScreen(viewModel = viewModel)
                                JarvisTab.TELECOM_CALLS -> TelecomCallsScreen(viewModel = viewModel)
                                JarvisTab.NEURAL_CHAT -> NeuralChatScreen(viewModel = viewModel)
                                JarvisTab.DIAGNOSTICS_RECORDS -> DiagnosticsToolsScreen(viewModel = viewModel)
                                JarvisTab.ZIP_PACKAGE -> ZipPackageScreen(viewModel = viewModel)
                            }
                        }
                    }

                    // Active Call Dialog overlay
                    activeCall?.let { call ->
                        ActiveCallDialog(
                            callRecord = call,
                            onEndCall = { viewModel.endCurrentCall() }
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun RequestRuntimePermissions() {
        val permissionsToRequest = buildList {
            add(Manifest.permission.RECORD_AUDIO)
            add(Manifest.permission.CALL_PHONE)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                add(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions()
        ) { /* Handled gracefully */ }

        LaunchedEffect(Unit) {
            val ungranted = permissionsToRequest.filter {
                ContextCompat.checkSelfPermission(this@MainActivity, it) != PackageManager.PERMISSION_GRANTED
            }
            if (ungranted.isNotEmpty()) {
                launcher.launch(ungranted.toTypedArray())
            }
        }
    }
}

@Composable
fun JarvisBottomNavigation(
    currentTab: JarvisTab,
    onSelectTab: (JarvisTab) -> Unit
) {
    NavigationBar(
        modifier = Modifier
            .background(JarvisSurface)
            .border(1.dp, JarvisCardBorder),
        containerColor = JarvisSurface,
        tonalElevation = 8.dp
    ) {
        NavigationItem(
            label = "Arc Core",
            icon = Icons.Default.Hub,
            selected = currentTab == JarvisTab.CORE_HUD,
            onClick = { onSelectTab(JarvisTab.CORE_HUD) },
            tag = "nav_arc_core"
        )
        NavigationItem(
            label = "Telecom",
            icon = Icons.Default.PhoneInTalk,
            selected = currentTab == JarvisTab.TELECOM_CALLS,
            onClick = { onSelectTab(JarvisTab.TELECOM_CALLS) },
            tag = "nav_telecom"
        )
        NavigationItem(
            label = "Neural Chat",
            icon = Icons.AutoMirrored.Filled.Chat,
            selected = currentTab == JarvisTab.NEURAL_CHAT,
            onClick = { onSelectTab(JarvisTab.NEURAL_CHAT) },
            tag = "nav_chat"
        )
        NavigationItem(
            label = "Diagnostics",
            icon = Icons.Default.Settings,
            selected = currentTab == JarvisTab.DIAGNOSTICS_RECORDS,
            onClick = { onSelectTab(JarvisTab.DIAGNOSTICS_RECORDS) },
            tag = "nav_diagnostics"
        )
        NavigationItem(
            label = "Package ZIP",
            icon = Icons.Default.FolderZip,
            selected = currentTab == JarvisTab.ZIP_PACKAGE,
            onClick = { onSelectTab(JarvisTab.ZIP_PACKAGE) },
            tag = "nav_zip"
        )
    }
}

@Composable
private fun androidx.compose.foundation.layout.RowScope.NavigationItem(
    label: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
    tag: String
) {
    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(20.dp),
                tint = if (selected) JarvisCyan else JarvisTextSecondary
            )
        },
        label = {
            Text(
                text = label,
                fontSize = 10.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                color = if (selected) JarvisCyan else JarvisTextSecondary
            )
        },
        colors = NavigationBarItemDefaults.colors(
            indicatorColor = JarvisCyan.copy(alpha = 0.2f),
            selectedIconColor = JarvisCyan,
            unselectedIconColor = JarvisTextSecondary
        ),
        modifier = Modifier.testTag(tag)
    )
}
