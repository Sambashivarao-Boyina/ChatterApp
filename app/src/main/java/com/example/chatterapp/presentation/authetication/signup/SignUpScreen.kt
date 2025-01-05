package com.example.chatterapp.presentation.authetication.signup

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
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
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import androidx.navigation.NavController
import com.example.chatterapp.R
import com.example.chatterapp.presentation.authetication.components.AuthButton
import com.example.chatterapp.presentation.authetication.components.Divider
import com.example.chatterapp.presentation.authetication.components.GoogleButton
import com.example.chatterapp.presentation.authetication.components.InputBox
import com.example.chatterapp.presentation.authetication.components.PasswordInput
import com.example.chatterapp.presentation.authetication.login.LoginEvent
import com.example.chatterapp.ui.theme.Blue
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import kotlinx.coroutines.launch
import android.provider.Settings
import androidx.credentials.exceptions.NoCredentialException


@Composable
fun SignUpScreen(
    navController: NavController,
    state: SignupState,
    onEvent:(SignUpEvent) -> Unit,
    isLoading: Boolean
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val onClick: () -> Unit = {
        val credentialManager = CredentialManager.create(context)


        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(context.getString(R.string.web_client_id))
            .setAutoSelectEnabled(false)
            .build()


        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()



        coroutineScope.launch {
            try {
                val result: GetCredentialResponse = credentialManager.getCredential(request = request, context = context)
                val credential = result.credential

                when(credential) {
                    is CustomCredential -> {
                        if(result.credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                            try {
                                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                                onEvent(SignUpEvent.GoogleAuth(email = googleIdTokenCredential.id, username = googleIdTokenCredential.displayName!!))

                            }catch (e: Exception) {
                                Toast.makeText(context, "Something went wrong, try again", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else -> {
                    Toast.makeText(context, "Something went wrong, try again", Toast.LENGTH_SHORT).show()
                }
                }

            }catch (e: NoCredentialException) {
                // Prompt the user to create a Google account
                Toast.makeText(context, "No account found. Please create a Google account.", Toast.LENGTH_LONG).show()

                // Redirect to Google account creation page
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://accounts.google.com/signup"))
                context.startActivity(intent)
            } catch (e: GetCredentialException) {
                Toast.makeText(context, e.message.toString(), Toast.LENGTH_LONG).show()
            } catch (e: GoogleIdTokenParsingException) {
                Toast.makeText(context, e.message.toString(), Toast.LENGTH_LONG).show()
            }
        }

    }


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

        Divider()

        GoogleButton {
            onClick()
        }


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