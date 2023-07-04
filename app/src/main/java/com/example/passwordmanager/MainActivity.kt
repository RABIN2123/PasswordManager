package com.example.passwordmanager

//import android.database.sqlite.SQLiteDatabase
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material.*
import androidx.room.Room
import com.example.passwordmanager.data.NoteDatabase
import com.example.passwordmanager.ui.theme.PasswordManagerTheme
import androidx.compose.runtime.*
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.passwordmanager.data.CryptoManager
import com.example.passwordmanager.navigation.NavigationView
import com.example.passwordmanager.ui.note.NoteViewModel
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory

private const val KEY_ENCRYPTED_TEXT = "password_for_db"

class MainActivity : ComponentActivity() {
    private val prefs by lazy {
        getSharedPreferences("secret_prefs", Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val passphrase: ByteArray = SQLiteDatabase.getBytes(getSecretKey())
        val factory = SupportFactory(passphrase)
        val database =
            Room.databaseBuilder(this@MainActivity, NoteDatabase::class.java, "database.db")
                .openHelperFactory(factory).build()
        val noteDao = database.dao
        val viewModel by viewModels<NoteViewModel>(
            factoryProducer = {
                object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return NoteViewModel(noteDao) as T
                    }
                }
            }
        )
        super.onCreate(savedInstanceState)
        setContent {
            PasswordManagerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val state by viewModel.state.collectAsState()
                    NavigationView(state, viewModel::onEvent)
                }
            }
        }
    }

    private fun getSecretKey(): CharArray {
        var savedKey = prefs.getString("Key", null) ?: ""
        val cryptoManager = CryptoManager.create(this)
        if(savedKey.isEmpty()) {
            savedKey = cryptoManager.createPassword()
            prefs.edit {
                putString("Key", savedKey)
            }
        }
        return cryptoManager.decrypt(savedKey).toCharArray()
    }
}

@Preview(showSystemUi = true)
@Composable
fun Preview() {
    PasswordManagerTheme {

    }
}