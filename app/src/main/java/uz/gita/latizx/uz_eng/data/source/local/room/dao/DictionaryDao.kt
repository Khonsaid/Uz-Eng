package uz.gita.latizx.uz_eng.data.source.local.room.dao

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Query

@Dao
interface DictionaryDao {
    @Query("SELECT * FROM dictionary")
    fun getAllEng(): Cursor

    @Query("SELECT * FROM dictionary ORDER BY uzbek")
    fun getAllUzb(): Cursor

    @Query("SELECT * FROM dictionary WHERE dictionary.english LIKE '%' || :word || '%'")
    fun searchByEng(word: String): Cursor

    @Query("SELECT * FROM dictionary WHERE dictionary.uzbek LIKE '%' || :word || '%'")
    fun searchByUzb(word: String): Cursor

    @Query("SELECT * FROM dictionary WHERE dictionary.is_favourite = 1")
    fun getAllFavourite(): Cursor

    @Query("UPDATE dictionary SET is_favourite = :fav WHERE id = :id")
    fun updateFav(id: Int, fav: Int)
}