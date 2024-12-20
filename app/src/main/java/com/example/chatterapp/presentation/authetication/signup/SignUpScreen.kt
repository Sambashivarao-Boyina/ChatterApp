package com.example.chatterapp.presentation.authetication.signup

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.chatterapp.R
import com.example.chatterapp.presentation.authetication.components.AuthButton
import com.example.chatterapp.presentation.authetication.components.InputBox
import com.example.chatterapp.presentation.authetication.components.PasswordInput
import com.example.chatterapp.ui.theme.Blue


@Composable
fun SignUpScreen(
    navController: NavController,
    state: SignupState,
    onEvent:(SignUpEvent) -> Unit,
    isLoading: Boolean
) {


    val annotatedString = buildAnnotatedString {
        append("Welcome to ")
        withStyle(style = SpanStyle(color = Blue)) {
            append(stringResource(R.string.app_name))
        }
    }

    val signupAnotatedString = buildAnnotatedString {
        append("Already registered?")
        withStyle(style = SpanStyle(color = Blue)) {
            append(" Login")
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
            text = "SIGN UP",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.displaySmall,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        InputBox(
            value = state.email,
            onChange = {
                onEvent(SignUpEvent.EmailUpdate(it))
            },
            placeholder = "Enter Email",
            painter = painterResource(R.drawable.ic_email),
            isError = state.isEmailError
        )
        Spacer(modifier = Modifier.height(20.dp))

        InputBox(
            value = state.username,
            onChange = {
                onEvent(SignUpEvent.UsernameUpdate(it))
            },
            placeholder = "Enter UserName",
            painter = painterResource(R.drawable.ic_person),
            isError = state.isUserNameError
        )
        Spacer(modifier = Modifier.height(20.dp))

        PasswordInput(
            value = state.password,
            onChange = {
                onEvent(SignUpEvent.PasswordUpdate(it))
            },
            placeholder = "Enter the Password",
            isError = state.isPasswordError
        )

        Spacer(modifier = Modifier.height(20.dp))

        PasswordInput(
            value = state.confirmPassword,
            onChange = {
                onEvent(SignUpEvent.ConfirmPasswordUpdate(it))
            },
            placeholder = "Enter the Confirm Password",
            isError = state.isConfirmPasswordError
        )

        Spacer(modifier = Modifier.height(20.dp))

        AuthButton(
            text = "Sign Up",
            onClick = {
                onEvent(SignUpEvent.SignUp)
            },
            isLoading = isLoading
        )


        TextButton(
            onClick = {
                navController.popBackStack()
            }
        ) {
            Text(
                text = signupAnotatedString,
                textAlign = TextAlign.Center,
                color = Color.White
            )
        }

    }
}


//@Preview(showBackground = true)
//@Composable
//fun SignUpScreenPreview() {
//    ChatterAppTheme {
//        SignUpScreen(navController = rememberNavController())
//    }
//}