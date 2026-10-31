package com.example

import com.example.model.CallDirection
import com.example.model.ChatMessage
import com.example.model.JarvisReminder
import com.example.model.MessageSender
import com.example.model.SystemDiagnostics
import com.example.model.TelecomCallRecord
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun testJarvisChatMessageCreation() {
        val msg = ChatMessage(
            id = "test-1",
            text = "All systems nominal, Mr. Amar.",
            sender = MessageSender.JARVIS,
            isVoice = true
        )
        assertEquals("test-1", msg.id)
        assertEquals(MessageSender.JARVIS, msg.sender)
        assertTrue(msg.isVoice)
    }

    @Test
    fun testTelecomCallRecord() {
        val record = TelecomCallRecord(
            id = "call-1",
            name = "Amar (Founder)",
            number = "+15557890199",
            direction = CallDirection.OUTGOING,
            timestampFormatted = "10:00 AM",
            durationFormatted = "02:15",
            isVideo = true
        )
        assertEquals("Amar (Founder)", record.name)
        assertTrue(record.isVideo)
        assertEquals(CallDirection.OUTGOING, record.direction)
    }

    @Test
    fun testDiagnosticsData() {
        val diag = SystemDiagnostics(
            batteryPct = 95,
            isCharging = true,
            ramUsedMb = 2048,
            ramTotalMb = 8192,
            networkType = "Quantum Wi-Fi",
            isNetworkConnected = true,
            uptimeFormatted = "04h 20m 00s"
        )
        assertEquals(95, diag.batteryPct)
        assertEquals("OPTIMAL", diag.coreStatus)
    }

    @Test
    fun testReminderModel() {
        val reminder = JarvisReminder(
            id = "rem-1",
            title = "Check Telecom Baseline Capabilities",
            timeFormatted = "Today at 14:00",
            priority = "CRITICAL"
        )
        assertEquals("CRITICAL", reminder.priority)
    }
}
