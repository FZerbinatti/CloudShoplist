package com.example.cloudshoplist.ViewModel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.dreamsphere.sharedshoplistk.models.ShopListItem
import com.dreamsphere.sharedshoplistk.repository.Firebase.Firebase
import com.dreamsphere.sharedshoplistk.repository.Room.IdItem
import com.dreamsphere.sharedshoplistk.repository.Room.IdRepository
import com.example.cloudshoplist.View.getRandomString
import com.example.cloudshoplist.repository.SharedPreferencesUtils.SharedPreferenceStringLiveData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val repository: IdRepository) : ViewModel() {
    private val TAG = "MainViewModel"

    val firebase = Firebase()
    var idSize: Int? = null
    var shoplist_ID = "a1a2a3"

    private val _viewState = MutableStateFlow<String>(shoplist_ID)
    val viewState: StateFlow<String> = _viewState

    init {
        Log.d(TAG, "INIT VIEWMODEL: ")
        repository.allIdItems().observeForever(Observer {it
            idSize = it.size
            Log.d(TAG, "it? : " + idSize)
            //
            if (idSize==0){
                shoplist_ID = getRandomString(10)
                _viewState.value = shoplist_ID
                insert(IdItem(shoplist_ID))

            }else{
                //se il numero di item è 2, cancella il [0]
                Log.d(TAG, "it? : "+ it.get(idSize!!-1).spesa_id)
                _viewState.value = it.get(idSize!!-1).spesa_id
                if (idSize!!>1){
                    for (i in 0..idSize!!-2){
                        delete(it.get(i))
                    }
                }
            }
        })
    }

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
        if (shopList.isEmpty()) {
            shopList.add(ShopListItem(1, titleText, checked))
            firebase.addValueFirebase(ShopListItem(1, titleText, checked))
        } else {
            shopList.add(ShopListItem(shopList.last().id + 1, titleText, checked))
            firebase.addValueFirebase(ShopListItem(shopList.last().id + 1, titleText, checked))
        }
        // add record on firebase
        //firebase_shoplists_ids.
    }
    fun removeRecord(todoItem: ShopListItem) {
        val index = shopList.indexOf(todoItem)
        shopList.remove(shopList[index])
    }
    fun insert(item: IdItem) = GlobalScope.launch {
        Log.d(TAG, "insert: id: "+item.id+ " code: "+item.spesa_id)
        repository.insert(item)
    }
    fun delete(item: IdItem) = GlobalScope.launch {
        repository.delete(item)
    }
    fun allIdItems() = repository.allIdItems()

}