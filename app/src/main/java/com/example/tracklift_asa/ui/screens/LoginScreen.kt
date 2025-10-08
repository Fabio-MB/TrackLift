package com.example.tracklift_asa.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tracklift_asa.ui.UserViewModel
import com.example.tracklift_asa.ui.UserViewModelFactory
import com.example.tracklift_asa.navigation.Screen
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.PopupProperties

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    userViewModel: UserViewModel = viewModel(
        factory = UserViewModelFactory(LocalContext.current.applicationContext as android.app.Application)
    ),
    onLoginClick: (String, String) -> Unit = { _, _ -> },
    onForgotPassword: () -> Unit = {},
    onRegisterClick: () -> Unit = {}
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var isLoginSelected by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF232323))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(60.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Log in",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = if (isLoginSelected) FontWeight.Bold else FontWeight.Normal,
                modifier = Modifier.clickable { isLoginSelected = true }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Registro",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = if (!isLoginSelected) FontWeight.Bold else FontWeight.Normal,
                modifier = Modifier.clickable { isLoginSelected = false }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .height(4.dp)
                .width(60.dp)
                .background(Color(0xFFFF9800), RoundedCornerShape(2.dp))
                .align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(32.dp))

        if (!isLoginSelected) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nome", color = Color.White) },
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .padding(vertical = 4.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFFF9800),
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White,
                    cursorColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )
        }
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email", color = Color.White) },
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .padding(vertical = 4.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFFF9800),
                unfocusedBorderColor = Color.Gray,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.White,
                cursorColor = Color.White,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Senha", color = Color.White) },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = if (passwordVisible) "Esconder senha" else "Mostrar senha",
                        tint = Color.White
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .padding(vertical = 4.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFFF9800),
                unfocusedBorderColor = Color.Gray,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.White,
                cursorColor = Color.White,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )

        if (!isLoginSelected) {
            OutlinedTextField(
                value = height,
                onValueChange = { height = it.filter { c -> c.isDigit() } },
                label = { Text("Altura (cm)", color = Color.White) },
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .padding(vertical = 4.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFFF9800),
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White,
                    cursorColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )
            OutlinedTextField(
                value = age,
                onValueChange = { age = it.filter { c -> c.isDigit() } },
                label = { Text("Idade", color = Color.White) },
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .padding(vertical = 4.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFFF9800),
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White,
                    cursorColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirmar Senha", color = Color.White) },
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(
                            imageVector = if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (confirmPasswordVisible) "Esconder senha" else "Mostrar senha",
                            tint = Color.White
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .padding(vertical = 4.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFFF9800),
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White,
                    cursorColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )
        }

        if (errorMessage.isNotEmpty()) {
            Text(errorMessage, color = Color.Red, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            onClick = {
                scope.launch {
                    if (isLoginSelected) {
                        userViewModel.login(email, password) { success, errorMsg ->
                            if (success) {
                                errorMessage = ""
                                navController.navigate(Screen.MainScreen.route) {
                                    popUpTo("login") { inclusive = true }
                                }
                            } else {
                                errorMessage = errorMsg ?: "Email ou senha inválidos."
                            }
                        }
                    } else {
                        if (password != confirmPassword) {
                            errorMessage = "As senhas não coincidem."
                            return@launch
                        }
                        if (name.isBlank() || email.isBlank() || password.isBlank() || height.isBlank() || age.isBlank()) {
                            errorMessage = "Preencha todos os campos."
                            return@launch
                        }
                        userViewModel.register(
                            name = name,
                            email = email,
                            password = password,
                            height = height.toIntOrNull() ?: 0,
                            age = age.toIntOrNull() ?: 0
                        ) { success, errorMsg ->
                            if (success) {
                                errorMessage = "Cadastro realizado! Faça login."
                                isLoginSelected = true
                            } else {
                                errorMessage = errorMsg ?: "Email já cadastrado."
                            }
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800)),
            shape = RoundedCornerShape(24.dp)
        ) {
            Text(if (isLoginSelected) "Entrar" else "Registrar", color = Color.White, fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(12.dp))
        TextButton(onClick = {
            isLoginSelected = !isLoginSelected
            errorMessage = ""
        }) {
            Text(if (isLoginSelected) "Não tem conta? Registre-se" else "Já tem conta? Faça login", color = Color.White, fontSize = 14.sp)
        }
    }
} 