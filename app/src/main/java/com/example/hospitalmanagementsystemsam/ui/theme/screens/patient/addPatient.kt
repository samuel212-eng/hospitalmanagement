package com.example.hospitalmanagementsystemsam.ui.theme.screens.patient

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.hospitalmanagementsystemsam.data.PatientViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPatientScreen(navController: NavController){
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    val patientViewModel: PatientViewModel= viewModel()
    val context = LocalContext.current


    var phone by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var illness by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var date_of_visit by remember { mutableStateOf("") }



    Scaffold(topBar = {
        TopAppBar(title = { Text(text = "Add Patient") },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Blue,
                titleContentColor = Color.White
            ))
    })
    { padding ->
        Column(
            modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center)
        {
            Box(modifier = Modifier.size(120.dp).align(Alignment.CenterHorizontally),
                contentAlignment = Alignment.Center,)
            {
                if(imageUri != null){
                    Image(painter = rememberAsyncImagePainter(imageUri),
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth(),
                        contentScale = ContentScale.Crop)
                }else{
                    Icon(Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(80.dp))
                }
            }
            Button(onClick = {launcher.launch("image/*")},
                modifier = Modifier.align(Alignment.CenterHorizontally))
            { Text(text = "Select Image") }

            OutlinedTextField(
                value = name,
                onValueChange = {name = it},
                label = {Text("Patient's Name")},
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = age,
                onValueChange = {age = it},
                label = {Text("Patient's Age")},
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = phone,
                onValueChange = {phone = it},
                label = {Text("Patient's phone number")},
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = illness,
                onValueChange = {illness = it},
                label = {Text("Patient's illness")},
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = gender,
                onValueChange = {gender = it},
                label = {Text("Patient's Gender")},
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = date_of_visit,
                onValueChange = {date_of_visit = it},
                label = {Text("Date Of Visit")},
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(onClick = {
                val patientId = System.currentTimeMillis().toString()
                patientViewModel.uploadPatient(
                    patientId = patientId,
                    imageUri = imageUri,
                    name = name,
                    age = age,
                    phone = phone,
                    illness = illness,
                    gender = gender,
                    date_of_visit = date_of_visit,
                    context = context,
                    navController = navController
                )},
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp))
            { Text(text = "Save Patient") }

        }

    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AddPatientScreenPreview(){
    AddPatientScreen(rememberNavController())
}