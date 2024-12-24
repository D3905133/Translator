package uk.ac.tees.mad.tt.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
        ) {
            Row(modifier = Modifier.fillMaxWidth().height(60.dp).background(colorScheme.primary) , verticalAlignment = Alignment.CenterVertically) {
                Spacer(modifier = Modifier.padding(5.dp))
                Icon(imageVector = Icons.Outlined.ArrowBackIosNew, contentDescription = "back", tint = Color.White,
                    modifier = Modifier.clickable {
                        navController.popBackStack()
                    })
                Spacer(modifier = Modifier.padding(10.dp))
                Text(text = "Favorite", color = Color.White, fontSize = 20.sp)
            }
            if (savedOffline != null) {
                LazyColumn {
                    items(savedOffline) { item ->
                        CardView(item.from, item.result, item.fromLang, item.toLang){
                            viewModel.deleteFromDatabase(item)
                        }
                    }
                }
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = waitingText.value)
                }
            }
        }
    }
}

@Composable
fun CardView(from: String, result: String, fromLang: String, toLang: String, onDeleteClick:()-> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp), elevation = 12.dp
    ) {
        Box {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(text = from)
                Spacer(modifier = Modifier.padding(5.dp))
                Text(text = result, fontWeight = FontWeight.SemiBold, fontSize = 20.sp)
                Spacer(modifier = Modifier.padding(5.dp))
                Text(text = "From Language :$fromLang")
                Spacer(modifier = Modifier.padding(5.dp))
                Text(text = "To Language :$toLang")
            }
            Icon(
                imageVector = Icons.Outlined.DeleteOutline,
                contentDescription = "delete_content",
                tint = Color.Red,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(10.dp)
                    .clickable {
                        onDeleteClick()
                    }
            )
        }
    }
}