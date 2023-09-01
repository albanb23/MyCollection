package com.albab.mycollection.view.auth.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.albab.mycollection.R
import com.albab.mycollection.view.ui.theme.MyCollectionTheme

@Preview(showBackground = true)
@Composable
fun Prev() {
    MyCollectionTheme {
        LoginScreen(doLogin = { _, _, _ -> }, loginWithGoogle = {}, forgotPassword = {})
    }
}

@Composable
fun LoginScreen(
    doLogin: (String, String, Boolean) -> Unit,
    loginWithGoogle: () -> Unit,
    forgotPassword: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(MaterialTheme.colorScheme.secondaryContainer)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(bottomStart = 80.dp))
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                //Logo
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f)
                .clip(RoundedCornerShape(topEnd = 80.dp))
                .background(MaterialTheme.colorScheme.secondaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.login_title),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(20.dp)
                )
                Spacer(modifier = Modifier.height(30.dp))
                LoginInputs(doLogin, loginWithGoogle, forgotPassword)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginInputs(
    doLogin: (String, String, Boolean) -> Unit,
    loginWithGoogle: () -> Unit,
    forgotPassword: () -> Unit,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    var showPassword by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = {
                Text(text = stringResource(id = R.string.email))
            },
            isError = emailError,
            leadingIcon = {
                Icon(imageVector = Icons.Default.AlternateEmail, contentDescription = "Email")
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = MaterialTheme.colorScheme.background,
                unfocusedBorderColor = MaterialTheme.colorScheme.background
            ),
            shape = RoundedCornerShape(25.dp),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = {
                Text(text = stringResource(id = R.string.password))
            },
            isError = passwordError,
            leadingIcon = {
                Icon(imageVector = Icons.Outlined.Lock, contentDescription = "Password")
            },
            trailingIcon = {
                IconButton(onClick = { showPassword = !showPassword }) {
                    Icon(
                        imageVector = if (showPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = "Show password"
                    )
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = MaterialTheme.colorScheme.background,
                unfocusedBorderColor = MaterialTheme.colorScheme.background
            ),
            shape = RoundedCornerShape(25.dp),
            modifier = Modifier.fillMaxWidth()
        )
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = rememberMe,
                onCheckedChange = { rememberMe = !rememberMe },
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.secondary
                )
            )
            Text(text = stringResource(id = R.string.remember_me))
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(id = R.string.forgot_password),
                textAlign = TextAlign.End,
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .clickable { forgotPassword() })
        }
        Button(
            onClick = { doLogin(email, password, rememberMe) },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(text = stringResource(id = R.string.login).uppercase())
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Divider(Modifier.weight(1f))
            Text(
                text = stringResource(id = R.string.or),
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Divider(Modifier.weight(1f))
        }
        Button(
            onClick = { doLogin(email, password, rememberMe) },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onBackground
            ),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.Login,
                contentDescription = "Login with Google",
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Text(text = stringResource(id = R.string.login_google))
        }
        Row(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(text = stringResource(id = R.string.no_account))
            Text(
                text = stringResource(id = R.string.signup),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}