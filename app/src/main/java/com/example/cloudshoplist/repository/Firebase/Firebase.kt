package com.dreamsphere.sharedshoplistk.repository.Firebase

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.dreamsphere.sharedshoplistk.models.ShopListItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlin.math.log

class Firebase(path_spesaID: String) {


    private val TAG = "Main Firebase"


    val database =
        Firebase.database("https://sharedshoplist-17901-default-rtdb.europe-west1.firebasedatabase.app/")

    val firebase_shoplists_ids = database.getReference("shoplists_ids").child(path_spesaID)


    fun fetchDataFromFirebase(): SnapshotStateList<ShopListItem> {
        Log.d(TAG, "fetchDataFromFirebase: path: " + firebase_shoplists_ids)
        val tempList = mutableStateListOf<ShopListItem>()
        /*        // firebase
                val response: MutableState<DataState> = mutableStateOf(DataState.Empty)

                val tempList = mutableStateListOf<ShopListItem>()
                response.value = DataState.Loading
                firebase_shoplists_ids
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for (DataSnap in snapshot.children){
                                val item = DataSnap.getValue(ShopListItem::class.java)
                                if (item!=null){
                                    Log.d(TAG, "onDataChange: item: "+item.item_name)
                                    tempList.add(item)}

                                //check item e path
                            }


                        }

                        override fun onCancelled(error: DatabaseError) {
                            response.value= DataState.Success(tempList)
                        }
                    })*/

        firebase_shoplists_ids.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (DataSnap in snapshot.children) {
                    val item = DataSnap.getValue(ShopListItem::class.java)
                    if (item != null) {
                        Log.d(TAG, "onDataChange: item: " + item.item_name)
                        tempList.add(item)
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        return tempList
    }

    fun checkItemFirebase(shopListItem: ShopListItem) {
        Log.d(TAG, "checkItemFirebase: "+firebase_shoplists_ids+"/"+shopListItem.item_name+"/item_checked = "+shopListItem.item_checked)
        firebase_shoplists_ids.child(shopListItem.item_name).child("item_checked").setValue(shopListItem.item_checked)
    }

    fun addValueFirebase(shopListItem: ShopListItem) {
        firebase_shoplists_ids.child(shopListItem.item_name).setValue(shopListItem)
    }

    fun deleteFromFirebase(item_name: String) {
        firebase_shoplists_ids.child(item_name).removeValue()
    }


}