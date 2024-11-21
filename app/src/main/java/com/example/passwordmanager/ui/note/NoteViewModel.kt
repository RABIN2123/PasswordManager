package com.example.passwordmanager.ui.note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.passwordmanager.data.NoteEntity
import com.example.passwordmanager.data.NoteDao
import com.example.passwordmanager.data.NoteLocal
import com.example.passwordmanager.data.NoteRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class NoteViewModel(private val noteDao: NoteDao) : ViewModel() {
    private val noteRepository = NoteRepositoryImpl(noteDao)
    private val _notesFlow = noteRepository.getNotesFlow()
    private val _uiState = MutableStateFlow(NoteState())
    val state = combine(_uiState, _notesFlow) { state, notes ->
        state.copy(
            notes = notes
                .map { note ->
                    NoteLocal(
                        note.id, note.appName, note.password,
                        updateDialogInState(state, note))
                }
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NoteState())
    private val updateDialogInState: (NoteState, NoteEntity) -> Boolean = { state, note ->
        val dialogShow = state.notes.find { it.id == note.id }
        dialogShow?.stateDialog ?: false
    }

    fun onEvent(event: NoteEvent) {
        when (event) {
            NoteEvent.SaveNote -> {
                val note = NoteEntity(
                    appName = state.value.appName,
                    password = state.value.appPassword
                )
                viewModelScope.launch(Dispatchers.IO) {
                    noteRepository.insertNote(note)
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
                    noteRepository.updateNote(event.note)
                }
            }

            is NoteEvent.ShowDialog -> {
                val changeNote = state.value.notes.find { it.id == event.note.id } ?: NoteLocal(appName = "", password = "")
                _uiState.update {
                    it.copy(
                        notes = state.value.notes.map { note ->
                            if (note.id == event.note.id)
                                changeNote.copy(stateDialog = !changeNote.stateDialog)
                            else note
                        },
                    )
                }
            }

            is NoteEvent.DeleteNote -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val note: NoteEntity
                    with(event.note) {
                        note = NoteEntity(id, appName, password)
                    }
                    noteRepository.deleteNote(note)
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

