package com.example.passwordmanager.ui.note

import com.example.passwordmanager.data.Note

sealed interface NoteEvent {
    object SaveNote : NoteEvent
    object SetApply : NoteEvent
    data class ShowDialog(val note: Note) : NoteEvent
    data class SetAppName(val appName: String) : NoteEvent
    data class SetPassword(val password: String) : NoteEvent
    data class DeleteNote(val note: Note) : NoteEvent
    data class UpdateNote(val note: Note) : NoteEvent
}
