package com.tatas.ugd89_a_10731_project1

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.Toast
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private var mCamera: Camera? = null
    private var mCameraView: CameraView? = null
    lateinit var proximitySensor: Sensor
    lateinit var sensorManager: SensorManager
    private var camBackId = Camera.CameraInfo.CAMERA_FACING_BACK
    private var camFrontId = Camera.CameraInfo.CAMERA_FACING_FRONT
    private var id_camera: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewCamera(id_camera)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)

        if (proximitySensor==null) {
            Toast.makeText(this, "No proximity sensor found in device", Toast.LENGTH_SHORT).show()
            finish()
        } else {

            sensorManager.registerListener(
                proximitySensorEventListener,
                proximitySensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }

        @SuppressLint("MissingInflatedId", "LocalSuppress") val imageClose =
            findViewById<View>(R.id.imgClose) as ImageButton
        imageClose.setOnClickListener {view: View -> System.exit(0)}
    }

    var proximitySensorEventListener: SensorEventListener? = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

        }

        override fun onSensorChanged(event: SensorEvent) {
            if(event.sensor.type == Sensor.TYPE_PROXIMITY) {
                if(event.values[0] == 0f) {
                    if (id_camera == Camera.CameraInfo.CAMERA_FACING_BACK) {
                        id_camera = Camera.CameraInfo.CAMERA_FACING_FRONT
                    } else {
                        id_camera = Camera.CameraInfo.CAMERA_FACING_BACK

                    }
                    mCamera!!.stopPreview()
                    mCamera!!.release()
                    viewCamera(id_camera)

                }
            }
        }

    }

    private fun viewCamera(id : Int) {

        if (id==Camera.CameraInfo.CAMERA_FACING_BACK){
            try {
                mCamera = Camera.open(id)
            } catch (e: Exception) {
                Log.d("error", "Failed to get Camera" + e.message)
            }
        } else {
            try {
                mCamera = Camera.open(id)
            } catch (e: Exception) {
                Log.d("error", "Failed to get Camera" + e.message)
            }
        }

        if (mCamera != null) {
            mCameraView = CameraView(this, mCamera!!)
            val cameraView = findViewById<View>(R.id.FLCamera) as FrameLayout
            cameraView.addView(mCameraView)
        }
    }
}