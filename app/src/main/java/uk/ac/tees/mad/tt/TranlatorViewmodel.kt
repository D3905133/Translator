package uk.ac.tees.mad.tt

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import com.easy.translator.EasyTranslator
import com.easy.translator.LanguagesModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import uk.ac.tees.mad.tt.models.User
import javax.inject.Inject

@HiltViewModel
class TranlatorViewmodel @Inject constructor(
    private val authentication: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
) : ViewModel() {

    val loadingInApp = mutableStateOf(false)
    val userData = mutableStateOf(User())
    val loggedIn = mutableStateOf(false)
    val availableLanguages = mutableStateOf<List<LanguagesModel>?>(null)

    init {
        if (authentication.currentUser != null) {
            retrieveUserData()
            loggedIn.value = true
        }
    }

    fun signUp(context: Context, name: String, email: String, password: String) {
        loadingInApp.value = true
        authentication.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
            val user = User(name = name, email = email, password = password)
            firestore.collection("users").document(it.user!!.uid).set(user).addOnSuccessListener {
                retrieveUserData()
                loadingInApp.value = false
                loggedIn.value = true
            }.addOnFailureListener {
                loadingInApp.value = false
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun logIn(context: Context, email: String, password: String) {
        loadingInApp.value = true
        authentication.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            retrieveUserData()
            loadingInApp.value = false
            loggedIn.value = true
        }.addOnFailureListener {
            loadingInApp.value = false
            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
        }
    }


    fun retrieveUserData() {
        val uid = authentication.currentUser?.uid
        Log.d("TAG", "retrieveUserData: $uid")
        if (uid != null) {
            firestore.collection("users").document(uid).get()
                .addOnSuccessListener {
                    val user = it.toObject(User::class.java)!!
                    Log.d("User", "retrieveUserData: $user")
                }.addOnFailureListener {
                    Log.d("TAG", "retrieveUserData: ${it.message}")
                }
        }
    }

    fun getAvailableLanguages(context: Context){
        val translate = EasyTranslator(context)
        availableLanguages.value = translate.getLanguagesList()
        Log.d("Fetched Available Languages", "getAvailableLanguages: ${availableLanguages.value}")
    }

    fun getLanguageEnum(language: String): LanguagesModel? {
        return LanguagesModel.values().find {
            it.langName.equals(language, ignoreCase = true) || it.shortCode.equals(language, ignoreCase = true)
        }
    }

    fun translateToData(context: Context, text: String, from: String, to: String, onSuccessful: (String) -> Unit) {
        loadingInApp.value = true
        val translate = EasyTranslator(context)
        Log.d("TAG", "translateToData: $text, $from, $to")

        val fromLang = getLanguageEnum(from) ?: LanguagesModel.AUTO_DETECT
        val toLang = getLanguageEnum(to) ?: LanguagesModel.HINDI

        translate.translate(
            text = text,
            fromLang,
            toLang,
            { result ->
                loadingInApp.value = false
                Log.d("TAG", "translateToData: $result")
                onSuccessful(result)
            },
            { error ->
                loadingInApp.value = false
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            }
        )
    }


}