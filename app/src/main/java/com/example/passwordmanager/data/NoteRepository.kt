package com.example.passwordmanager.data

import kotlinx.coroutines.flow.Flow

/*Impl предложил бот, второй вариант NoteReposity тут,
 интерфейс NoteDaoRepository */
class NoteRepositoryImpl(private val noteDao: NoteDao): NoteRepository {
    override suspend fun insertNote(note: Note) {
        noteDao.insertNote(note)
    }

    override suspend fun updateNote(note: Note) {
        noteDao.updateNote(note)
    }

    override suspend fun deleteNote(note: Note) {
        noteDao.deleteNote(note)
    }

    override fun getNotesFlow(): Flow<List<Note>> {
        return noteDao.getNotesFlow()
    }

}