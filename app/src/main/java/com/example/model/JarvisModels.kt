package com.example.model

enum class MessageSender {
    FOUNDER,
    JARVIS,
    SYSTEM
}

data class ChatMessage(
    val id: String,
    val text: String,
    val sender: MessageSender,
    val timestamp: Long = System.currentTimeMillis(),
    val isVoice: Boolean = false,
    val actionSuggestion: String? = null
)

data class JarvisReminder(
    val id: String,
    val title: String,
    val timeFormatted: String,
    val isCompleted: Boolean = false,
    val priority: String = "HIGH"
)

enum class CallDirection {
    INCOMING,
    OUTGOING,
    MISSED
}

data class TelecomCallRecord(
    val id: String,
    val name: String,
    val number: String,
    val direction: CallDirection,
    val timestampFormatted: String,
    val durationFormatted: String,
    val isVideo: Boolean = false
)

data class SystemDiagnostics(
    val batteryPct: Int,
    val isCharging: Boolean,
    val ramUsedMb: Long,
    val ramTotalMb: Long,
    val networkType: String,
    val isNetworkConnected: Boolean,
    val uptimeFormatted: String,
    val coreStatus: String = "OPTIMAL"
)

data class JarvisSettings(
    val founderName: String = "Amar",
    val telecomApiKey: String = "",
    val geminiApiKey: String = "",
    val founderPhoneNumber: String = "+1 (555) 789-0199",
    val isTtsEnabled: Boolean = true,
    val speechPitch: Float = 0.95f,
    val speechSpeed: Float = 1.05f
)
