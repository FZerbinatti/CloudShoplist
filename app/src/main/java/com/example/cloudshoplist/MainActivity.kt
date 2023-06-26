package com.example.cloudshoplist
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dreamsphere.CloudShoplist.R
import com.dreamsphere.sharedshoplistk.repository.Room.IdDatabase
import com.dreamsphere.sharedshoplistk.repository.Room.IdItem
import com.dreamsphere.sharedshoplistk.repository.Room.IdRepository
import com.example.cloudshoplist.View.PersonalizedAlertDialog
import com.example.cloudshoplist.View.ShoplistIdViewModelFactory
import com.example.cloudshoplist.View.SpesaList
import com.example.cloudshoplist.View.TopBox_ID_Spesa
import com.example.cloudshoplist.View.WindowCenterOffsetPositionProvider
import com.example.cloudshoplist.View.getRandomString
import com.example.cloudshoplist.ViewModel.MainViewModel
import com.example.cloudshoplist.repository.SharedPreferencesUtils.SharedPreferenceStringLiveData
import com.example.cloudshoplist.ui.theme.CloudShoplistTheme
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    private  val TAG = "MainActivity"

    lateinit var ViewModel: MainViewModel
    lateinit var sharedPref: SharedPreferenceStringLiveData
    val context = this

    val Context.dataStore : DataStore<Preferences> by preferencesDataStore(
        name = "shoplist_id"

    )

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            //room
            val shopListIdsRepository = IdRepository(IdDatabase(this))
            val factory = ShoplistIdViewModelFactory(shopListIdsRepository)
            ViewModel = ViewModelProvider(this,factory).get(MainViewModel::class.java)

            var spesaID = ViewModel.shoplist_ID
            var showDialog = remember { mutableStateOf(false) }
            val scope = rememberCoroutineScope()
            val lazyListState = rememberLazyListState()
            val todoListState = ViewModel.shopListFlow.collectAsState()

            val state = ViewModel.viewState.collectAsState().value
            Log.d(TAG, "onCreate: state? "+state)


            // VIEW
            CloudShoplistTheme() {
                Scaffold(
                    floatingActionButton = {
                        val context = LocalContext.current
                        FloatingActionButton(
                            backgroundColor = colorResource(id = R.color.button_color),
                            onClick = {
                                //apri alert dialog
                                showDialog.value = true
                            }) {
                            Icon(Icons.Default.Add, contentDescription = null)
                        }
                    },
                    content = {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colors.background
                        ) {
                            //MainView(viewModel, context)
                            Column() {

                                TopBox_ID_Spesa(state)
                                SpesaList(ViewModel)

                                //popup per inserire nuovo item
                                if (showDialog.value==true) {
                                    Popup(
                                        popupPositionProvider = WindowCenterOffsetPositionProvider(),
                                        onDismissRequest = { showDialog.value = false },
                                        properties = PopupProperties(focusable = true)
                                    ) {
                                        PersonalizedAlertDialog({
                                            showDialog.value = false
                                            scope.launch {
                                                lazyListState.scrollToItem(todoListState.value.size)
                                            }
                                        }, ViewModel)
                                    }
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}




//to do: add settings button where:
// - add bigger fontsize
// - add notification request for this list members


