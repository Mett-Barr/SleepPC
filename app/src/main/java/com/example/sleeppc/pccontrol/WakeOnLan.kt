package com.example.sleeppc.pccontrol

import android.content.Context
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.example.sleeppc.tool.bindProcessToWifi
import com.example.sleeppc.tool.vibrate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

//fun ComponentActivity.wakeOnLan(callback: () -> Unit = {}) {
//    lifecycleScope.launch {
//        sendWakeOnLanPacket()
//        vibrate()
//        delay(1000)
//        callback()
//    }
//}



suspend fun Context.wakeOnLan() {
    bindProcessToWifi()
    sendWakeOnLanPacket()

//    delay(500)
//    vibrate()
}

const val ETHERNET_MAC = "04-42-1A-24-F6-53"
const val WIFI_MAC = "A4-6B-B6-3C-BF-0E"

const val SOURCE_PORT = 47556

suspend fun sendWakeOnLanPacket(macAddress: String = ETHERNET_MAC, port: Int = 9) = withContext(
    Dispatchers.IO){
    Log.d("WOL", "Sending Wake-on-LAN packet...")

    val macBytes = macAddressToBytes(macAddress)
    val magicPacket = createMagicPacket(macBytes)
    val broadcastAddress = InetAddress.getByName("255.255.255.255")

    DatagramSocket(SOURCE_PORT).use { socket ->
//    DatagramSocket().use { socket ->
        socket.broadcast = true
        val packet = DatagramPacket(magicPacket, magicPacket.size, broadcastAddress, port)
        socket.send(packet)
    }

    Log.d("WOL", "Wake-on-LAN packet sent successfully.")
}

private fun macAddressToBytes(macAddress: String): ByteArray {
    Log.d("WOL", "Converting MAC address to bytes: $macAddress")

    val bytes = ByteArray(6)
    val hex = macAddress.replace("[:\\-]".toRegex(), "")

    for (i in 0 until 6) {
        val hexSubstring = hex.substring(i * 2, i * 2 + 2)
        bytes[i] = hexSubstring.toInt(16).toByte()
    }

    Log.d("WOL", "Converted MAC address bytes: ${bytes.contentToString()}")

    return bytes
}

private fun createMagicPacket(macBytes: ByteArray): ByteArray {
    Log.d("WOL", "Creating magic packet...")

    val magicPacket = ByteArray(6 + 6 * 16)

    // 6 bytes of 0xFF
    for (i in 0..5) {
        magicPacket[i] = 0xFF.toByte()
    }

    // 16 copies of the target MAC address
    for (i in 6 until magicPacket.size step 6) {
        macBytes.copyInto(magicPacket, i)
    }

    return magicPacket
}
