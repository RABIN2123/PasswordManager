package com.example.passwordmanager.data

import kotlinx.coroutines.flow.Flow

/*Impl предложил бот, второй вариант NoteReposity тут,
 интерфейс NoteDaoRepository */
class NoteRepositoryImpl(private val noteDao: NoteDao): NoteRepository {
    override suspend fun insertNote(note: NoteEntity) {
        noteDao.insertNote(note)
    }

    override suspend fun updateNote(note: NoteEntity) {
        noteDao.updateNote(note)
    }

    override suspend fun deleteNote(note: NoteEntity) {
        noteDao.deleteNote(note)
    }

    override fun getNotesFlow(): Flow<List<NoteEntity>> {
        return noteDao.getNotesFlow()
    }

}