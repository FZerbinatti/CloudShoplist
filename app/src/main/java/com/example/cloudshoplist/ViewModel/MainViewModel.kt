package com.example.cloudshoplist.ViewModel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.dreamsphere.sharedshoplistk.models.ShopListItem
import com.dreamsphere.sharedshoplistk.repository.Firebase.Firebase
import com.dreamsphere.sharedshoplistk.repository.Room.IdItem
import com.dreamsphere.sharedshoplistk.repository.Room.IdRepository
import com.example.cloudshoplist.View.getRandomString
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val repository: IdRepository) : ViewModel() {
    private val TAG = "MainViewModel"

    var idSize: Int? = null
    var shoplist_ID = "a1a2a3"

    private val _viewState = MutableStateFlow<String>(shoplist_ID)
    val viewState: StateFlow<String> = _viewState

    private var shopList = mutableStateListOf<ShopListItem>()
    private val _shopListFlow = MutableStateFlow(shopList)
    val shopListFlow: StateFlow<List<ShopListItem>> get() = _shopListFlow

    init {
        Log.d(TAG, "INIT VIEWMODEL: ")
        /*
        repository.fun_first_time().observeForever({
            //Log.d(TAG, ": "+it.get(0).)
        })*/


        repository.allIdItems().observeForever(Observer {it
            idSize = it.size
            Log.d(TAG, "it? : " + idSize)
            //
            if (idSize==0){

                shoplist_ID = getRandomString(10)
                _viewState.value = shoplist_ID
                insert(IdItem(shoplist_ID))
                getShopListFromFirebase()
                tutorial()

            }else{
                //se il numero di item è 2, cancella il [0]
                Log.d(TAG, "it? : "+ it.get(idSize!!-1).spesa_id)
                if (_viewState.value.equals("a1a2a3")){}
                _viewState.value = it.get(idSize!!-1).spesa_id
                getShopListFromFirebase()
                if (idSize!!>1){
                    for (i in 0..idSize!!-2){
                        delete(it.get(i))
                    }
                }
            }
        })
    }

    fun tutorial(){

        addFirebase(" ↗ Click sull'icona ❏ per copiare il codice ID e invialo a qualcuno ↗ ", false)
        addFirebase(" ↑ clicca sul codice ID per poterne inserire uno nuovo ↑ ", false)
        addFirebase(" ⇢ scorri a destra per marcare ⇢ ", false)
        addFirebase(" ⇠ scorri a sinistra per cancellare ⇠ ", false)
        addFirebase("☐ clicca sul quadratino per sbarrare ☑ ", false)






    }

    private fun getShopListFromFirebase() {

        val database = com.google.firebase.ktx.Firebase.database("https://sharedshoplist-17901-default-rtdb.europe-west1.firebasedatabase.app/")
        //var index=0
        val firebase_shoplists_ids = database.getReference("shoplists_ids").child(_viewState.value)
        firebase_shoplists_ids.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                shopList.clear()
                for (DataSnap in snapshot.children) {
                    //index++
                    val item = DataSnap.getValue(ShopListItem::class.java)
                    if (item != null) {
                        shopList.add(ShopListItem(item.item_name, item.item_checked))
                    }
                }
                shopList.sortBy { it.item_checked }

            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }



    fun setChecked(index: Int, value: Boolean) {
        shopList[index] = shopList[index].copy(item_checked = value)
        shopList.add(ShopListItem(shopList[index].item_name.capitalize(), value))
        shopList.remove(shopList[index])
        shopList.sortBy { it.item_checked }


    }




    public fun addRecord(titleText: String, checked: Boolean) {
        Log.d(TAG, "addRecord: "+titleText)
        if (shopList.isEmpty()) {
            shopList.add(ShopListItem(titleText.capitalize(), checked))
        } else {
            shopList.add(ShopListItem(titleText.capitalize(), checked))
        }
    }
    fun removeRecord(item: ShopListItem) {
        Log.d(TAG, "removeRecord: "+item.item_name)
        val index = shopList.indexOf(item)
        shopList.remove(shopList[index])


    }

    fun addFirebase(titleText: String, checked: Boolean){
        Log.d(TAG, "addFirebase: ")
        // add record on firebase
        val firebase = Firebase(_viewState.value)
        firebase.addValueFirebase(ShopListItem(titleText.capitalize(), checked))
    }
    fun removeFirebase(titleText: String){
        Log.d(TAG, "removeFirebase: ")
        // delete on firebase
        val firebase = Firebase(_viewState.value)
        firebase.deleteFromFirebase(titleText)
    }

    fun setcheckedFirebase(index: Int, value: Boolean){
        Log.d(TAG, "setcheckedFirebase: "+shopList[index].item_name)
        val firebase = Firebase(_viewState.value)
        shopList[index].item_checked=value
        firebase.checkItemFirebase(shopList[index])
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