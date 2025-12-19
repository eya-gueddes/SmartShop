package com.eya.smartshop.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.eya.smartshop.auth.AuthViewModel
import com.eya.smartshop.ui.theme.*


@Composable
fun LoginScreen(vm: AuthViewModel, onLoginSuccess: () -> Unit) {

    val state = vm.uiState

    // Trigger navigation when user logs in
    LaunchedEffect(state.user) {
        if (state.user != null) {
            onLoginSuccess()
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = ChampagneBeige
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(32.dp)
                    .fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = state.email,
                    onValueChange = vm::onEmailChange,
                    label = { Text("Email") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Email
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Burgundy,
                        unfocusedBorderColor = SandGold,
                        focusedTextColor = DarkMaroon,
                        unfocusedTextColor = DarkMaroon,
                        focusedLabelColor = DarkMaroon,
                        unfocusedLabelColor = DarkMaroon
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = state.password,
                    onValueChange = vm::onPasswordChange,
                    label = { Text("Password") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Password
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Burgundy,
                        unfocusedBorderColor = SandGold,
                        focusedTextColor = DarkMaroon,
                        unfocusedTextColor = DarkMaroon,
                        focusedLabelColor = DarkMaroon,
                        unfocusedLabelColor = DarkMaroon
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { vm.signIn() },
                    colors = ButtonDefaults.buttonColors(containerColor = Burgundy),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Log In", color = Color.White)
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = { vm.signUp() },
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = SandGold,
                        contentColor = Burgundy
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Sign Up")
                }

                state.error?.let {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(it, color = DeepWine)
                }
            }
        }
    }
}