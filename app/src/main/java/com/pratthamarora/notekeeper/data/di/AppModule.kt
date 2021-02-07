package com.pratthamarora.notekeeper.data.di

import android.app.Application
import androidx.room.Room
import com.pratthamarora.notekeeper.data.db.NoteDatabase
import com.pratthamarora.notekeeper.data.db.NotesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNoteDatabase(
        @ApplicationContext app: Application
    ): NoteDatabase = Room.databaseBuilder(app, NoteDatabase::class.java, "NotesDatabase")
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    fun provideNoteDao(db: NoteDatabase): NotesDao = db.noteDao()

    @Provides
    @Singleton
    fun provideApplicationScope() = CoroutineScope(SupervisorJob())
}