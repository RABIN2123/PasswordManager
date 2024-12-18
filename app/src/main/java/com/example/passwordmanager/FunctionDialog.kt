package com.example.passwordmanager

import android.view.Gravity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import com.example.passwordmanager.data.NoteEntity
import com.example.passwordmanager.data.NoteLocal
import com.example.passwordmanager.ui.note.NoteEvent



@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FunctionDialog(
    modifier: Modifier = Modifier,
    note: NoteLocal,
    onEvent: (NoteEvent) -> Unit,

    ) {
    var readOnly by remember { mutableStateOf(true) }
    var appName by remember { mutableStateOf(note.appName) }
    var password by remember { mutableStateOf(note.password) }
    Dialog(
        onDismissRequest = { onEvent(NoteEvent.ShowDialog(note)) },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    )
    {

        Surface(elevation = 4.dp, border = BorderStroke(1.dp, Color.Black)) {
            Column(
                modifier = modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                //Change nameApp
                ChangeField(
                    value = appName,
                    onValueChange = { appName = it },
                    label = R.string.label_app_name,
                    readOnly = readOnly
                )
                //Change password
                ChangeField(
                    value = password,
                    onValueChange = { password = it },
                    label = R.string.label_app_password,
                    readOnly = readOnly
                )
                Button(onClick = {
                    readOnly = if (readOnly) {
                        !readOnly
                    } else {
                        val changeNote = NoteEntity(note.id, appName, password)
                        onEvent(NoteEvent.UpdateNote(changeNote))
                        !readOnly
                    }
                }, modifier = Modifier.fillMaxWidth()) {
                    Text(text = stringResource(id = if (readOnly) R.string.change else R.string.save))
                }
                Button(
                    onClick = { onEvent(NoteEvent.DeleteNote(note)) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
                ) {
                    Text(text = stringResource(id = R.string.delete))
                }
            }

            val dialogWindow = LocalView.current.parent as DialogWindowProvider
            LaunchedEffect(Unit) {
                with(dialogWindow.window) {
                    setGravity(Gravity.BOTTOM)
                    setDimAmount(0f)
                }
            }

        }
    }

}

@Composable
fun ChangeField(value: String, onValueChange: (String) -> Unit, label: Int, readOnly: Boolean) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(stringResource(id = label)) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        readOnly = readOnly
    )
}
