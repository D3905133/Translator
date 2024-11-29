package uk.ac.tees.mad.tt.screen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateValueAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import uk.ac.tees.mad.tt.R

@Composable
fun Splash(navController: NavHostController) {
    val scale = remember {
        Animatable(0f)
    }
    LaunchedEffect(Unit) {
        scale.animateTo(2f, animationSpec = tween(1000, easing = LinearEasing))
        scale.animateTo(1f, animationSpec = tween(1000, easing = LinearEasing))
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.designer),
            contentDescription = "app_icon",
            modifier = Modifier
                .size(150.dp)
                .clip(
                    CircleShape
                )
                .scale(scale.value)
        )
        Text(text = "Translate")
    }
}