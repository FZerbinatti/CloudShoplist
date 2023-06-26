package com.example.cloudshoplist.ViewModel
import androidx.compose.runtime.mutableStateListOf
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import com.dreamsphere.sharedshoplistk.models.ShopListItem
import com.dreamsphere.sharedshoplistk.repository.Firebase.Firebase
import com.dreamsphere.sharedshoplistk.repository.Room.IdItem
import com.dreamsphere.sharedshoplistk.repository.Room.IdRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val repository: IdRepository) : ViewModel() {

    //, private val shoplist_id: String
    val firebase = Firebase()
    val shoplistID = String()
    private var shopList1 = mutableStateListOf<ShopListItem>()
    private val SHOPLIST_ID = stringPreferencesKey("shoplist_id")

    //return to Room



    private var shopList = mutableStateListOf(
        ShopListItem(0, "My First Task", false),
        ShopListItem(1, "My Second Task", true),
        ShopListItem(2, "My Third Task", true),
        //https://www.youtube.com/watch?v=J4QywOWZdoo
    )




    private val _shopListFlow = MutableStateFlow(shopList)






    val shopListFlow: StateFlow<List<ShopListItem>> get() = _shopListFlow

    fun setChecked(index: Int, value: Boolean) {
        shopList[index] = shopList[index].copy(item_checked = value)
    }

    public fun addRecord(titleText: String, checked: Boolean) {
        if(shopList.isEmpty()){
            shopList.add(ShopListItem(1,titleText ,checked))
            firebase.addValueFirebase(ShopListItem(1,titleText ,checked))
        }else{
            shopList.add(ShopListItem(shopList.last().id +1,titleText ,checked))
            firebase.addValueFirebase(ShopListItem(shopList.last().id +1,titleText ,checked))


        }

        // add record on firebase
        //firebase_shoplists_ids.
    }

    fun removeRecord(todoItem: ShopListItem) {
        val index = shopList.indexOf(todoItem)
        shopList.remove(shopList[index])
    }


    // In coroutines thread insert item in insert function.
    fun insert(item: IdItem) = GlobalScope.launch {
        repository.insert(item)
    }

    // In coroutines thread delete item in delete function.
    fun delete(item: IdItem) = GlobalScope.launch {
        repository.delete(item)
    }

    //Here we initialized allGroceryItems function with repository
    fun allIdItems() = repository.allIdItems()

    //Number of Ids in the database


    //suspend fun getIdFromRoom(): String = repository.getAnId()



}