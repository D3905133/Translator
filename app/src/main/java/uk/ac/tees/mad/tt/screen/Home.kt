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
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.ContentPaste
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Translate
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uk.ac.tees.mad.tt.R
import uk.ac.tees.mad.tt.TranlatorViewmodel
import uk.ac.tees.mad.tt.navigation.AppNavComp

private const val REQUEST_RECORD_AUDIO_PERMISSION_CODE = 1
private const val REQUEST_CAMERA_PERMISSION_CODE = 2

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Home(navController: NavHostController, viewModel: TranlatorViewmodel) {
    val context = LocalContext.current
    val activity = (context as? Activity) ?: return

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        if (bitmap != null) {
            viewModel.uploadImageToFirebase(bitmap)
        } else {
            Toast.makeText(context, "Failed to capture image", Toast.LENGTH_SHORT).show()
        }
    }

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var textToTranslate by remember { mutableStateOf("") }
    val speechRecognizer = remember { SpeechRecognizer.createSpeechRecognizer(context) }
    var hasMicrophonePermission by remember { mutableStateOf(checkMicrophonePermission(context)) }
    var isRecording by remember { mutableStateOf(false) } // Track recording status

    LaunchedEffect(Unit) {
        viewModel.getAvailableLanguages(context)
    }

    val isSuccess = remember { mutableStateOf(false) }
    val result = remember { mutableStateOf("") }
    val userData = viewModel.userData

    val isLoading = viewModel.loadingInApp
    val availableLanguages = viewModel.availableLanguages
    var expandedFrom by remember { mutableStateOf(false) }
    var expandedTo by remember { mutableStateOf(false) }
    var selectedFromLanguage by remember {
        mutableStateOf(
            availableLanguages.value?.get(0)?.name ?: ""
        )
    }
    val username = remember { mutableStateOf(userData.value.name)}
    val email = remember { mutableStateOf(userData.value.email) }
    var selectedToLanguage by remember { mutableStateOf("") }
    val filteredLanguages =
        availableLanguages.value?.filter { it.name != "AUTO_DETECT" } ?: emptyList()

    if (!hasMicrophonePermission) {
        requestMicrophonePermission(activity)
    }

    LaunchedEffect(isSuccess.value) {
        if (isSuccess.value) {
            navController.navigate(
                AppNavComp.Result.createRoute(
                    textToTranslate,
                    result.value,
                    selectedFromLanguage,
                    selectedToLanguage
                )
            )
        }
    }
    ModalDrawer(
        modifier = Modifier.statusBarsPadding(),
        drawerState = drawerState,
        drawerContent = {
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)) {
                Text(
                    "Translator",
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Divider()
                Spacer(Modifier.height(30.dp))
                Column(modifier = Modifier.fillMaxSize(),horizontalAlignment = Alignment.CenterHorizontally){
                    if(userData.value.profilePicture.isNotEmpty()){
                      AsyncImage(model = userData.value.profilePicture, contentDescription = "profile",
                          modifier = Modifier
                              .size(120.dp)
                              .clip(CircleShape)
                              .clickable {
                                  if (checkCameraPermission(context)) {
                                      cameraLauncher.launch(null)
                                  } else {
                                      requestCameraPermission(activity)
                                  }
                              })
                    }else{
                        Image(painter = painterResource(id = R.drawable.boy), contentDescription = null,
                            modifier = Modifier
                                .size(120.dp)
                                .clickable {
                                    if (checkCameraPermission(context)) {
                                        cameraLauncher.launch(null)
                                    } else {
                                        requestCameraPermission(activity)
                                    }
                                })
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    OutlinedTextField(value = username.value, onValueChange = {username.value = it}, singleLine = true,
                        textStyle = MaterialTheme.typography.body1,
                        leadingIcon = { Icon(imageVector = Icons.Outlined.Person, contentDescription = null) }, trailingIcon = {
                        Icon(imageVector = Icons.Outlined.Edit, contentDescription = null)
                    },
                        colors = TextFieldDefaults.textFieldColors(
                            unfocusedLabelColor = Color.Transparent,
                            focusedLabelColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            backgroundColor = Color.Transparent
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = email.value, onValueChange = {email.value = it}, singleLine = true,
                        textStyle = MaterialTheme.typography.body1,
                        leadingIcon = { Icon(imageVector = Icons.Outlined.Email, contentDescription = null) }, trailingIcon = {
                        Icon(imageVector = Icons.Outlined.Edit, contentDescription = null)
                    },
                        colors = TextFieldDefaults.textFieldColors(
                            unfocusedLabelColor = Color.Transparent,
                            focusedLabelColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            backgroundColor = Color.Transparent
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { /*TODO*/ }, modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(text = "Saved Translations")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { viewModel.updateUserData(username.value,email.value,context) }, modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(text = "Save")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { viewModel.logOut()
                                     navController.navigate(AppNavComp.Login.destination){
                                         popUpTo(0)
                                     }}, modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(text = "Log Out")
                    }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Translate") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            }
        ) { paddingValues ->
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)) {
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
                                                    isRecording =
                                                        false // Reset after getting result
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
                        CircularProgressIndicator(
                            color = colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
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
                        Text(
                            text = "To:    ",
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
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
                        viewModel.translateToData(
                            context = context,
                            textToTranslate,
                            selectedFromLanguage,
                            selectedToLanguage,
                            onSuccessful = {
                                Toast.makeText(
                                    context,
                                    "Translation Successful",
                                    Toast.LENGTH_SHORT
                                ).show()
                                Log.d("Navigation1", "Navigation called in the lambda")
                                result.value = it
                                Log.d("Result", result.value)
                                isSuccess.value = true
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
                if (isLoading.value) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.7f)),
                        contentAlignment = Alignment.Center
                    ) {
                        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                    }
                }
            }
        }
    }
}

private fun checkCameraPermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
}

private fun requestCameraPermission(activity: Activity) {
    ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION_CODE)
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
