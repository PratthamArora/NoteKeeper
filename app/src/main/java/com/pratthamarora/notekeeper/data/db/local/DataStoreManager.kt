package com.pratthamarora.notekeeper.data.db.local

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.emptyPreferences
import androidx.datastore.preferences.preferencesKey
import com.pratthamarora.notekeeper.data.models.UserPrefs
import com.pratthamarora.notekeeper.utils.SortOrder
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreManager @Inject constructor(@ApplicationContext context: Context) {

    private val dataStore = context.createDataStore("user_prefs")
    val preferences = dataStore.data
        .catch { ex ->
            if (ex is IOException) {
                Log.d(DataStoreManager::class.java.simpleName, "error is $ex ")
                emit(emptyPreferences())
            } else {
                throw ex
            }
        }
        .map { prefs ->
            val sortOrder =
                SortOrder.valueOf(prefs[PreferencesKeys.SORT_ORDER] ?: SortOrder.BY_DATE.name)
            val isCompleted = prefs[PreferencesKeys.IS_COMPLETED] ?: false
            UserPrefs(sortOrder = sortOrder, isCompleted = isCompleted)
        }

    suspend fun setSortOrder(sortOrder: SortOrder) {
        dataStore.edit {
            it[PreferencesKeys.SORT_ORDER] = sortOrder.name
        }
    }

    suspend fun setIsCompleted(isCompleted: Boolean) {
        dataStore.edit {
            it[PreferencesKeys.IS_COMPLETED] = isCompleted
        }
    }

    private object PreferencesKeys {
        val SORT_ORDER = preferencesKey<String>("sort_order")
        val IS_COMPLETED = preferencesKey<Boolean>("is_completed")
    }
}