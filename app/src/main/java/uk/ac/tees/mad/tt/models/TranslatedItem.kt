package uk.ac.tees.mad.tt.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "translated_items")
data class TranslatedItem(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val from: String,
    val result: String,
    val fromLang: String,
    val toLang: String
)
