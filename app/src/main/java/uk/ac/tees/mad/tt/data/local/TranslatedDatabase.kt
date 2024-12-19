package uk.ac.tees.mad.tt.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import uk.ac.tees.mad.tt.models.TranslatedItem

@Database(entities = [TranslatedItem::class], version = 1, exportSchema = false)
abstract class TranslatedDatabase : RoomDatabase() {
    abstract fun translatedItemsDao(): TranslatedItemsDao
}