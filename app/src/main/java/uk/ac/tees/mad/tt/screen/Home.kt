package uk.ac.tees.mad.tt.screen

import android.content.ClipboardManager
import android.content.Context
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
import androidx.navigation.NavHostController
import uk.ac.tees.mad.tt.TranlatorViewmodel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Home(navController: NavHostController, viewModel: TranlatorViewmodel) {
    val context = LocalContext.current
    var textToTranslate by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.getAvailableLanguages(context)
    }

    val availableLanguages = viewModel.availableLanguages
    var expandedFrom by remember { mutableStateOf(false) }
    var expandedTo by remember { mutableStateOf(false) }
    var selectedFromLanguage by remember { mutableStateOf(availableLanguages.value?.get(0)?.name ?: "") }
    var selectedToLanguage by remember { mutableStateOf("") }

    val filteredLanguages = availableLanguages.value?.filter { it.name != "AUTO_DETECT" } ?: emptyList()

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
                    Icon(imageVector = Icons.Outlined.Mic, contentDescription = null)
                }
            },
            label = { Text(text = "Write/Paste Text to Translate") },
            shape = RoundedCornerShape(20.dp),
            singleLine = false,
            minLines = 10,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp)
        )

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)) {
            Text(text = "From:", modifier = Modifier.align(Alignment.CenterVertically))
            ExposedDropdownMenuBox(expanded = expandedFrom, onExpandedChange = { expandedFrom = !expandedFrom }) {
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

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)) {
            Text(text = "To:    ", modifier = Modifier.align(Alignment.CenterVertically))
            ExposedDropdownMenuBox(expanded = expandedTo, onExpandedChange = { expandedTo = !expandedTo }) {
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
            viewModel.translateToData(context = context, textToTranslate)
        }) {
            Text(text = "Translate")
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
