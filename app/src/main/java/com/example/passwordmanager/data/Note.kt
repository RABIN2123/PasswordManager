package com.example.passwordmanager.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = Note.TABLE_NAME)
data class Note(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_ID) val id: Int = 0,
    @ColumnInfo(name = COLUMN_APP_NAME) val appName: String,
    @ColumnInfo(name = COLUMN_PASSWORD) val password: String,
) {

    companion object {
        const val TABLE_NAME = "note"
        const val COLUMN_ID = "id"
        const val COLUMN_APP_NAME = "app_name"
        const val COLUMN_PASSWORD = "password"
    }

}

data class NoteLocal(
    val id: Int = 0,
    val appName: String,
    val password: String,
    var stateDialog: Boolean = false
)



