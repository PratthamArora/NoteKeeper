package com.pratthamarora.notekeeper.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.text.DateFormat

@Entity(tableName = "notes_table")
@Parcelize
data class Notes(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val isImportant: Boolean = false,
    val isCompleted: Boolean = false,
    val dateCreated: Long = System.currentTimeMillis(),
) : Parcelable {
    val dateCreatedFormatted: String
        get() = DateFormat.getDateTimeInstance().format(dateCreated)
}
