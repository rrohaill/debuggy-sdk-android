package dev.rohail.debuggy.interceptor

import android.content.Context
import android.content.Intent
import android.hardware.SensorManager
import com.squareup.seismic.ShakeDetector
import dev.rohail.debuggy.ui.LogsActivity
import java.lang.ref.WeakReference

class MyShakeDetector : ShakeDetector.Listener {

    private val shakeDetector: ShakeDetector = ShakeDetector(this)
    private lateinit var context: WeakReference<Context>

    fun start(context: WeakReference<Context>, sensitivity: Int) {
        this.context = context
        context.get()?.let {
            val sensorManager: SensorManager =
                it.getSystemService(Context.SENSOR_SERVICE) as SensorManager
            shakeDetector.setSensitivity(sensitivity)
            shakeDetector.start(sensorManager)
        }
    }

    fun stop() {
        shakeDetector.stop()
    }

    override fun hearShake() {
        context.get()?.let {
            val myIntent = Intent(it, LogsActivity::class.java)
            myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            it.startActivity(myIntent)
        }
    }
}