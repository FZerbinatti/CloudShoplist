package com.dreamsphere.sharedshoplistk.repository.Room

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer


class IdRepository(private val db: IdDatabase) {

    suspend fun insert(item: IdItem) = db.getShopListIdDao().insert(item)
    suspend fun delete(item: IdItem) = db.getShopListIdDao().delete(item)
    fun allIdItems() = db.getShopListIdDao().getAllIds()
    suspend fun numberOfIds()= db.getShopListIdDao().countAllIds()

}