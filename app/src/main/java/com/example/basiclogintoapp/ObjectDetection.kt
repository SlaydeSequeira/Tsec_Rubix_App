package com.example.basiclogintoapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions
import com.google.firebase.ml.common.modeldownload.FirebaseModelManager
import com.google.firebase.ml.custom.FirebaseCustomRemoteModel
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.objects.FirebaseVisionObject
import com.google.firebase.ml.vision.objects.FirebaseVisionObjectDetector
import com.google.firebase.ml.vision.objects.FirebaseVisionObjectDetectorOptions
import java.lang.StringBuilder

class ObjectDetection : AppCompatActivity() {

    companion object {
        private const val CAMERA_PERMISSION_REQUEST_CODE = 100
        private const val CAMERA_CAPTURE_REQUEST_CODE = 101
    }

    private lateinit var objectDetector: FirebaseVisionObjectDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_object_detection)

        // Check and request CAMERA permission
        checkCameraPermission()
    }

    private fun checkCameraPermission() {
        // Check if the CAMERA permission is already granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Request CAMERA permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )
        } else {
            // Permission already granted, proceed with the camera operation
            openCamera()
        }
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (cameraIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(cameraIntent, CAMERA_CAPTURE_REQUEST_CODE)
        } else {
            Toast.makeText(this, "No camera app found", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // CAMERA permission granted, proceed with the camera operation
                    openCamera()
                } else {
                    // Permission denied, handle accordingly (e.g., show a message)
                    showToast("Camera permission denied. Cannot capture images.")
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_CAPTURE_REQUEST_CODE && resultCode == RESULT_OK) {
            // Handle the captured image, e.g., process it using object detection
            processCapturedImage(data)
        }
    }

    private fun processCapturedImage(data: Intent?) {
        val imageBitmap: Bitmap? = data?.extras?.get("data") as? Bitmap

        // Create a FirebaseVisionImage from the captured Bitmap
        val image = FirebaseVisionImage.fromBitmap(imageBitmap!!)

        // Specify the name you assigned when you deployed the model.
        val remoteModel = FirebaseCustomRemoteModel.Builder("your_model_name").build()

        // Start the model download task.
        val downloadConditions = FirebaseModelDownloadConditions.Builder()
            .requireWifi()
            .build()

        FirebaseModelManager.getInstance().download(remoteModel, downloadConditions)
            .addOnSuccessListener {
                // Model download success, create an object detector from the model file.
                FirebaseModelManager.getInstance().getLatestModelFile(remoteModel)
                    .addOnSuccessListener { modelFile ->
                        val options = FirebaseVisionObjectDetectorOptions.Builder()
                            .setDetectorMode(FirebaseVisionObjectDetectorOptions.SINGLE_IMAGE_MODE)
                            .enableMultipleObjects()
                            .enableClassification()
                            .build()
                        objectDetector =
                            FirebaseVision.getInstance().getOnDeviceObjectDetector(options)

                        // Now that the model is ready, run the object detector.
                        runObjectDetector(image)
                    }
            }
            .addOnFailureListener { e ->
                // Handle any errors
                showToast("Model download failed: ${e.message}")
            }
    }

    // ...
    private fun runObjectDetector(image: FirebaseVisionImage) {
        // Process the image using the object detector.
        objectDetector.processImage(image)
            .addOnSuccessListener { detectedObjects ->
                // Process the detected objects
                Log.d("ObjectDetection", "Detected objects: $detectedObjects")

                // Add the following code to log object information
                if (detectedObjects.isNullOrEmpty()) {
                    Log.d("ObjectDetection", "No objects detected.")
                } else {
                    val detectedObjectsInfo = StringBuilder()
                    for (obj in detectedObjects) {
                        val bounds = obj.boundingBox
                        val entityId = obj.trackingId
                        val label =
                            entityId?.let { getLabelForObjectId(it) } // Replace with correct method

                        detectedObjectsInfo.append("ID: $entityId, Bounds: $bounds, Label: $label\n")
                    }

                    Log.d("ObjectDetection", "Detected Objects:\n$detectedObjectsInfo")
                }
            }
            .addOnFailureListener { e ->
                // Handle any errors
                Log.e("ObjectDetection", "Object detection failed", e)
                showToast("Object detection failed: ${e.message}")
            }
    }

    // Replace this with the correct method to get the label based on the tracking ID
    private fun getLabelForObjectId(entityId: Int): String {
        // Replace this with the correct logic based on your model or backend
        // This is just a placeholder example
        return "Object$entityId"
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
