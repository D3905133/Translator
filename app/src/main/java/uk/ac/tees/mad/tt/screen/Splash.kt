package uk.ac.tees.mad.tt.screen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import uk.ac.tees.mad.tt.R
import uk.ac.tees.mad.tt.TranlatorViewmodel
import uk.ac.tees.mad.tt.navigation.AppNavComp
import uk.ac.tees.mad.tt.navigation.AppNavigation

@Composable
fun Splash(navController: NavHostController,viewModel: TranlatorViewmodel) {
    val scale = remember {
        Animatable(0f)
    }
    LaunchedEffect(Unit) {
        scale.animateTo(2f, animationSpec = tween(1000, easing = LinearEasing))
        scale.animateTo(1f, animationSpec = tween(1000, easing = LinearEasing))
        if (viewModel.loggedIn.value){
            navController.navigate(AppNavComp.Home.destination){
                popUpTo(0)
            }
        }else {
            navController.navigate(AppNavComp.Login.destination) {
                popUpTo(0)
            }
        }
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
        Text(text = "Translator", fontWeight = FontWeight.SemiBold, fontSize = 30.sp)
    }
}