package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.CallDirection
import com.example.model.ChatMessage
import com.example.model.JarvisReminder
import com.example.model.JarvisSettings
import com.example.model.MessageSender
import com.example.model.SystemDiagnostics
import com.example.model.TelecomCallRecord
import com.example.service.DeviceDiagnosticsService
import com.example.service.ReminderNotificationHelper
import com.example.service.TelecomHelper
import com.example.service.VoiceAssistantEngine
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

enum class JarvisTab {
    CORE_HUD,
    TELECOM_CALLS,
    NEURAL_CHAT,
    DIAGNOSTICS_RECORDS,
    ZIP_PACKAGE
}

class JarvisViewModel(application: Application) : AndroidViewModel(application) {

    val telecomHelper = TelecomHelper(application)
    val voiceEngine = VoiceAssistantEngine(application)
    private val diagnosticsService = DeviceDiagnosticsService(application)
    private val reminderHelper = ReminderNotificationHelper(application)

    private val _currentTab = MutableStateFlow(JarvisTab.CORE_HUD)
    val currentTab: StateFlow<JarvisTab> = _currentTab.asStateFlow()

    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _reminders = MutableStateFlow<List<JarvisReminder>>(emptyList())
    val reminders: StateFlow<List<JarvisReminder>> = _reminders.asStateFlow()

    private val _callRecords = MutableStateFlow<List<TelecomCallRecord>>(emptyList())
    val callRecords: StateFlow<List<TelecomCallRecord>> = _callRecords.asStateFlow()

    private val _diagnostics = MutableStateFlow(diagnosticsService.getDiagnostics())
    val diagnostics: StateFlow<SystemDiagnostics> = _diagnostics.asStateFlow()

    private val _settings = MutableStateFlow(JarvisSettings())
    val settings: StateFlow<JarvisSettings> = _settings.asStateFlow()

    private val _liveInputText = MutableStateFlow("")
    val liveInputText: StateFlow<String> = _liveInputText.asStateFlow()

    private val _isProcessing = MutableStateFlow(false)
    val isProcessing: StateFlow<Boolean> = _isProcessing.asStateFlow()

    private val _activeSimulatedCall = MutableStateFlow<TelecomCallRecord?>(null)
    val activeSimulatedCall: StateFlow<TelecomCallRecord?> = _activeSimulatedCall.asStateFlow()

    init {
        // Initial chat messages
        _chatMessages.value = listOf(
            ChatMessage(
                id = UUID.randomUUID().toString(),
                text = "Good day, Mr. Amar. JARVIS Professional AI is online and operational. Telecom Baseline & Video Calling capabilities are registered. How may I serve you today?",
                sender = MessageSender.JARVIS,
                timestamp = System.currentTimeMillis() - 60000
            )
        )

        _reminders.value = reminderHelper.createInitialReminders()

        _callRecords.value = listOf(
            TelecomCallRecord(
                id = UUID.randomUUID().toString(),
                name = "Amar (Founder)",
                number = "+1 (555) 789-0199",
                direction = CallDirection.OUTGOING,
                timestampFormatted = "10:14 AM",
                durationFormatted = "04:12",
                isVideo = true
            ),
            TelecomCallRecord(
                id = UUID.randomUUID().toString(),
                name = "Google Cloud Telecom Bridge",
                number = "+1 (800) 555-0144",
                direction = CallDirection.INCOMING,
                timestampFormatted = "09:30 AM",
                durationFormatted = "01:45",
                isVideo = false
            )
        )

        // Connect voice engine recognition to command processing
        voiceEngine.onVoiceResultCallback = { spokenText ->
            processUserCommand(spokenText, isVoice = true)
        }

        // Periodic diagnostics refresh
        viewModelScope.launch {
            while (true) {
                delay(3000)
                _diagnostics.value = diagnosticsService.getDiagnostics()
            }
        }
    }

    fun selectTab(tab: JarvisTab) {
        _currentTab.value = tab
    }

    fun updateLiveInput(text: String) {
        _liveInputText.value = text
    }

    fun toggleVoiceListening() {
        if (voiceEngine.isListening.value) {
            voiceEngine.stopListening()
        } else {
            voiceEngine.startListening()
        }
    }

    fun submitTextCommand() {
        val query = _liveInputText.value.trim()
        if (query.isNotEmpty()) {
            _liveInputText.value = ""
            processUserCommand(query, isVoice = false)
        }
    }

    fun processUserCommand(rawCommand: String, isVoice: Boolean) {
        val userMsg = ChatMessage(
            id = UUID.randomUUID().toString(),
            text = rawCommand,
            sender = MessageSender.FOUNDER,
            isVoice = isVoice
        )
        _chatMessages.value = _chatMessages.value + userMsg
        _isProcessing.value = true

        val lower = rawCommand.lowercase().trim()

        viewModelScope.launch {
            delay(400) // Realistic neural processing interval
            val reply: String
            var action: String? = null

            when {
                lower.contains("call amar") || lower.contains("call founder") || lower.contains("call me") -> {
                    reply = "Initiating direct telecom uplink to Founder Amar at ${_settings.value.founderPhoneNumber}."
                    action = "CALL_AMAR"
                    triggerCallToAmar(isVideo = lower.contains("video"))
                }
                lower.startsWith("call") -> {
                    val target = rawCommand.substringAfter("call").trim()
                    reply = "Dialing $target through telecom protocol."
                    action = "CALL_EXTERNAL"
                    initiateCustomCall(target, "Contact", isVideo = lower.contains("video"))
                }
                lower.contains("system") || lower.contains("diagnostic") || lower.contains("status") -> {
                    val d = _diagnostics.value
                    reply = "System Diagnostic Report: Core status is ${d.coreStatus}. Battery at ${d.batteryPct}% (${if (d.isCharging) "Charging" else "Discharging"}). Memory utilization: ${d.ramUsedMb}MB of ${d.ramTotalMb}MB. Network: ${d.networkType}."
                    action = "SHOW_DIAGNOSTICS"
                }
                lower.contains("who are you") || lower.contains("who is amar") -> {
                    reply = "I am J.A.R.V.I.S. (Just A Rather Very Intelligent System) Professional AI. My creator and supreme commander is Founder Amar. I am configured with voice synthesis, telephony integration, and system automation protocols."
                }
                lower.contains("telecom") || lower.contains("callsmanager") || lower.contains("capability") -> {
                    reply = "CallsManager subsystem is fully registered with CAPABILITY_BASELINE and CAPABILITY_SUPPORTS_VIDEO_CALLING. Secure telecom device API key authentication is enabled."
                    action = "SHOW_TELECOM"
                }
                lower.contains("reminder") || lower.contains("remind me") -> {
                    val reminderText = rawCommand.replace("set reminder", "").replace("remind me to", "").trim()
                    val cleanText = if (reminderText.isEmpty()) "Review project with Founder Amar" else reminderText
                    addReminder(cleanText, "Today at ${SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())}")
                    reply = "Reminder logged and scheduled: \"$cleanText\". I have dispatched an alert to your notification tray."
                    action = "SHOW_REMINDER"
                }
                lower.contains("protocol omega") || lower.contains("emergency") || lower.contains("security") -> {
                    reply = "Protocol Omega acknowledged, Mr. Amar. All auxiliary nodes isolated. High-level security shield active. Telecom emergency channels reserved."
                    reminderHelper.dispatchNotification("Security Protocol", "Protocol Omega activated by Founder Amar.")
                }
                lower.contains("download") || lower.contains("zip") || lower.contains("package") -> {
                    reply = "The complete JARVIS PROFESSIONAL AI project structure and downloadable ZIP package have been compiled. Navigate to the ZIP Package tab to review architecture and deployment files."
                    _currentTab.value = JarvisTab.ZIP_PACKAGE
                }
                else -> {
                    reply = "Command analyzed, Mr. Amar: \"$rawCommand\". Neural routines executed successfully. All parameters within optimal thresholds."
                }
            }

            val jarvisMsg = ChatMessage(
                id = UUID.randomUUID().toString(),
                text = reply,
                sender = MessageSender.JARVIS,
                actionSuggestion = action
            )
            _chatMessages.value = _chatMessages.value + jarvisMsg
            _isProcessing.value = false

            if (_settings.value.isTtsEnabled) {
                voiceEngine.speak(reply)
            }
        }
    }

    fun triggerCallToAmar(isVideo: Boolean = false) {
        val phoneNumber = _settings.value.founderPhoneNumber
        val callRecord = TelecomCallRecord(
            id = UUID.randomUUID().toString(),
            name = "Amar (Founder)",
            number = phoneNumber,
            direction = CallDirection.OUTGOING,
            timestampFormatted = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date()),
            durationFormatted = "Connecting...",
            isVideo = isVideo
        )
        _activeSimulatedCall.value = callRecord
        _callRecords.value = listOf(callRecord) + _callRecords.value
        telecomHelper.triggerDeviceCall(phoneNumber, "Founder Amar", isVideo)
    }

    fun initiateCustomCall(number: String, name: String = "External Contact", isVideo: Boolean = false) {
        val callRecord = TelecomCallRecord(
            id = UUID.randomUUID().toString(),
            name = name,
            number = number,
            direction = CallDirection.OUTGOING,
            timestampFormatted = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date()),
            durationFormatted = "Connecting...",
            isVideo = isVideo
        )
        _activeSimulatedCall.value = callRecord
        _callRecords.value = listOf(callRecord) + _callRecords.value
        telecomHelper.triggerDeviceCall(number, name, isVideo)
    }

    fun endCurrentCall() {
        _activeSimulatedCall.value = null
        telecomHelper.endActiveCall()
        voiceEngine.speak("Call terminated. Telecom channel closed.")
    }

    fun addReminder(title: String, time: String) {
        val item = JarvisReminder(
            id = UUID.randomUUID().toString(),
            title = title,
            timeFormatted = time,
            isCompleted = false,
            priority = "HIGH"
        )
        _reminders.value = listOf(item) + _reminders.value
        reminderHelper.dispatchNotification(title, "Scheduled for $time")
    }

    fun toggleReminder(id: String) {
        _reminders.value = _reminders.value.map {
            if (it.id == id) it.copy(isCompleted = !it.isCompleted) else it
        }
    }

    fun updateSettings(newSettings: JarvisSettings) {
        _settings.value = newSettings
    }

    override fun onCleared() {
        super.onCleared()
        voiceEngine.destroy()
    }
}
