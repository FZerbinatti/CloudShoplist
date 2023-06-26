package com.dreamsphere.sharedshoplistk.repository.Firebase

import com.dreamsphere.sharedshoplistk.models.ShopListItem

sealed class DataState {

    class Success(val data: MutableList<ShopListItem>): DataState()
    class Failure(val message: MutableList<ShopListItem>): DataState()
    object Loading: DataState()
    object Empty: DataState()



}