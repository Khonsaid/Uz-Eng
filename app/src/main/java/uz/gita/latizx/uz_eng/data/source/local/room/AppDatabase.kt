package uz.gita.latizx.uz_eng.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import uz.gita.latizx.uz_eng.data.source.local.room.dao.DictionaryDao
import uz.gita.latizx.uz_eng.data.source.local.room.dao.HistoryDao
import uz.gita.latizx.uz_eng.data.source.local.room.entity.DictionaryEntity
import uz.gita.latizx.uz_eng.data.source.local.room.entity.HistoryEntity

@Database(entities = [DictionaryEntity::class, HistoryEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getDictionaryDao(): DictionaryDao
    abstract fun getHistoryDao(): HistoryDao

}