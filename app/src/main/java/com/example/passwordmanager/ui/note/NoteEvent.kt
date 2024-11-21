package com.example.passwordmanager.ui.note

import com.example.passwordmanager.data.NoteEntity
import com.example.passwordmanager.data.NoteLocal

sealed interface NoteEvent {
    object SaveNote : NoteEvent
    object SetApply : NoteEvent
    class ShowDialog(val note: NoteLocal) : NoteEvent
    class SetAppName(val appName: String) : NoteEvent
    class SetPassword(val password: String) : NoteEvent
    class DeleteNote(val note: NoteLocal) : NoteEvent
    class UpdateNote(val note: NoteEntity) : NoteEvent
}
