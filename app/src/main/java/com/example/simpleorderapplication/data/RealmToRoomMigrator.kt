package com.example.simpleorderapplication.data

import android.content.Context
import androidx.core.content.edit
import androidx.room.withTransaction
import com.example.simpleorderapplication.data.models.OrderWithProductsEntity
import com.example.simpleorderapplication.data.models.RealmOrder
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

class RealmToRoomMigrator(
    context: Context,
    private val realm: Realm,
    private val database: AppDatabase
) {
    private val preferences = context.applicationContext
        .getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    private val mutex = Mutex()

    suspend fun migrateIfNeeded() = mutex.withLock {
        if (preferences.getBoolean(MIGRATION_COMPLETE_KEY, false)) return@withLock

        withContext(Dispatchers.IO) {
            val roomOrders = realm.query<RealmOrder>().find().map{ OrderWithProductsEntity.fromDomain(it.toDomain()) }

            database.withTransaction {
                val dao = database.ordersDao()
                roomOrders.forEach { dao.save(it) }
            }

            preferences.edit(commit = true) { putBoolean(MIGRATION_COMPLETE_KEY, true) }

            check(preferences.getBoolean(MIGRATION_COMPLETE_KEY, false)) {
                "Não foi possível salvar o estado da migração do Realm para o Room"
            }
        }
    }

    private companion object {
        const val PREFERENCES_NAME = "realm_to_room_migration"
        const val MIGRATION_COMPLETE_KEY = "orders_migrated"
    }
}
