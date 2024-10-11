package expo.modules.orcarfidreader

import android.media.AudioManager
import android.media.ToneGenerator
import android.util.Log
import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition
import java.io.File

class OrcaRfidReaderModule : Module() {
    companion object {
        private const val TAG = "OrcaRfidReaderModule"
    }

    private val toneGen = ToneGenerator(AudioManager.STREAM_MUSIC, 100)

    val serialPorts = mutableListOf<String>()
    val readerHelper = ReaderHelper(this@OrcaRfidReaderModule)
    var matchEPCs = ""

    fun startReader(
        serialPort: String,
        baudRate: Int,
    ): Boolean {
        Log.i(TAG, "Starting UHF Reader on Serial Port: $serialPort with Baud Rate: $baudRate")
        return readerHelper.startReader(serialPort, baudRate)
    }

    // Get list of available serial ports on the device
    fun listSerialPorts(): List<String> {
        if (serialPorts.isEmpty()) {
            val devDirectory = File("/dev")

            if (devDirectory.exists() && devDirectory.isDirectory) {
                val files = devDirectory.listFiles()

                if (files != null) {
                    for (file in files) {
                        if (file.name.startsWith("tty")) {
                            serialPorts.add(file.absolutePath)
                        }
                    }
                }
            } else {
                Log.e(TAG, "Error Listing Serial Ports! /dev Directory does not exist!")
            }
        }

        return serialPorts
    }

    fun getReaderPower(): Int {
        Log.i(TAG, "Getting UHF Reader Power")
        return 100
    }

    fun setReaderPower(power: Int) {
        Log.i(TAG, "Setting UHF Reader Power to $power")
    }

    // Pre-defined baud rates
    fun listBaudRates() = listOf(9600, 19200, 38400, 57600, 115200)

    fun shouldPlayBeep(epc: String): Boolean = matchEPCs.isEmpty() || matchEPCs.contains(epc)

    fun playBeep() {
        toneGen.startTone(ToneGenerator.TONE_PROP_BEEP, 150)
    }

    fun stopReader() {
        Log.i(TAG, "Stopping UHF Reader")
        readerHelper.stopReader()
    }

    override fun definition() =
        ModuleDefinition {
            Name("OrcaRFIDReader")

            Events("onRFIDRead")

            Function("startReader") { serialPort: String, baudRate: Int ->
                startReader(serialPort, baudRate)
            }

            Function("getReaderPower") {
                getReaderPower()
            }

            Function("setReaderPower") { power: Int ->
                setReaderPower(power)
            }

            Function("listSerialPorts") {
                listSerialPorts()
            }

            Function("listBaudRates") {
                listBaudRates()
            }

            Function("setMatchEPCs") { newMatchEPCs: String ->
                matchEPCs = newMatchEPCs
            }

            Function("resetMatchEPCs") {
                matchEPCs = ""
            }

            Function("stopReader") {
                stopReader()
            }
        }
}
