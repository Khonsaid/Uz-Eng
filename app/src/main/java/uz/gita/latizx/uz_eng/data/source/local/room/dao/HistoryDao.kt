package uz.gita.latizx.uz_eng.data.source.local.room.dao

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import uz.gita.latizx.uz_eng.data.source.local.room.entity.HistoryEntity

@Dao
interface HistoryDao {

    @Query("SELECT * FROM HistoryEntity")
    fun getAllHistory() : Cursor

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(historyEntity: HistoryEntity)

    @Query("SELECT * FROM HistoryEntity ORDER BY date DESC LIMIT 10")
    fun getLast10TodosFlow(): Flow<List<HistoryEntity>>
}