package com.aziaka.donavan.cameratest

import android.content.ContentValues
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.hardware.Camera
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.os.Build




class MainActivity : AppCompatActivity() {

    private var mCameraFront: Camera? = null
    private var mCameraBack: Camera? = null
    private var mPreviewBack: CameraClass? = null
    private var mPreviewFront: CameraClass? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // Check permission

        val MyVersion = Build.VERSION.SDK_INT
        if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (!checkIfAlreadyhavePermission()) {
                requestForSpecificPermission()
            }
        }

        // Create an instance of Camera frontcamera

        mCameraFront = openFrontFacingCameraGingerbread()
        val previewFront = findViewById(R.id.camerafront_preview) as FrameLayout
        mPreviewFront = CameraClass(this, mCameraFront)
        previewFront.addView(mPreviewFront)

        // Create an instance of Camera backcamera

        mCameraBack = openBackFacingCameraGingerbread()
        mPreviewBack = CameraClass(this, mCameraBack)
        val previewBack = findViewById(R.id.cameraback_preview) as FrameLayout
        previewBack.addView(mPreviewBack)

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            101 -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //granted
            } else {
                //not granted
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun requestForSpecificPermission() {
        ActivityCompat.requestPermissions(this, arrayOf<String>(android.Manifest.permission.CAMERA, android.Manifest.permission.RECORD_AUDIO), 101)
    }

    private fun checkIfAlreadyhavePermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.GET_ACCOUNTS)
        if (result == PackageManager.PERMISSION_GRANTED)
            return true
        return false
    }

    private fun openFrontFacingCameraGingerbread(): Camera? {
        var cameraCount = 0
        var cam: Camera? = null
        val cameraInfo = Camera.CameraInfo()
        cameraCount = Camera.getNumberOfCameras()
        for (camIdx in 0..cameraCount - 1) {
            Camera.getCameraInfo(camIdx, cameraInfo)
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                try {
                    cam = Camera.open(1)
                } catch (e: RuntimeException) {
                    Log.e(ContentValues.TAG, "Camera failed to open 2: " + e.localizedMessage)
                }

            }
        }
        return cam
    }

    private fun openBackFacingCameraGingerbread(): Camera? {
        var cameraCount = 0
        var cam: Camera? = null
        val cameraInfo = Camera.CameraInfo()
        cameraCount = Camera.getNumberOfCameras()
        for (camIdx in 0..cameraCount - 1) {
            Camera.getCameraInfo(camIdx, cameraInfo)
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                try {
                    cam = Camera.open(0)
                } catch (e: RuntimeException) {
                    Log.e(ContentValues.TAG, "Camera failed to open 1 : " + e.localizedMessage)
                }

            }
        }
        return cam
    }
}
