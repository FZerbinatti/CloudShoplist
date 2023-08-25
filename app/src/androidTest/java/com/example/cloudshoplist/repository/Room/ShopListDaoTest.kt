package com.example.cloudshoplist.repository.Room

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.androiddevs.shoppinglisttestingyt.getOrAwaitValue
import com.dreamsphere.sharedshoplistk.repository.Room.IdDatabase
import com.dreamsphere.sharedshoplistk.repository.Room.IdItem
import com.dreamsphere.sharedshoplistk.repository.Room.ShoplistIdDao
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class ShopListDaoTest {

    private lateinit var database: IdDatabase
    private lateinit var dao: ShoplistIdDao

    @get:Rule
    var instantTaskExecutorRule= InstantTaskExecutorRule()

    @Before
    fun setup(){
        database = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), IdDatabase::class.java).allowMainThreadQueries().build()
        dao = database.getShopListIdDao()
    }

    @After
    fun closeDB(){
        database.close()
    }

    @Test
    fun insertIDintoDB()= runBlockingTest {
        val id_Item = IdItem("AAA111",1)
        dao.insert(id_Item)
        val allIdITems = dao.getAllIds().getOrAwaitValue()
        assertThat(allIdITems).contains(id_Item)
    }

    @Test
    fun deleteIDintoDB()= runBlockingTest {
        val id_Item = IdItem("AAA111",1)
        dao.insert(id_Item)
        dao.delete(id_Item)


        val allIdITems = dao.getAllIds().getOrAwaitValue()
        assertThat(allIdITems).doesNotContain(id_Item)
    }

}