package com.example.passwordmanager.navigation

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.passwordmanager.InterfaceAddMenu
import com.example.passwordmanager.NoteList
import com.example.passwordmanager.R
import com.example.passwordmanager.ui.note.NoteEvent
import com.example.passwordmanager.ui.note.NoteState


enum class Screen(@StringRes val title: Int) {
    Home(R.string.app_name),
    Add(R.string.input_note)
}

@Composable
fun PasswordManagerAppBar(
    title: Screen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    state: NoteState,
    applyClick: () -> Unit
) {
    if (!canNavigateBack) {
        TopAppBar(
            title = { Text(text = stringResource(id = title.title)) },
        )
    } else {
        TopAppBar(
            title = {
                Text(text = stringResource(id = title.title))
            },
            navigationIcon = {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }, actions = {
                IconButton(onClick = applyClick, enabled = state.stateApply) {
                    Icon(
                        imageVector = Icons.Filled.Done,
                        contentDescription = stringResource(id = R.string.done)
                    )
                }
            }
        )
    }

}

@Composable
fun NavigationView(state: NoteState, onEvent: (NoteEvent) -> Unit) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = Screen.valueOf(
        backStackEntry?.destination?.route ?: Screen.Home.name
    )

    Scaffold(topBar = {
        PasswordManagerAppBar(
            title = currentScreen,
            canNavigateBack = navController.previousBackStackEntry != null,
            navigateUp = navController::navigateUp,
            state = state
        ) {
            onEvent(NoteEvent.SaveNote)
            navController.popBackStack(Screen.Home.name, inclusive = false)
        }
    }) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.name) {
                NoteList(
                    noteState = state.notes,
                    navController = navController,
                    onEvent = onEvent
                )
            }
            composable(Screen.Add.name) {
                InterfaceAddMenu(state, onEvent)
            }
        }
    }

}
