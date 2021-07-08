package ru.eisaev.supersuinstaller

import android.net.LocalSocket
import android.net.LocalSocketAddress
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import java.io.*


class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "SuperSUInstaller"
    }

    private lateinit var cmdClient: CmdClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        res2sdcard(R.raw.su, "su")
        res2sdcard(R.raw.supolicy, "supolicy")
        res2sdcard(R.raw.libsupol, "libsupol.so")
        res2sdcard(R.raw.supersuapk, "SuperSU-v2.82-SR5.apk")

        init()
        execCmdNoOutput("mount -o remount,rw /dev/block/platform/soc/by-name/system /system")
        Thread.sleep(1_000)
        execCmdNoOutput("cp /sdcard/su /system/xbin/")
        Thread.sleep(1_000)
        execCmdNoOutput("chmod 755 /system/xbin/su")
        Thread.sleep(1_000)
        execCmdNoOutput("cp /sdcard/supolicy /system/xbin/")
        Thread.sleep(1_000)
        execCmdNoOutput("chmod 755 /system/xbin/supolicy")
        Thread.sleep(1_000)
        execCmdNoOutput("cp /sdcard/libsupol.so /system/lib/")
        Thread.sleep(1_000)
        execCmdNoOutput("chmod 644 /system/lib/libsupol.so")
        Thread.sleep(1_000)
        execCmdNoOutput("/system/xbin/su -ad")
    }

    override fun onDestroy() {
        deinit()

        super.onDestroy()
    }

    private fun init() {
        cmdClient = CmdClient()
        cmdClient.connect()
    }

    private fun deinit() {
        cmdClient.close()
    }

    private fun execCmdNoOutput(cmd: String?) {
        cmdClient.send(cmd)
    }

    private fun execCmd(cmd: String?): String? {
        cmdClient.send(cmd)
        return cmdClient.recv()
    }

    internal class CmdClient {
        companion object {
            private const val TAG = "SuperSUInstaller"
            private const val name = "cmd_skt"
        }

        private var client: LocalSocket? = null
        private var ostream: PrintWriter? = null
        private var istream: BufferedReader? = null

        fun connect() {
            try {
                client = LocalSocket()
                client!!.connect(LocalSocketAddress(name))
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        fun send(cmd: String?) {
            Log.d(TAG, "send: $cmd")
            try {
                ostream = PrintWriter(client!!.outputStream)
                ostream!!.println(cmd)
                ostream!!.flush()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        fun recv(): String? {
            var result: String? = null
            try {
                istream = BufferedReader(InputStreamReader(client!!.inputStream))
                result = istream!!.readLine()
                Log.d(TAG, "recv: $result")
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                //
            }
            return result
        }

        fun close() {
            try {
                istream!!.close()
                ostream!!.close()
                client!!.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun res2sdcard(resourceId: Int, resourceName: String) {
        val inStream: InputStream = resources.openRawResource(resourceId)
        val outStream = FileOutputStream("/sdcard/$resourceName")
        val buff = ByteArray(1024)
        var read = 0

        try {
            while (inStream.read(buff).also { read = it } > 0) {
                outStream.write(buff, 0, read)
            }
        } finally {
            inStream.close()
            outStream.close()
        }
    }
}
