package com.dreamsphere.sharedshoplistk.repository.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [IdItem::class], version = 1)
abstract class IdDatabase : RoomDatabase() {

    abstract fun getShopListIdDao(): ShoplistIdDao

    companion object {
        @Volatile
        private var instance: IdDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also {
                instance = it
            }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, IdDatabase::class.java, "IdDatabase.db").build()

    }
}