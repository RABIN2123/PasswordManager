package com.example.passwordmanager.ui.note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.passwordmanager.data.Note
import com.example.passwordmanager.data.NoteDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class NoteViewModel(private val noteDao: NoteDao) : ViewModel() {
    private val _notesFlow = noteDao.getNotesFlow()
    private val _uiState = MutableStateFlow(NoteState())
    val state = combine(_uiState, _notesFlow) { state, notes ->
        state.copy(
            notes = notes
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NoteState())

    fun onEvent(event: NoteEvent) {
        when (event) {
            NoteEvent.SaveNote -> {
                val note = Note(
                    appName = state.value.appName,
                    password = state.value.appPassword
                )
                viewModelScope.launch(Dispatchers.IO) {
                    noteDao.upsertNote(note)
                }
                _uiState.update {
                    it.copy(
                        stateApply = false,
                        appName = "",
                        appPassword = ""
                    )
                }
            }

            is NoteEvent.UpdateNote -> {
                viewModelScope.launch(Dispatchers.IO) {
                    noteDao.upsertNote(event.note)
                }
                _uiState.update {
                    it.copy()
                }
            }

            is NoteEvent.ShowDialog -> {
                val changeNote = state.value.notes.find { it.id == event.note.id }
                // TODO пофиксить костыль//
                changeNote!!.stateDialog = !changeNote.stateDialog
                _uiState.update {
                    it.copy(
                        notes = state.value.notes.map { note ->
                            if (note.id == event.note.id)
                                changeNote
                            else note
                        },
                        testState = !it.testState
                    )
                }
            }

            is NoteEvent.DeleteNote -> {
                viewModelScope.launch(Dispatchers.IO) {
                    noteDao.deleteNote(event.note)
                }
                _uiState.update {
                    it.copy(
                    )
                }
            }

            is NoteEvent.SetAppName -> {
                _uiState.update {
                    it.copy(
                        appName = event.appName.trim()
                    )
                }
            }

            is NoteEvent.SetPassword -> {
                _uiState.update {
                    it.copy(
                        appPassword = event.password.trim()
                    )
                }
            }

            NoteEvent.SetApply -> {
                _uiState.update {
                    val name = state.value.appName
                    val password = state.value.appPassword
                    it.copy(
                        stateApply = (name.isNotBlank() && password.isNotBlank())
                    )
                }

            }
        }
    }


}

