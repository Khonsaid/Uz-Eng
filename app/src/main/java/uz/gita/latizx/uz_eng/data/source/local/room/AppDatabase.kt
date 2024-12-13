package uz.gita.latizx.uz_eng.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import uz.gita.latizx.uz_eng.data.source.local.room.converter.Converters
import uz.gita.latizx.uz_eng.data.source.local.room.dao.DictionaryDao
import uz.gita.latizx.uz_eng.data.source.local.room.dao.HistoryDao
import uz.gita.latizx.uz_eng.data.source.local.room.dao.WordDetailDao
import uz.gita.latizx.uz_eng.data.source.local.room.entity.DictionaryEntity
import uz.gita.latizx.uz_eng.data.source.local.room.entity.HistoryEntity
import uz.gita.latizx.uz_eng.data.source.local.room.entity.WordDetailEntity

@Database(entities = [DictionaryEntity::class, HistoryEntity::class, WordDetailEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getDictionaryDao(): DictionaryDao
    abstract fun getHistoryDao(): HistoryDao
    abstract fun getWordDetailDao(): WordDetailDao
}