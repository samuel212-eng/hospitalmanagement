package com.example.hospitalmanagementsystemsam.ui.theme.screens.patient

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.hospitalmanagementsystemsam.data.PatientViewModel
import com.example.hospitalmanagementsystemsam.models.PatientModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdatePatientScreen(navController: NavController, id: String) {
    val context = LocalContext.current
    val patientViewModel: PatientViewModel = viewModel()
    
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var illness by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var date_of_visit by remember { mutableStateOf("") }
    val imageUri = remember { mutableStateOf<Uri?>(null) }
    var patient by remember { mutableStateOf<PatientModel?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri.value = uri
    }

    val currentDataRef = FirebaseDatabase.getInstance().getReference().child("Patients/$id")

    DisposableEffect(id) {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    if (snapshot.exists() && snapshot.value is Map<*, *>) {
                        val data = snapshot.getValue(PatientModel::class.java)
                        data?.let {
                            patient = it
                            name = it.name ?: ""
                            age = it.age ?: ""
                            phone = it.phone ?: ""
                            illness = it.illness ?: ""
                            gender = it.gender ?: ""
                            date_of_visit = it.date_of_visit ?: ""
                        }
                    } else {
                        Log.e("UpdatePatient", "Invalid data format or missing data for ID: $id")
                    }
                } catch (e: Exception) {
                    Log.e("UpdatePatient", "Error parsing patient data", e)
                } finally {
                    isLoading = false
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
                isLoading = false
            }
        }
        currentDataRef.addValueEventListener(listener)
        onDispose { currentDataRef.removeEventListener(listener) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Update Patient Details", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF880E4F))
            )
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Update Patient",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF880E4F)
                    )

                    Spacer(modifier = Modifier.height(16.dp))


                    Card(
                        shape = CircleShape,
                        elevation = CardDefaults.cardElevation(6.dp),
                        modifier = Modifier
                            .size(140.dp)
                            .clickable { launcher.launch("image/*") }
                            .shadow(8.dp, CircleShape)
                    ) {
                        AnimatedContent(
                            targetState = imageUri.value,
                            label = "Image Picker Animation"
                        ) { targetUri ->
                            AsyncImage(
                                model = targetUri ?: patient?.imageUrl,
                                contentDescription = "Patient Image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }

                    Text(
                        text = "Tap to change picture",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 20.dp),
                        color = Color.LightGray,
                        thickness = 1.dp
                    )

                    val fieldModifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)

                    val fieldShape = RoundedCornerShape(14.dp)

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Full Name") },
                        modifier = fieldModifier,
                        shape = fieldShape,
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) }
                    )

                    OutlinedTextField(
                        value = age,
                        onValueChange = { age = it },
                        label = { Text("Age") },
                        modifier = fieldModifier,
                        shape = fieldShape,
                        leadingIcon = { Icon(Icons.Default.DateRange, contentDescription = null) }
                    )

                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Phone Number") },
                        modifier = fieldModifier,
                        shape = fieldShape,
                        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) }
                    )

                    OutlinedTextField(
                        value = illness,
                        onValueChange = { illness = it },
                        label = { Text("Illness") },
                        modifier = fieldModifier,
                        shape = fieldShape,
                        leadingIcon = { Icon(Icons.Default.Info, contentDescription = null) }
                    )

                    OutlinedTextField(
                        value = gender,
                        onValueChange = { gender = it },
                        label = { Text("Gender") },
                        modifier = fieldModifier,
                        shape = fieldShape,
                        leadingIcon = { Icon(Icons.Default.Face, contentDescription = null) }
                    )

                    OutlinedTextField(
                        value = date_of_visit,
                        onValueChange = { date_of_visit = it },
                        label = { Text("Date of Visit") },
                        modifier = fieldModifier,
                        shape = fieldShape,
                        leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            patientViewModel.updatePatient(
                                patientId = id,
                                imageUri = imageUri.value,
                                name = name,
                                age = age,
                                illness = illness,
                                phone = phone,
                                gender = gender,
                                date_of_visit = date_of_visit,
                                imageUrl = patient?.imageUrl,
                                context = context,
                                navController = navController
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF880E4F))
                    ) {
                        Icon(Icons.Default.Done, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Update Details", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}