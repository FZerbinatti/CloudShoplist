package com.example.cloudshoplist.View
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.dreamsphere.CloudShoplist.R
import com.example.cloudshoplist.InputValidator
import com.example.cloudshoplist.ViewModel.MainViewModel
import kotlinx.coroutines.android.awaitFrame

@Composable
fun PersonalizedAlertDialog(closeRecord: () -> Unit, viewModel: MainViewModel) {



    val focusRequester = remember { FocusRequester() }
    val tile_box = remember { mutableStateOf("item") }
    val context = LocalContext.current

    LaunchedEffect(focusRequester) {
        awaitFrame()
        focusRequester.requestFocus()
    }

    Surface(
        border = BorderStroke(1.dp, colorResource(id = R.color.black)),
        shape = RoundedCornerShape(8.dp),
        color = Color(0xEBEEEEEE),
        modifier = Modifier.padding(horizontal = 30.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var titleText by rememberSaveable { mutableStateOf("") }
            var checkBoxStatus by rememberSaveable { mutableStateOf(false) }
            TextField(
                modifier = Modifier.focusRequester(focusRequester),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                textStyle = TextStyle(color = Color.Black),
                        value = titleText,
                onValueChange = { titleText = it.replaceFirstChar { it.uppercase() } },
                label = { tile_box },


                )
            /*Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Urgency:")
                Checkbox(
                    checked = checkBoxStatus,
                    onCheckedChange = {
                        checkBoxStatus = !checkBoxStatus
                    }
                )
            }*/
            Spacer(modifier = Modifier.height(32.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    modifier = Modifier
                        .height(50.dp)
                        .width(80.dp),

                        //.clip(shape = RoundedCornerShape(15.dp)),
                        //.background(colorResource(id = R.color.button_green_color)),



                    onClick = {
                        //val blockedChars: String = "~#^|\$%&*!./"
                        titleText = titleText.replace(".", " ").replace(",", " ")
                        Log.d(" Main TAG", "PersonalizedAlertDialog: input validator: "+InputValidator.validateInput(titleText))
                        if(InputValidator.validateInput(titleText)){
                            Log.d("Main Personalized Alert Dialog", "PersonalizedAlertDialog: adding text: "+titleText)
                            viewModel.addRecord(titleText, checkBoxStatus)
                            viewModel.addFirebase(titleText, checkBoxStatus)
                            closeRecord.invoke()
                        }else{
                            tile_box.value ="Non sono ammessi caratteri speciali"
                            Toast.makeText(context, context.getString(R.string.INVALID_INPUT), Toast.LENGTH_LONG).show()
                        }
                    },


                    border = BorderStroke(2.dp, Color.Gray),
                    shape = RoundedCornerShape(15.dp)


                    ) {
                    Text(text = "Ok", color = colorResource(id = R.color.black))
                }
            }

        }
    }
}

