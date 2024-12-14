package uk.ac.tees.mad.tt

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import uk.ac.tees.mad.tt.models.User
import javax.inject.Inject

@HiltViewModel
class TranlatorViewmodel @Inject constructor(
    private val authentication : FirebaseAuth,
    private val firestore : FirebaseFirestore,
    private val storage : FirebaseStorage
) : ViewModel() {

    val loadingInApp = mutableStateOf(false)
    init {

    }

    fun signUp(context : Context, name : String, email : String, password : String){
        loadingInApp.value = true
        authentication.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
            val user = User(name = name, email = email, password = password)
        }
    }

}