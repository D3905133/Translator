package uk.ac.tees.mad.tt.data.dependencies

import android.content.Context
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import uk.ac.tees.mad.tt.data.local.TranslatedDatabase
import uk.ac.tees.mad.tt.data.local.TranslatedItemsDao

@Module
@InstallIn(SingletonComponent::class)
object TranslatorModule {

    @Provides
    fun providesFirebaseAuthentication() : FirebaseAuth = Firebase.auth

    @Provides
    fun providesFirebaseFirestore() : FirebaseFirestore = Firebase.firestore

    @Provides
    fun providesFirebaseStorage() : FirebaseStorage = Firebase.storage

    @Provides
    fun provideTranslatedDatabase(@ApplicationContext context: Context): TranslatedDatabase {
        return Room.databaseBuilder(
            context,
            TranslatedDatabase::class.java,
            "translated_database"
            ).build()
    }

    @Provides
    fun provideTranslatedDao(database: TranslatedDatabase): TranslatedItemsDao {
        return database.translatedItemsDao()
    }

}