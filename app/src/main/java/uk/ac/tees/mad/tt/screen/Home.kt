package uk.ac.tees.mad.tt.screen

import android.Manifest
import android.app.Activity
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.outlined.ContentPaste
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.Translate
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import uk.ac.tees.mad.tt.TranlatorViewmodel

private const val REQUEST_RECORD_AUDIO_PERMISSION_CODE = 1

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Home(navController: NavHostController, viewModel: TranlatorViewmodel) {
    val context = LocalContext.current
    val activity = (context as? Activity) ?: return
    var textToTranslate by remember { mutableStateOf("") }
    val speechRecognizer = remember { SpeechRecognizer.createSpeechRecognizer(context) }
    var hasMicrophonePermission by remember { mutableStateOf(checkMicrophonePermission(context)) }
    var isRecording by remember { mutableStateOf(false) } // Track recording status

    LaunchedEffect(Unit) {
        viewModel.getAvailableLanguages(context)
    }

    val availableLanguages = viewModel.availableLanguages
    var expandedFrom by remember { mutableStateOf(false) }
    var expandedTo by remember { mutableStateOf(false) }
    var selectedFromLanguage by remember { mutableStateOf(availableLanguages.value?.get(0)?.name ?: "") }
    var selectedToLanguage by remember { mutableStateOf("") }
    val filteredLanguages = availableLanguages.value?.filter { it.name != "AUTO_DETECT" } ?: emptyList()

    if (!hasMicrophonePermission) {
        requestMicrophonePermission(activity)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .navigationBarsPadding()
            .statusBarsPadding(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = textToTranslate,
            onValueChange = { textToTranslate = it },
            colors = TextFieldDefaults.textFieldColors(
                textColor = colorScheme.onBackground,
                cursorColor = colorScheme.onBackground
            ),
            leadingIcon = {
                Icon(imageVector = Icons.Outlined.Translate, contentDescription = null)
            },
            trailingIcon = {
                Column {
                    Icon(
                        imageVector = Icons.Outlined.ContentPaste,
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            pasteFromClipboard(context) { pastedText ->
                                textToTranslate = pastedText
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Icon(
                        imageVector = Icons.Outlined.Mic,
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            if (checkMicrophonePermission(context)) {
                                isRecording = true // Set to true when starting
                                startVoiceRecognition(
                                    context,
                                    speechRecognizer,
                                    onResult = { recognizedText ->
                                        textToTranslate = recognizedText
                                        isRecording = false // Reset after getting result
                                    },
                                    onError = {
                                        isRecording = false // Reset if error occurs
                                    })
                            } else {
                                requestMicrophonePermission(context as Activity)
                            }
                        }
                    )
                }
            },
            label = { Text(text = "Write/Paste Text to Translate") },
            shape = RoundedCornerShape(20.dp),
            singleLine = false,
            minLines = 10,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
        )

        if (isRecording) {
            Text(
                "Listening...",
                color = colorScheme.primary,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            CircularProgressIndicator(color = colorScheme.primary, modifier = Modifier.size(24.dp))
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
        ) {
            Text(text = "From:", modifier = Modifier.align(Alignment.CenterVertically))
            ExposedDropdownMenuBox(
                expanded = expandedFrom,
                onExpandedChange = { expandedFrom = !expandedFrom }) {
                OutlinedTextField(
                    readOnly = true,
                    value = selectedFromLanguage,
                    onValueChange = {},
                    shape = RoundedCornerShape(20.dp),
                    label = { Text("Select Language") },
                    trailingIcon = {
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                    },
                    modifier = Modifier.padding(horizontal = 20.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = colorScheme.background,
                        cursorColor = colorScheme.onBackground
                    )
                )

                ExposedDropdownMenu(
                    expanded = expandedFrom,
                    onDismissRequest = { expandedFrom = false }
                ) {
                    availableLanguages.value!!.forEach { language ->
                        DropdownMenuItem(onClick = {
                            selectedFromLanguage = language.name
                            expandedFrom = false
                        }) {
                            Text(text = language.name)
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
        ) {
            Text(text = "To:    ", modifier = Modifier.align(Alignment.CenterVertically))
            ExposedDropdownMenuBox(
                expanded = expandedTo,
                onExpandedChange = { expandedTo = !expandedTo }) {
                OutlinedTextField(
                    readOnly = true,
                    value = selectedToLanguage,
                    onValueChange = {},
                    shape = RoundedCornerShape(20.dp),
                    label = { Text("Select Language") },
                    trailingIcon = {
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                    },
                    modifier = Modifier.padding(horizontal = 20.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = colorScheme.background,
                        cursorColor = colorScheme.onBackground
                    )
                )

                ExposedDropdownMenu(
                    expanded = expandedTo,
                    onDismissRequest = { expandedTo = false }
                ) {
                    filteredLanguages.forEach { language ->
                        DropdownMenuItem(onClick = {
                            selectedToLanguage = language.name
                            expandedTo = false
                        }) {
                            Text(text = language.name)
                        }
                    }
                }
            }
        }

        Button(onClick = {
            viewModel.translateToData(context = context, textToTranslate, selectedFromLanguage, selectedToLanguage, onSuccessful = {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            })
        }) {
            Text(text = "Translate")
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            speechRecognizer.destroy()
        }
    }
}

private fun pasteFromClipboard(context: Context, onPaste: (String) -> Unit) {
    val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clipData = clipboardManager.primaryClip

    if (clipData != null && clipData.itemCount > 0) {
        val item = clipData.getItemAt(0)
        val pastedText = item.text?.toString() ?: ""
        onPaste(pastedText)
    }
}

private fun checkMicrophonePermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
}

private fun requestMicrophonePermission(activity: Activity) {
    ActivityCompat.requestPermissions(
        activity,
        arrayOf(Manifest.permission.RECORD_AUDIO),
        REQUEST_RECORD_AUDIO_PERMISSION_CODE
    )
}

private fun startVoiceRecognition(
    context: Context,
    speechRecognizer: SpeechRecognizer,
    onResult: (String) -> Unit,
    onError: () -> Unit
) {
    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
        putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
    }

    speechRecognizer.setRecognitionListener(object : RecognitionListener {
        override fun onReadyForSpeech(params: Bundle?) {}

        override fun onBeginningOfSpeech() {}

        override fun onRmsChanged(rmsdB: Float) {}

        override fun onBufferReceived(buffer: ByteArray?) {}

        override fun onEndOfSpeech() {}

        override fun onError(error: Int) {
            Toast.makeText(context, "Error occurred: $error", Toast.LENGTH_SHORT).show()
            onError()
            speechRecognizer.stopListening()
        }

        override fun onResults(results: Bundle) {
            val matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            matches?.let {
                onResult(it[0])
            }
        }

        override fun onPartialResults(partialResults: Bundle) {}

        override fun onEvent(eventType: Int, params: Bundle?) {}
    })

    speechRecognizer.startListening(intent)
}
