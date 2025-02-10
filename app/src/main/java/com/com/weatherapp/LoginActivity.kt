package com.com.weatherapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.com.weatherapp.ui.theme.WeatherAppTheme
import android.content.Intent.FLAG_ACTIVITY_NO_HISTORY
import android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherAppTheme {
               LoginPage()
                }
            }
        }
    }


@Composable
fun LoginPage(modifier: Modifier = Modifier) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val activity = LocalContext.current as? Activity

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ) {
        Text(
            text = "Bem-vindo/a!",
            fontSize = 24.sp
        )
        Spacer(modifier = modifier.size(24.dp))

        OutlinedTextField(
            value = email,
            label = { Text(text = "Digite seu e-mail") },
            modifier = modifier.fillMaxWidth(),
            onValueChange = { email = it }
        )
        Spacer(modifier = modifier.size(24.dp))

        OutlinedTextField(
            value = password,
            label = { Text(text = "Digite sua senha") },
            modifier = modifier.fillMaxWidth(),
            onValueChange = { password = it },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = modifier.size(24.dp))

        Row(modifier = modifier.fillMaxWidth()) {
            Button(
                onClick = {
                    if (email.isNotEmpty() && password.isNotEmpty()) {
                        Firebase.auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(activity!!) { task ->
                                if (task.isSuccessful) {
                                    activity.startActivity(
                                        Intent(activity, MainActivity::class.java).setFlags(
                                            Intent.FLAG_ACTIVITY_SINGLE_TOP
                                        )
                                    )
                                    Toast.makeText(activity, "Login OK!", Toast.LENGTH_LONG).show()
                                } else {
                                    Toast.makeText(activity, "Login FALHOU!", Toast.LENGTH_LONG).show()
                                }
                            }
                    } else {
                        Toast.makeText(activity, "Preencha todos os campos!", Toast.LENGTH_LONG).show()
                    }
        },
                enabled = email.isNotEmpty() && password.isNotEmpty(),
                modifier = modifier.weight(1f)
            ) {
                Text("Login")
            }

            Spacer(modifier = modifier.size(16.dp))
            Button(
                onClick = { email = ""; password = "" },
                modifier = modifier.weight(1f)
            ) {
                Text("Limpar")
            }
        }
        Spacer(modifier = modifier.size(24.dp))
        Button(
            onClick = {
                // Navegar para RegisterActivity
                activity?.startActivity(Intent(activity, RegisterActivity::class.java).setFlags(
                    FLAG_ACTIVITY_SINGLE_TOP or FLAG_ACTIVITY_NO_HISTORY
                ))
            }
        ) {
            Text("Criar uma conta")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPagePreview() {
    WeatherAppTheme {
        LoginPage()
    }
}