package com.com.weatherapp

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.com.weatherapp.ui.theme.WeatherAppTheme

class RegisterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherAppTheme {
                RegisterPage()
            }
        }
    }
}

@Composable
fun RegisterPage(modifier: Modifier = Modifier) {
    // Variáveis de estado para armazenar as entradas do usuário
    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }

    val activity = LocalContext.current as? Activity

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ) {
        Text(text = "Crie sua conta", fontSize = 24.sp)
        Spacer(modifier = modifier.size(24.dp))

        // Campo para o nome
        OutlinedTextField(
            value = name,
            label = { Text(text = "Digite seu nome") },
            modifier = modifier.fillMaxWidth(),
            onValueChange = { name = it }
        )
        Spacer(modifier = modifier.size(16.dp))

        // Campo para o e-mail
        OutlinedTextField(
            value = email,
            label = { Text(text = "Digite seu e-mail") },
            modifier = modifier.fillMaxWidth(),
            onValueChange = { email = it }
        )
        Spacer(modifier = modifier.size(16.dp))

        // Campo para a senha
        OutlinedTextField(
            value = password,
            label = { Text(text = "Digite sua senha") },
            modifier = modifier.fillMaxWidth(),
            onValueChange = { password = it },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = modifier.size(16.dp))

        // Campo para repetir a senha
        OutlinedTextField(
            value = confirmPassword,
            label = { Text(text = "Repita sua senha") },
            modifier = modifier.fillMaxWidth(),
            onValueChange = { confirmPassword = it },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = modifier.size(24.dp))

        // Botões: Registrar e Limpar
        Row(modifier = modifier.fillMaxWidth()) {
            Button(
                onClick = {
                    if (password == confirmPassword) {
                       Toast.makeText(activity, "Registro realizado com sucesso!", Toast.LENGTH_SHORT).show()
                        activity?.finish()
                    } else {
                        // Exibe um Toast se as senhas não coincidirem
                        Toast.makeText(activity, "As senhas não coincidem!", Toast.LENGTH_SHORT).show()
                    }
                },
                enabled = name.isNotEmpty()
                        && email.isNotEmpty()
                        && password.isNotEmpty()
                        && confirmPassword.isNotEmpty(),
                modifier = modifier.weight(1f)
            ) {
                Text("Registrar")
            }

            Spacer(modifier = modifier.size(16.dp))

            Button(
                onClick = {
                    // Limpa os campos
                    name = ""
                    email = ""
                    password = ""
                    confirmPassword = ""
                },
                modifier = modifier.weight(1f)
            ) {
                Text("Limpar")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterPagePreview() {
    WeatherAppTheme {
        RegisterPage()
    }
}
