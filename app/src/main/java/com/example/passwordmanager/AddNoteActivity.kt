package com.example.passwordmanager

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.passwordmanager.ui.note.NoteEvent
import com.example.passwordmanager.ui.note.NoteState

@Composable
fun InterfaceAddMenu(state: NoteState, onEvent: (NoteEvent) -> Unit, applyClick: () -> Unit) {
    val focusMananger = LocalFocusManager.current
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        //Input name app
        InputField(
            state.appName,
            label = R.string.label_app_name,
            placeholder = R.string.input_app_name,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusMananger.moveFocus(FocusDirection.Down) }
            )
        ) {newName -> onEvent(NoteEvent.SetAppName(newName))}
        //Input password app
        InputField(
            state.appPassword,

            label = R.string.label_app_password,
            placeholder = R.string.input_password,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusMananger.clearFocus()
                    applyClick()
                }
            )
        ) {newPassword -> onEvent(NoteEvent.SetPassword(newPassword)) }
        onEvent(NoteEvent.SetApply)
    }

}


@Composable
fun InputField(
    value: String,
    label: Int,
    placeholder: Int,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
    onValueChange: (String) -> Unit
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(stringResource(id = label)) },
        placeholder = { Text(stringResource(id = placeholder)) },
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        modifier = Modifier.padding(top = 15.dp)
    )
}

