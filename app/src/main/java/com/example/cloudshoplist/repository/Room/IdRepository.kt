package com.dreamsphere.sharedshoplistk.repository.Room



class IdRepository(private val db: IdDatabase) {

    suspend fun insert(item: IdItem) = db.getShopListIdDao().insert(item)
    suspend fun delete(item: IdItem) = db.getShopListIdDao().delete(item)


    fun allIdItems() = db.getShopListIdDao().getAllIds()

    suspend fun numberOfIds()= db.getShopListIdDao().countAllIds()

    //fun getFirstId() = db.getShopListIdDao().retrieveFirstID()

     //suspend fun getAnId() = db.getShopListIdDao().getAnId()

}