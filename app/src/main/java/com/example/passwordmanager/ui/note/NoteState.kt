package com.example.passwordmanager.ui.note

import com.example.passwordmanager.data.NoteLocal


data class NoteState(
    val notes: List<NoteLocal> = emptyList(),
    val appName: String = "",
    val appPassword: String = "",
    val stateApply: Boolean = false,
)