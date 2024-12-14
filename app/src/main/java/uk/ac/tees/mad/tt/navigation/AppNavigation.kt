package uk.ac.tees.mad.tt.navigation

import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import uk.ac.tees.mad.tt.screen.Login
import uk.ac.tees.mad.tt.screen.Signup
import uk.ac.tees.mad.tt.screen.Splash

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    Surface {
        NavHost(navController = navController, startDestination = AppNavComp.Splash.destination) {
            composable(AppNavComp.Splash.destination) {
                Splash(navController)
            }
            composable(AppNavComp.Login.destination) {
                Login(navController)
            }
            composable(AppNavComp.Signup.destination) {
                Signup(navController)
            }
        }
    }
}