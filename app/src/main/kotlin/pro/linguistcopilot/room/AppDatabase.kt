package pro.linguistcopilot.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import pro.linguistcopilot.feature.book.room.BookItemDao
import pro.linguistcopilot.feature.book.room.converter.DateConverter
import pro.linguistcopilot.feature.book.room.converter.MetaInfoConverter
import pro.linguistcopilot.feature.book.room.model.BookItemDbModel

@Database(
    entities = [
        BookItemDbModel::class
    ],
    version = 1
)
@TypeConverters(
    DateConverter::class,
    MetaInfoConverter::class
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookItemDao(): BookItemDao

    companion object {
        const val DATABASE_NAME = "linguist-database"
    }
}