package com.example.passwordmanager

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.passwordmanager.data.NoteLocal
import com.example.passwordmanager.navigation.Screen
import com.example.passwordmanager.ui.note.NoteEvent

@Composable
fun NoteList(
    noteState: List<NoteLocal>,
    navController: NavHostController,
    onEvent: (NoteEvent) -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(columns = GridCells.Fixed(2)) {
            items(noteState) { note ->
                NoteCard(note = note, onEvent = onEvent)
            }
        }
        IconButton(
            onClick = { navController.navigate(Screen.Add.name) }, modifier = Modifier
                .size(70.dp)
                .align(Alignment.BottomEnd)
        ) {
            Icon(painterResource(R.drawable.baseline_add_box_24), contentDescription = null)
        }
    }
}

@Composable
fun NoteCard(modifier: Modifier = Modifier, note: NoteLocal, onEvent: (NoteEvent) -> Unit) {
    var blur by remember { mutableStateOf(8.dp) }
    Card(
        modifier = modifier
            .padding(8.dp)
            .pointerInput(note.id) {
                detectTapGestures(
                    onLongPress = {
                        onEvent(NoteEvent.ShowDialog(note))
                    },
                    onTap = {
                        blur = if (blur == 8.dp) {
                            0.dp
                        } else {
                            8.dp
                        }
                    }
                )
            }, elevation = 4.dp
    ) {
        if (note.stateDialog) {
            FunctionDialog(
                note = note,
                onEvent = onEvent
            )
        }
        Column(modifier = modifier.padding(4.dp)) {
            Text(text = note.appName, style = MaterialTheme.typography.h5)
            Text(
                text = note.password,
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.blur(
                    blur,
                    edgeTreatment = BlurredEdgeTreatment.Rectangle
                )
            )
        }
    }
}