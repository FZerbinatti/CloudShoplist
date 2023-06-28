package com.example.cloudshoplist.View

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dreamsphere.CloudShoplist.R
import com.dreamsphere.sharedshoplistk.models.ShopListItem
import com.example.cloudshoplist.ViewModel.MainViewModel

@Composable
fun ShopListItemRow(
    item: ShopListItem,
    todoListState: State<List<ShopListItem>>,
    viewModel: MainViewModel,
    robotoFont: FontFamily = FontFamily(
        Font(R.font.robotthin, FontWeight.Thin),
        Font(R.font.robotoregular, FontWeight.Normal),
        Font(R.font.robotobold, FontWeight.Bold),
    )
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        /*Text(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp),
            text = item.title
        )*/
        if (item.item_checked){
            Text(
                item.item_name,
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),

                textAlign = TextAlign.Start,
                fontFamily = robotoFont,
                fontWeight = FontWeight.Normal,
                fontSize = 22.sp,
                color = colorResource(id = R.color.texts_color),
                style = TextStyle(textDecoration = TextDecoration.LineThrough)
            )
        }else{
            Text(
                item.item_name,
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),

                textAlign = TextAlign.Start,
                fontFamily = robotoFont,
                fontWeight = FontWeight.Normal,
                fontSize = 22.sp,
                color = colorResource(id = R.color.texts_color)
            )
        }

        Checkbox(
            checked = item.item_checked,

            onCheckedChange = {
                val index = todoListState.value.indexOf(item)
                viewModel.setcheckedFirebase(index, it)
                viewModel.setChecked(index, it)
            },
            colors = CheckboxDefaults.colors(uncheckedColor = Color.Black)
        )
    }
}