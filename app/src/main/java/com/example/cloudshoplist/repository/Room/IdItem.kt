package com.dreamsphere.sharedshoplistk.repository.Room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_shiplist_id")

data class IdItem(

    var spesa_id: String,
    @ColumnInfo(name = "id_shoplist")
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

){


}
