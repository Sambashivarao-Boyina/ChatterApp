package com.example.chatterapp.presentation.authetication.login

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import androidx.navigation.NavController
import com.example.chatterapp.R
import com.example.chatterapp.presentation.authetication.components.AuthButton
import com.example.chatterapp.presentation.authetication.components.Divider
import com.example.chatterapp.presentation.authetication.components.GoogleButton
import com.example.chatterapp.presentation.authetication.components.InputBox
import com.example.chatterapp.presentation.authetication.components.PasswordInput
import com.example.chatterapp.presentation.navGraph.Route
import com.example.chatterapp.ui.theme.Blue



@SuppressLint("RememberReturnType")
@Composable
fun LoginScreen(
    navController: NavController,
    state:LoginState,
    onEvent: (LoginEvent) -> Unit,
    isLoading: Boolean
) {


    val annotatedString = buildAnnotatedString {
        append("Welcome back to ")
        withStyle(style = SpanStyle(color = Blue)) {
            append(stringResource(R.string.app_name))
        }
    }

    val loginAnotatedString = buildAnnotatedString {
        append("New User?")
        withStyle(style = SpanStyle(color = Blue)) {
            append(" Sign Up")
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = null,
            modifier = Modifier.size(200.dp)
        )

        Text(
            text = annotatedString,
            color = Color.White,
            fontWeight = FontWeight.ExtraBold,
            style = MaterialTheme.typography.displayMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "LOGIN",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.displaySmall,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        InputBox(
            value = state.email,
            onChange = {
                onEvent(LoginEvent.EmailUpdate(it))
            },
            placeholder = "Enter Email",
            painter = painterResource(R.drawable.ic_email),
            isError = state.isEmailError
        )
        Spacer(modifier = Modifier.height(20.dp))

        PasswordInput(
            value = state.password,
            placeholder = "Enter the Password",
            onChange= {
                onEvent(LoginEvent.PasswordUpdate(it))
            },
            isError = state.isPasswordError
        )

        Spacer(modifier = Modifier.height(20.dp))

        AuthButton(
            text = "Login",
            onClick = {
                onEvent(LoginEvent.Login)
            },
            isLoading = isLoading
        )

        Divider()

        GoogleButton {  }

        TextButton(
            onClick = {
                navController.navigate(Route.SignupScreen.route)
            },
        ) {
            Text(
                text = loginAnotatedString,
                color = Color.White
            )
        }

    }
}

//
//@Composable
//@Preview
//fun LoginScreenPreview() {
//    ChatterAppTheme {
//        LoginScreen(navController = rememberNavController())
//    }
//}