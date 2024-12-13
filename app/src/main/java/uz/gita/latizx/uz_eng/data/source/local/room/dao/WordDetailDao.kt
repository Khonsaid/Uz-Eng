package uz.gita.latizx.uz_eng.data.source.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import uz.gita.latizx.uz_eng.data.source.local.room.entity.WordDetailEntity

@Dao
interface WordDetailDao {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertWordDetail(infos: List<WordDetailEntity>)

    @Query("DELETE FROM WordDetailEntity WHERE word IN(:words)")
    suspend fun deleteWordDetail(words: List<String>)

    @Query("SELECT * FROM WordDetailEntity WHERE word LIKE '%' || :word || '%'")
    suspend fun getWordDetail(word: String): List<WordDetailEntity>
}