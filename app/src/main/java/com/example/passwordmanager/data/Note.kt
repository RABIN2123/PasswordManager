package com.example.passwordmanager.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "note")
data class Note(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "app_name") var appName: String,
    @ColumnInfo(name = "password") var password: String,
) {
    @Ignore
    var stateDialog: Boolean = false

    constructor(id: Int, appName: String, password: String, stateDialog: Boolean) : this(
        id,
        appName,
        password
    ) {
        this.stateDialog = stateDialog
    }
}