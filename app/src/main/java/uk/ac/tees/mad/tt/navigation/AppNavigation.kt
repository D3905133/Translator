package uk.ac.tees.mad.tt.navigation

import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import uk.ac.tees.mad.tt.TranlatorViewmodel
import uk.ac.tees.mad.tt.screen.Home
import uk.ac.tees.mad.tt.screen.Login
import uk.ac.tees.mad.tt.screen.Signup
import uk.ac.tees.mad.tt.screen.Splash

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val viewModel : TranlatorViewmodel = viewModel()
    Surface {
        NavHost(navController = navController, startDestination = AppNavComp.Splash.destination) {
            composable(AppNavComp.Splash.destination) {
                Splash(navController, viewModel)
            }
            composable(AppNavComp.Login.destination) {
                Login(navController, viewModel)
            }
            composable(AppNavComp.Signup.destination) {
                Signup(navController, viewModel)
            }
            composable(AppNavComp.Home.destination) {
                Home(navController, viewModel)
            }
        }
    }
}