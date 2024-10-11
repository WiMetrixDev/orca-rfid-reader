package expo.modules.orcarfidreader

import android.util.Log
import com.naz.serial.port.ModuleManager
import com.payne.connect.port.SerialPortHandle
import com.payne.reader.Reader
import com.payne.reader.base.Consumer
import com.payne.reader.bean.config.AntennaCount
import com.payne.reader.bean.config.Cmd
import com.payne.reader.bean.config.ResultCode
import com.payne.reader.bean.receive.Failure
import com.payne.reader.bean.receive.OutputPower
import com.payne.reader.bean.receive.Success
import com.payne.reader.bean.send.InventoryConfig
import com.payne.reader.bean.send.InventoryParam
import com.payne.reader.process.ReaderImpl
import com.payne.reader.util.ArrayUtils

class ReaderHelper(
    module: OrcaRfidReaderModule,
) {
    companion object {
        private const val TAG = "OrcaRfidReaderModule"
        private const val MAX_POWER = 33
    }

    private val mModule = module
    private val mReader: Reader = ReaderImpl.create(AntennaCount.SINGLE_CHANNEL)
    private var mConnectHandle: SerialPortHandle? = null
    private var currentPower = 33

    private val mOnGetPowerSuccess =
        Consumer<OutputPower> { outputPower ->
            currentPower = outputPower.outputPower[0].toInt()
            Log.i(TAG, "Success in Reader's Get Power Consumer with Power: $currentPower")
        }

    private val mOnGetPowerFailure =
        Consumer<Failure> { failure ->
            Log.w(TAG, "Failure to get power: ${failure.errorCode}, but trying again")
            mReader.getOutputPower(
                mOnGetPowerSuccess,
            ) { innerFailure ->
                Log.e(TAG, "Failed to get power again: ${innerFailure.errorCode}")
            }
        }

    private val mOnSetPowerSuccess =
        Consumer<Success> {
            Log.i(TAG, "Success in Reader's Set Power Consumer")
        }

    private val mOnSetPowerFailure =
        Consumer<Failure> { failure ->
            Log.w(TAG, "Failure to set power: ${failure.errorCode}, but trying again")
            mReader.setOutputPowerUniformly(
                currentPower.toByte(),
                true,
                mOnSetPowerSuccess,
            ) { innerFailure ->
                Log.e(TAG, "Failed to set power again: ${innerFailure.errorCode}")
            }
        }

    fun startReader(
        serialPort: String,
        baudRate: Int,
    ): Boolean {
        try {
            if (mConnectHandle != null) {
                mConnectHandle!!.release()
            }
            val moduleManager = ModuleManager.newInstance()
            moduleManager.setUHFStatus(true)
            mConnectHandle = SerialPortHandle(serialPort, baudRate)
            mReader.setCmdTimeout(6000L)
            mReader.setOriginalDataCallback(
                { bytes ->
                    val str = ArrayUtils.bytesToHexString(bytes, 0, bytes.size)
                    Log.d(TAG, "SENDING DATA: $str")
                },
                { bytes ->
                    val str = ArrayUtils.bytesToHexString(bytes, 0, bytes.size)
                    Log.d(TAG, "RECEIVING DATA: $str")
                },
            )
            val isConnected = mReader.connect(mConnectHandle)
            Log.i(TAG, "Reader Connected: $isConnected")

            val mInventoryParam = InventoryParam()
            val config =
                InventoryConfig
                    .Builder()
                    .setInventoryParam(mInventoryParam)
                    .setInventory(mInventoryParam.inventory)
                    .setOnInventoryTagSuccess { inventoryTag ->
                        Log.i(TAG, "Inventory Tag Success")
                        val epc = inventoryTag.epc
                        val rssi = inventoryTag.rssi
                        Log.i(TAG, "Reading EPC: $epc, RSSI: $rssi")
                        mModule.sendEvent(
                            "onRFIDRead",
                            mapOf("epc" to epc.replace(" ", ""), "rssi" to rssi),
                        )
                    }.setOnFailure { failure ->
                        val cmdStr = Cmd.getNameForCmd(failure.cmd)
                        val resultCodeStr = ResultCode.getNameForResultCode(failure.errorCode)
                        Log.w(TAG, "Inventory Tag Failure: Ant($failure.antId) $cmdStr -> $resultCodeStr")
                    }.setFastInventory(true)
                    .build()

            mReader.setInventoryConfig(config)
            mReader.startInventory(true)

            // Call the getPower method to ensure the currentPower is set correctly
            Log.i(TAG, "Current Power: ${getPower()}")

            return isConnected
        } catch (e: Exception) {
            Log.e(TAG, "Error Starting UHF Reader: $e")
            e.printStackTrace()
            return false
        }
    }

    fun getPower(): Int {
        mReader.getOutputPower(
            mOnGetPowerSuccess,
            mOnGetPowerFailure,
        )

        // Return the power in 0-100 range
        return currentPower / MAX_POWER * 100
    }

    fun setPower(power: Int) {
        // Transform the power from 0-100 to 0-33 and convert it to byte
        val normalizedPower = (power / 100 * MAX_POWER).toByte()
        mReader.setOutputPowerUniformly(
            normalizedPower,
            true,
            mOnSetPowerSuccess,
            mOnSetPowerFailure,
        )
        currentPower = normalizedPower.toInt()
    }

    fun stopReader() {
        mReader.stopInventory(true)
        mReader.disconnect()
        if (mConnectHandle != null) {
            mConnectHandle!!.release()
            mConnectHandle = null
        }
    }
}
