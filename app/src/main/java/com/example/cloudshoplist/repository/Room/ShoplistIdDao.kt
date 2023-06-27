package com.dreamsphere.sharedshoplistk.repository.Room

import android.util.Log
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.room.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope

@Dao
interface ShoplistIdDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item:IdItem)

    @Delete
    suspend fun delete(item: IdItem)

    @Query("SELECT * FROM table_shiplist_id")
     fun getAllIds(): LiveData<List<IdItem>>

    @Query("SELECT COUNT(*) FROM table_shiplist_id")
    suspend fun countAllIds(): Int

}