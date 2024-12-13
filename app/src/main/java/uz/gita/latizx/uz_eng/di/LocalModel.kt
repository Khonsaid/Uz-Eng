package uz.gita.latizx.uz_eng.di

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import uz.gita.latizx.uz_eng.data.source.local.room.AppDatabase
import uz.gita.latizx.uz_eng.data.source.local.room.converter.Converters
import uz.gita.latizx.uz_eng.util.GsonParser
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalModel {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase = Room
        .databaseBuilder(context, AppDatabase::class.java, "Dictionary.db")
        .addTypeConverter(Converters(GsonParser(Gson())))
        .createFromAsset("dictionary.db")
        .build()

}