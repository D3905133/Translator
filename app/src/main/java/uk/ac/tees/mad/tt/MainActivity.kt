package uk.ac.tees.mad.tt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import uk.ac.tees.mad.tt.navigation.AppNavigation
import uk.ac.tees.mad.tt.ui.theme.TranslatorTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TranslatorTheme {
                AppNavigation()
            }
        }
    }
}
