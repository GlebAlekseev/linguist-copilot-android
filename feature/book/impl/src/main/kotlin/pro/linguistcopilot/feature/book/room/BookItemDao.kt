package pro.linguistcopilot.feature.book.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import pro.linguistcopilot.feature.book.room.model.BookItemDbModel

@Dao
interface BookItemDao {
    @Query("SELECT * FROM BookItemDbModel ORDER BY created_at ASC")
    fun getAllAsFlow(): Flow<List<BookItemDbModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrReplace(bookItemDbModel: BookItemDbModel)
}