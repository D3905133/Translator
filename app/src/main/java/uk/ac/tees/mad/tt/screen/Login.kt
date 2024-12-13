package uk.ac.tees.mad.tt.screen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import uk.ac.tees.mad.tt.R

@Composable
fun Login(navController: NavHostController) {
    val offsetY = remember { Animatable(2000f) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var email by remember {
        mutableStateOf("")
    }

    LaunchedEffect(Unit) {
        scope.launch {
            offsetY.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis =  1000, easing = LinearEasing)
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
                .padding(top = 150.dp)
                .clip(RoundedCornerShape(20.dp, 20.dp))
                .background(colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp)) {
                Image(painter = painterResource(id = R.drawable.designer), contentDescription = "app_icon",
                    modifier = Modifier.size(80.dp))
                Text(
                    text = "Sign in to begin using Translator",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                Spacer(modifier = Modifier.height(40.dp))

            }
        }
    }
}