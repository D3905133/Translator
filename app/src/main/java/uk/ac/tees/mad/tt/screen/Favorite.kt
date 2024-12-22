package uk.ac.tees.mad.tt.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import uk.ac.tees.mad.tt.TranlatorViewmodel

@Composable
fun Favorite(viewModel: TranlatorViewmodel, navController: NavHostController) {
    val waitingText = remember {
        mutableStateOf("Loading....")
    }
    LaunchedEffect(Unit) {
        delay(2000L)
        waitingText.value = "No Data Available."
    }
    val savedOffline = viewModel.savedOffline.value
    Box(modifier = Modifier
        .fillMaxSize()
        .systemBarsPadding()){
        Column(modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()) {
            if (savedOffline != null) {
                LazyColumn {
                    items(savedOffline) { item->
                        CardView(item.from, item.result, item.fromLang, item.toLang)
                    }
                }
            }else{
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                    Text(text = waitingText.value)
                }
            }
        }
    }
}

@Composable
fun CardView(from: String, result: String, fromLang: String, toLang: String) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(20.dp), elevation = 12.dp) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(text = from)
            Text(text = result)
            Row {
                Text(text = fromLang)
                Spacer(modifier = Modifier.weight(1f))
                Text(text = toLang)
            }
        }
    }
}