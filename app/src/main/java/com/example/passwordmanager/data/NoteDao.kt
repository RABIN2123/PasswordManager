package com.example.passwordmanager.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert


import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Upsert
    suspend fun insertNote(note: NoteEntity)
    @Update
    suspend fun updateNote(note: NoteEntity)

    @Delete
    suspend fun deleteNote(note: NoteEntity)

    @Query("SELECT * FROM ${NoteEntity.TABLE_NAME} ORDER BY ${NoteEntity.COLUMN_APP_NAME} ASC")
    fun getNotesFlow(): Flow<List<NoteEntity>>
}

interface NoteRepository {
    suspend fun insertNote(note: NoteEntity)
    suspend fun updateNote(note: NoteEntity)
    suspend fun deleteNote(note: NoteEntity)
    fun getNotesFlow(): Flow<List<NoteEntity>>
}