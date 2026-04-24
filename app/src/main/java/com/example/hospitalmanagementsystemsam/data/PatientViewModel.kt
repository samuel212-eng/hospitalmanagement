package com.example.hospitalmanagementsystemsam.data

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.hospitalmanagementsystemsam.models.PatientModel
import com.example.hospitalmanagementsystemsam.navigation.ROUTE_VIEW_PATIENT
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.io.InputStream

class PatientViewModel : ViewModel() {
    private val cloudinaryUrl = "https://api.cloudinary.com/v1_1/ddh9tu1af/image/upload"
    private val uploadPreset = "image_folder"
    private val _patients = mutableStateListOf<PatientModel>()
    val patients: List<PatientModel> = _patients

    fun uploadPatient(
        patientId: String,
        imageUri: Uri?,
        name: String,
        age: String,
        phone: String,
        illness: String,
        gender: String,
        date_of_visit: String,
        context: Context,
        navController: NavController
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val imageUrl = imageUri?.let { uploadToCloudinary(context, it) }
                val patientData = mapOf(
                    "id" to patientId,
                    "name" to name,
                    "age" to age,
                    "phone" to phone,
                    "illness" to illness,
                    "imageUrl" to imageUrl,
                    "gender" to gender,
                    "date_of_visit" to date_of_visit
                )
                val ref = FirebaseDatabase.getInstance()
                    .getReference("Patients").child(patientId)
                ref.setValue(patientData).await()
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Patient saved Successfully", Toast.LENGTH_LONG).show()
                    navController.navigate(ROUTE_VIEW_PATIENT)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Patient not saved: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun uploadToCloudinary(context: Context, uri: Uri): String {
        val contentResolver = context.contentResolver
        val inputStream: InputStream? = contentResolver.openInputStream(uri)
        val fileBytes = inputStream?.readBytes() ?: throw Exception("Image read failed")
        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart(
                "file", "image.jpg",
                RequestBody.create("image/*".toMediaTypeOrNull(), fileBytes)
            )
            .addFormDataPart("upload_preset", uploadPreset).build()
        val request = Request.Builder().url(cloudinaryUrl).post(requestBody).build()
        val response = OkHttpClient().newCall(request).execute()
        if (!response.isSuccessful) throw Exception("Upload failed: ${response.message}")
        val responseBody = response.body?.string()
        val secureUrl = Regex("\"secure_url\":\"(.*?)\"")
            .find(responseBody ?: "")?.groupValues?.get(1)
        return secureUrl ?: throw Exception("Failed to get image URL")
    }

    fun fetchPatient(context: Context) {
        val ref = FirebaseDatabase.getInstance().getReference("Patients")
        ref.get()
            .addOnSuccessListener { snapshot ->
                _patients.clear()
                if (snapshot.exists()) {
                    for (child in snapshot.children) {
                        try {
                            val value = child.value
                            if (value is Map<*, *>) {
                                val patient = child.getValue(PatientModel::class.java)
                                patient?.let {
                                    it.id = child.key
                                    _patients.add(it)
                                }
                            } else {
                                Log.e("PatientViewModel", "Skipping invalid patient data at ${child.key}: $value")
                            }
                        } catch (e: Exception) {
                            Log.e("PatientViewModel", "Error parsing patient at ${child.key}", e)
                        }
                    }
                }
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to load patients: ${it.message}", Toast.LENGTH_LONG).show()
            }
    }

    fun updatePatient(
        patientId: String,
        imageUri: Uri?,
        name: String,
        age: String,
        illness: String,
        phone: String,
        gender: String,
        date_of_visit: String,
        imageUrl: String?,
        context: Context,
        navController: NavController
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val newImageUrl = imageUri?.let { uploadToCloudinary(context, it) } ?: imageUrl
                val updateData = mutableMapOf<String, Any?>(
                    "id" to patientId,
                    "name" to name,
                    "age" to age,
                    "phone" to phone,
                    "illness" to illness,
                    "gender" to gender,
                    "date_of_visit" to date_of_visit,
                    "imageUrl" to newImageUrl
                )
                val ref = FirebaseDatabase.getInstance().getReference("Patients").child(patientId)
                ref.updateChildren(updateData).await()
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Patient updated successfully", Toast.LENGTH_LONG).show()
                    navController.navigate(ROUTE_VIEW_PATIENT)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Failed to update patient: ${e.message}", Toast.LENGTH_LONG).show()
                }
                Log.e("PatientViewModel", "Update failed", e)
            }
        }
    }

    fun deletePatient(patientId: String, context: Context) {
        val ref = FirebaseDatabase.getInstance().getReference("Patients").child(patientId)
        ref.removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Patient deleted", Toast.LENGTH_SHORT).show()
                fetchPatient(context)
            } else {
                Toast.makeText(context, "Failed to delete patient", Toast.LENGTH_SHORT).show()
            }
        }
    }
}