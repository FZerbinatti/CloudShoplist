package com.example.cloudshoplist.View

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.cloudshoplist.ViewModel.MainViewModel

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun SpesaList(viewModel: MainViewModel) {



    val shopListState = viewModel.shopListFlow.collectAsState()
    val lazyListState = rememberLazyListState()
    val scope = rememberCoroutineScope()




    Column(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 10.dp)

            .fillMaxWidth()
            .fillMaxHeight()
            .border(
                width = 1.dp,
                color = Color.Gray,
                shape = RoundedCornerShape(15.dp)
            )
            .clip(RoundedCornerShape(15.dp))
            .background(Color.White)


    ) {
        LazyColumn(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.padding(10.dp),
            state = LazyListState()
        ) {

            items(
                items = shopListState.value,
                key = { shopItem -> shopItem.item_name },
                itemContent = { item ->
                    val currentItem by rememberUpdatedState(item)
                    val dismissState = rememberDismissState(
                        confirmStateChange = {

                            if (it == DismissValue.DismissedToStart) {
                                Log.d("Main ", "SpesaList:  DismissValue.DismissedToStart")
                                viewModel.removeRecord(currentItem)
                                viewModel.removeFirebase(currentItem.item_name)

                                true

                            } else if (it == DismissValue.DismissedToEnd) {
                                //rimuovi item ma mettilo in coda come checked
                                Log.d("Main ", "SpesaList:  DismissValue.DismissedToEnd")
                                viewModel.setcheckedFirebase(shopListState.value.indexOf(currentItem), true)
                                //viewModel.removeRecord(currentItem)
                                //viewModel.addRecord(currentItem.item_name, true)


                                true
                            } else {
                                false
                            }
                        }
                    )

                    if (dismissState.isDismissed(DismissDirection.EndToStart)) {
                        Log.d("Main ", "SpesaList: DismissDirection.EndToStart")
                        viewModel.removeFirebase(currentItem.item_name)
                        viewModel.removeRecord(item)
                    } else if (dismissState.isDismissed(DismissDirection.StartToEnd)) {
                        //rimuovi item ma mettilo in coda come checked
                        Log.d("Main ", "SpesaList: DismissDirection.StartToEnd")
                        viewModel.setcheckedFirebase(shopListState.value.indexOf(currentItem), true)
                        //viewModel.removeRecord(item)
                        //viewModel.addRecord(currentItem.item_name, true)



                    }

                    SwipeToDismiss(
                        state = dismissState,
                        modifier = Modifier
                            .padding(vertical = 1.dp)
                            .animateItemPlacement(),
                        directions = setOf(
                            DismissDirection.StartToEnd,
                            DismissDirection.EndToStart
                        ),
                        dismissThresholds = { direction ->
                            FractionalThreshold(
                                if (direction == DismissDirection.StartToEnd) 0.15f else 0.15f
                            )
                        },
                        background = {
                            SwipeBackground(dismissState, item)
                        },
                        dismissContent = {
                            ShopListItemRow(item, shopListState, viewModel)
                        }
                    )


                }

            )

        }
    }


}

/*@Composable
fun SetData(viewModel: MainViewModel){
    when(val result = viewModel.response.value)
}*/



