package com.example.hospitalmanagementsystemsam.ui.theme.screens.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.hospitalmanagementsystemsam.R
import com.example.hospitalmanagementsystemsam.data.AuthViewModel
import com.example.hospitalmanagementsystemsam.navigation.ROUTE_DASHBOARD
import com.example.hospitalmanagementsystemsam.navigation.ROUTE_LOGIN
import com.example.hospitalmanagementsystemsam.navigation.ROUTE_REGISTER

@Composable
fun RegisterScreen(navController: NavController){
    var username by remember { mutableStateOf("") }
    var email by remember {mutableStateOf("")}
    var phone by remember { mutableStateOf("") }
    var password by remember {mutableStateOf("")}
    var confirmpassword by remember { mutableStateOf("") }
    val authViewModel: AuthViewModel= viewModel()
    val context = LocalContext.current
//


    Box(modifier= Modifier.fillMaxSize().background(Brush.linearGradient(colors =listOf(Color.Cyan,Color.Green,Color.Blue))))
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        Image(
            painter= painterResource(id = R.drawable.image),
            contentDescription = "App Logo",
            modifier = Modifier
                .size(140.dp)
                .clip(CircleShape)
                .border(2.dp,Color.Red,CircleShape)
                .shadow(8.dp,CircleShape))


        Text(text = "Sign up to get started",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Red)
        OutlinedTextField(
            value = username,
            onValueChange = {username = it},
            label = {Text(text ="Enter Username")},
            placeholder = {Text(text = "Please enter Username")},
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            shape = RoundedCornerShape(20.dp)
        )
        OutlinedTextField(
            value = email,
            onValueChange = {email= it},
            label = {Text(text ="Enter Email")},
            placeholder = {Text(text = "Please enter Email")},
            leadingIcon = {Icon(Icons.Default.Email, contentDescription = null)},
            shape = RoundedCornerShape(20.dp))

        OutlinedTextField(
            value = phone,
            onValueChange = {phone=it},
            label = {Text(text = "Enter Phone Number")},
            placeholder = {Text(text = "Please enter your phone number")},
            leadingIcon = {Icon(Icons.Default.Phone,contentDescription = null)},
            shape = RoundedCornerShape(20.dp)
        )


        OutlinedTextField(
            value = password,
            onValueChange = {password = it},
            label = {Text(text="Enter Password")},
            visualTransformation = PasswordVisualTransformation(),
            placeholder = {Text(text = "Please enter Password")},
            leadingIcon = {Icon(Icons.Default.Lock, contentDescription = null)},
            shape = RoundedCornerShape(20.dp))
        OutlinedTextField(
            value = confirmpassword,
            onValueChange = {confirmpassword = it},
            label = {Text(text="Confirm Password")},
            visualTransformation = PasswordVisualTransformation(),
            placeholder = {Text(text = "Please confirm Password")},
            leadingIcon = {Icon(Icons.Default.Check, contentDescription = null)},
            shape = RoundedCornerShape(20.dp))

        Button(onClick = {authViewModel.signup(
            username=username,
            email=email,password=password,
            phone = phone,
            confirmpassword=confirmpassword,
            navController =navController,
            context =context)}) {Text(text = "Register") }

        Row {
            Text(text = "Already have an account?", color = Color.Blue)
            Spacer(modifier = Modifier.width(7.dp))
            Text(text = "Login Here",
                color = Color.Green,
                modifier = Modifier
                    .clickable{navController.navigate(
                        ROUTE_LOGIN
            )})

        }

    }

}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegisterScreenPreview(){
    RegisterScreen(rememberNavController())
}