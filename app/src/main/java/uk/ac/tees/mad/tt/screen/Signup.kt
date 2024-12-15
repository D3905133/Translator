package uk.ac.tees.mad.tt.screen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import uk.ac.tees.mad.tt.R
import uk.ac.tees.mad.tt.TranlatorViewmodel
import uk.ac.tees.mad.tt.navigation.AppNavComp

@Composable
fun Signup(navController: NavHostController, viewModel: TranlatorViewmodel) {
    val offsetY = remember { Animatable(2000f) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var name by remember {
        mutableStateOf("")
    }
    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    val isPasswordVisible = remember {
        mutableStateOf(false)
    }
    val loading = viewModel.loadingInApp
    val loggedIn = viewModel.loggedIn

    if (loggedIn.value){
        navController.navigate(AppNavComp.Home.destination)
    }

    LaunchedEffect(Unit) {
        scope.launch {
            offsetY.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 1000, easing = LinearEasing)
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.primary)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .offset { IntOffset(0, offsetY.value.toInt()) }
                .padding(top = 200.dp)
                .clip(RoundedCornerShape(20.dp, 20.dp))
                .background(colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.designer),
                    contentDescription = "app_icon",
                    modifier = Modifier.size(80.dp)
                )
                Text(
                    text = "Sign up to begin using Translator",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    color = colorScheme.onBackground,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
            Spacer(modifier = Modifier.height(40.dp))
            OutlinedTextField(
                value = name, onValueChange = { name = it },
                colors = TextFieldDefaults.textFieldColors(
                    textColor = colorScheme.onBackground,
                    disabledTextColor = colorScheme.onBackground,
                    leadingIconColor = colorScheme.onBackground,
                    trailingIconColor = colorScheme.onBackground,
                    focusedIndicatorColor = colorScheme.onBackground,
                    unfocusedIndicatorColor = colorScheme.onBackground,
                    disabledIndicatorColor = colorScheme.onBackground,
                    focusedLabelColor = colorScheme.onBackground,
                    unfocusedLabelColor = colorScheme.onBackground,
                    cursorColor = colorScheme.onBackground,
                    placeholderColor = colorScheme.onBackground
                ),
                leadingIcon = {
                    Icon(imageVector = Icons.Outlined.Person, contentDescription = null)
                },
                label = { Text(text = "Name") }, shape = RoundedCornerShape(20.dp),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedTextField(
                value = email, onValueChange = { email = it },
                colors = TextFieldDefaults.textFieldColors(
                    textColor = colorScheme.onBackground,
                    disabledTextColor = colorScheme.onBackground,
                    leadingIconColor = colorScheme.onBackground,
                    trailingIconColor = colorScheme.onBackground,
                    focusedIndicatorColor = colorScheme.onBackground,
                    unfocusedIndicatorColor = colorScheme.onBackground,
                    disabledIndicatorColor = colorScheme.onBackground,
                    focusedLabelColor = colorScheme.onBackground,
                    unfocusedLabelColor = colorScheme.onBackground,
                    cursorColor = colorScheme.onBackground,
                    placeholderColor = colorScheme.onBackground
                ),
                leadingIcon = {
                    Icon(imageVector = Icons.Outlined.Mail, contentDescription = null)
                },
                label = { Text(text = "Email") }, shape = RoundedCornerShape(20.dp),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedTextField(value = password,
                onValueChange = { password = it },
                colors = TextFieldDefaults.textFieldColors(
                    textColor = colorScheme.onBackground,
                    disabledTextColor = colorScheme.onBackground,
                    leadingIconColor = colorScheme.onBackground,
                    trailingIconColor = colorScheme.onBackground,
                    focusedIndicatorColor = colorScheme.onBackground,
                    unfocusedIndicatorColor = colorScheme.onBackground,
                    disabledIndicatorColor = colorScheme.onBackground,
                    focusedLabelColor = colorScheme.onBackground,
                    unfocusedLabelColor = colorScheme.onBackground,
                    cursorColor = colorScheme.onBackground,
                    placeholderColor = colorScheme.onBackground
                ),
                leadingIcon = {
                    Icon(imageVector = Icons.Outlined.Lock, contentDescription = null)
                },
                label = { Text(text = "Password") },
                shape = RoundedCornerShape(20.dp),
                trailingIcon = {
                    Icon(
                        imageVector = if (isPasswordVisible.value) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
                        contentDescription = "", modifier = Modifier.clickable {
                            isPasswordVisible.value = !isPasswordVisible.value
                        }
                    )
                },
                singleLine = true,
                visualTransformation = if (isPasswordVisible.value) VisualTransformation.None else PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(onClick = { viewModel.signUp(context, name, email, password) }, shape = RoundedCornerShape(20.dp), colors = ButtonDefaults.buttonColors(
                colorScheme.primary), modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 60.dp)) {
                Text(text = "Sign up", fontWeight = FontWeight.SemiBold, color = colorScheme.background)
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text(text = "Already have an account?", color = colorScheme.onBackground)
                Text(text = "Log in", color = colorScheme.primary, fontWeight = FontWeight.SemiBold, modifier = Modifier.clickable {
                    navController.navigate(AppNavComp.Signup.destination){
                        popUpTo(0)
                    }
                }
                )
            }
        }
        if(loading.value){
            Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.7f)), contentAlignment = Alignment.Center){
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
        }
    }
}