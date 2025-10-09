package com.example.tracklift_asa.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tracklift_asa.ui.UserViewModel
import com.example.tracklift_asa.ui.UserViewModelFactory
import com.example.tracklift_asa.navigation.Screen
import com.example.tracklift_asa.ui.theme.*
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalContext

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
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current

    // Animações
    val tabIndicatorOffset by animateFloatAsState(
        targetValue = if (isLoginSelected) 0f else 1f,
        animationSpec = tween(300),
        label = "tab_indicator"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        TrackLiftBackground,
                        TrackLiftSurface,
                        TrackLiftBackground
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            
            // Logo e título
            Text(
                text = "TrackLift",
                style = MaterialTheme.typography.displayMedium,
                color = TrackLiftOnBackground,
                fontWeight = FontWeight.Bold
            )
            
            Text(
                text = "Transforme seu corpo, transforme sua vida",
                style = MaterialTheme.typography.bodyMedium,
                color = TrackLiftOnSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )
            
            Spacer(modifier = Modifier.height(48.dp))
            
            // Seletor de aba
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(containerColor = TrackLiftSurfaceVariant),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TabButton(
                        text = "Entrar",
                        isSelected = isLoginSelected,
                        onClick = { 
                            isLoginSelected = true
                            errorMessage = ""
                        },
                        modifier = Modifier.weight(1f)
                    )
                    TabButton(
                        text = "Registrar",
                        isSelected = !isLoginSelected,
                        onClick = { 
                            isLoginSelected = false
                            errorMessage = ""
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Formulário
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = TrackLiftSurface),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Campos animados para registro
                    AnimatedVisibility(
                        visible = !isLoginSelected,
                        enter = slideInVertically() + fadeIn(),
                        exit = slideOutVertically() + fadeOut()
                    ) {
                        ModernTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = "Nome completo",
                            leadingIcon = Icons.Default.Person,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = { /* Auto focus next field */ }
                            )
                        )
                    }
                    
                    ModernTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = "Email",
                        leadingIcon = Icons.Default.Email,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { /* Auto focus next field */ }
                        )
                    )
                    
                    ModernTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = "Senha",
                        leadingIcon = Icons.Default.Lock,
                        isPassword = true,
                        passwordVisible = passwordVisible,
                        onPasswordVisibilityToggle = { passwordVisible = !passwordVisible },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = if (isLoginSelected) ImeAction.Done else ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = { keyboardController?.hide() }
                        )
                    )
                    
                    AnimatedVisibility(
                        visible = !isLoginSelected,
                        enter = slideInVertically() + fadeIn(),
                        exit = slideOutVertically() + fadeOut()
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            ModernTextField(
                                value = confirmPassword,
                                onValueChange = { confirmPassword = it },
                                label = "Confirmar senha",
                                leadingIcon = Icons.Default.Lock,
                                isPassword = true,
                                passwordVisible = confirmPasswordVisible,
                                onPasswordVisibilityToggle = { confirmPasswordVisible = !confirmPasswordVisible },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Password,
                                    imeAction = ImeAction.Next
                                )
                            )
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                ModernTextField(
                                    value = height,
                                    onValueChange = { height = it.filter { c -> c.isDigit() } },
                                    label = "Altura (cm)",
                                    keyboardType = KeyboardType.Number,
                                    modifier = Modifier.weight(1f)
                                )
                                
                                ModernTextField(
                                    value = age,
                                    onValueChange = { age = it.filter { c -> c.isDigit() } },
                                    label = "Idade",
                                    keyboardType = KeyboardType.Number,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                    
                    // Mensagem de erro
                    AnimatedVisibility(
                        visible = errorMessage.isNotEmpty(),
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = TrackLiftError.copy(alpha = 0.1f)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = errorMessage,
                                color = TrackLiftError,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Botão principal
                    Button(
                        onClick = {
                            keyboardController?.hide()
                            isLoading = true
                            scope.launch {
                                if (isLoginSelected) {
                                    userViewModel.login(email, password) { success, errorMsg ->
                                        isLoading = false
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
                                        isLoading = false
                                        errorMessage = "As senhas não coincidem."
                                        return@launch
                                    }
                                    if (name.isBlank() || email.isBlank() || password.isBlank() || height.isBlank() || age.isBlank()) {
                                        isLoading = false
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
                                        isLoading = false
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
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = TrackLiftPrimary,
                            contentColor = TrackLiftOnPrimary
                        ),
                        shape = RoundedCornerShape(16.dp),
                        enabled = !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = TrackLiftOnPrimary,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = if (isLoginSelected) "Entrar" else "Criar conta",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Link alternativo
            TextButton(
                onClick = {
                    isLoginSelected = !isLoginSelected
                    errorMessage = ""
                }
            ) {
                Text(
                    text = if (isLoginSelected) "Não tem conta? Criar agora" else "Já tem conta? Fazer login",
                    color = TrackLiftPrimary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun TabButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(4.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) TrackLiftPrimary else Color.Transparent
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            color = if (isSelected) TrackLiftOnPrimary else TrackLiftOnSurfaceVariant,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            modifier = Modifier.padding(vertical = 16.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ModernTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    onPasswordVisibilityToggle: (() -> Unit)? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { 
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium
            ) 
        },
        leadingIcon = leadingIcon?.let { icon ->
            {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = TrackLiftOnSurfaceVariant
                )
            }
        },
        trailingIcon = if (isPassword) {
            {
                IconButton(onClick = { onPasswordVisibilityToggle?.invoke() }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = if (passwordVisible) "Esconder senha" else "Mostrar senha",
                        tint = TrackLiftOnSurfaceVariant
                    )
                }
            }
        } else null,
        visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = keyboardOptions.copy(keyboardType = keyboardType),
        keyboardActions = keyboardActions,
        modifier = modifier,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = TrackLiftPrimary,
            unfocusedBorderColor = TrackLiftDivider,
            focusedLabelColor = TrackLiftPrimary,
            unfocusedLabelColor = TrackLiftOnSurfaceVariant,
            cursorColor = TrackLiftPrimary,
            focusedTextColor = TrackLiftOnSurface,
            unfocusedTextColor = TrackLiftOnSurface
        ),
        shape = RoundedCornerShape(12.dp),
        singleLine = true
    )
} 