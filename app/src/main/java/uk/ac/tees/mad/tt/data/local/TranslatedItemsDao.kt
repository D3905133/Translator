package uk.ac.tees.mad.tt.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import uk.ac.tees.mad.tt.models.TranslatedItem

@Dao
interface TranslatedItemsDao {

    @Query("SELECT * FROM translated_items")
    suspend fun getAllTranslatedItems(): List<TranslatedItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(translatedItem: TranslatedItem)

    @Delete
    suspend fun delete(item: TranslatedItem)
}