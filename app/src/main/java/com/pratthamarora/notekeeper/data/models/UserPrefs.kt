package com.pratthamarora.notekeeper.data.models

import com.pratthamarora.notekeeper.utils.SortOrder

data class UserPrefs(
    val sortOrder: SortOrder,
    val isCompleted: Boolean
)
