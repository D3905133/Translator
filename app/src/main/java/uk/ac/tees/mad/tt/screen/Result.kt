package uk.ac.tees.mad.tt.screen

import android.content.Context
import android.content.Intent
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Speaker
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavHostController
import uk.ac.tees.mad.tt.TranlatorViewmodel
import java.util.Locale

private const val TAG = "ResultScreenTTS"

@Composable
fun Result(
    navHostController: NavHostController,
    viewmodel: TranlatorViewmodel,
    from: String,
    result: String,
    fromLang: String,
    toLang: String,
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    var tts: TextToSpeech? = remember { null }
    val isTtsInitialized = remember { mutableStateOf(false) }
    DisposableEffect(context) {
        Log.d(TAG, "Initializing TextToSpeech")
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                Log.d(TAG, "TextToSpeech initialized successfully")
                val languageResult = tts?.setLanguage(Locale.US)
                if (languageResult == TextToSpeech.LANG_MISSING_DATA || languageResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e(TAG, "Language not supported")
                    Toast.makeText(context, "Language not supported", Toast.LENGTH_SHORT).show()
                } else {
                    isTtsInitialized.value = true
                    Log.d(TAG, "Language set successfully")
                }
            } else {
                Log.e(TAG, "TTS Initialization failed with status: $status")
                Toast.makeText(context, "TTS Initialization failed", Toast.LENGTH_SHORT).show()
            }
        }
        onDispose {
            Log.d(TAG, "Shutting down TextToSpeech")
            tts?.shutdown()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            elevation = 20.dp
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = result,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = from, fontSize = 15.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "From : $fromLang", fontSize = 10.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(5.dp))
                Text(text = "To : $toLang", fontSize = 10.sp, color = Color.Gray)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Speaker,
                        contentDescription = "speak",
                        modifier = Modifier
                            .clickable {
                                if (isTtsInitialized.value) {
                                    Log.d(TAG, "Attempting to speak text: $result")
                                    val speakStatus = tts?.speak(result, TextToSpeech.QUEUE_FLUSH, null, null)
                                    if (speakStatus == TextToSpeech.ERROR) {
                                        Log.e(TAG, "Error in speaking text")
                                        Toast.makeText(context, "Error in speaking text", Toast.LENGTH_SHORT).show()
                                    }
                                } else {
                                    Log.e(TAG, "TTS not initialized")
                                    Toast.makeText(context, "TTS not initialized", Toast.LENGTH_SHORT).show()
                                }
                            }
                            .padding(8.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Icon(
                        imageVector = Icons.Outlined.Share,
                        contentDescription = "share",
                        modifier = Modifier
                            .clickable {
                                shareText(context, result)
                            }
                            .padding(8.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Icon(
                        imageVector = Icons.Outlined.ContentCopy,
                        contentDescription = "copy result",
                        modifier = Modifier
                            .clickable {
                                Toast
                                    .makeText(context, "Copied", Toast.LENGTH_SHORT)
                                    .show()
                                clipboardManager.setText(AnnotatedString(result))
                            }
                            .padding(8.dp)
                    )
                }
            }
        }

        Icon(
            imageVector = Icons.Outlined.Save,
            contentDescription = "save to database",
            modifier = Modifier
                .align(Alignment.End)
                .padding(end = 40.dp)
                .clickable {
                    viewmodel.saveToDatabase(from = from, result = result, fromLang = fromLang, toLang = toLang)
                    Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
                }
        )
    }
}

fun shareText(context: Context, text: String) {
    val shareIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, text)
        type = "text/plain"
    }
    startActivity(context, Intent.createChooser(shareIntent, null), null)
}
