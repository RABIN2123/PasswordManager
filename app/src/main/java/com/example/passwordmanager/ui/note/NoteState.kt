package com.example.passwordmanager.ui.note

import com.example.passwordmanager.data.NoteRepo


data class NoteState(
    val notes: List<NoteRepo> = emptyList(),
    val appName: String = "",
    val appPassword: String = "",
    val stateApply: Boolean = false,
)