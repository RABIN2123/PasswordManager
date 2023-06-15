package com.example.passwordmanager.ui.note

import com.example.passwordmanager.data.Note


data class NoteState (
    val notes: List<Note> = emptyList(),
    val appName: String = "",
    val appPassword: String = "",
    val stateApply: Boolean = false,
    val testState: Boolean = false
)