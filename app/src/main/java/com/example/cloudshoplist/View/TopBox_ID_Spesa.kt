package com.example.cloudshoplist.View
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dreamsphere.CloudShoplist.R

@Composable
fun TopBox_ID_Spesa(spesa_ID: String) {

    val context = LocalContext.current

    //questa variabile quando diventa vera triggera l'AlertDialog
    var showDialog = remember { mutableStateOf(false) }
    var alertPastedIdSpesa = remember { mutableStateOf("") }

    val robotoFont = FontFamily(
        Font(R.font.robotthin, FontWeight.Thin),
        Font(R.font.robotoregular, FontWeight.Normal),
        Font(R.font.robotobold, FontWeight.Bold),
    )

    //se non è presente nessun ID nel sharedPreferences, generane uno nuovo

    //Toast.makeText(context, "Db Inserted", Toast.LENGTH_LONG).show()

    if (showDialog.value)
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text(text = context.getString(R.string.ALERT_PASTE_ID_SPESA)) },

            text = {
                Column() {
                    TextField(
                        value = alertPastedIdSpesa.value,
                        onValueChange = { alertPastedIdSpesa.value = it }
                    )
                }
            },


            //on confirm, se il textbox non è vuoto, compialo nella casella
            confirmButton = {
                Button(onClick = {

                    if (!alertPastedIdSpesa.value.isEmpty()) {
                        var spesa_ID = alertPastedIdSpesa.value

                        showDialog.value = false


                    } else {
                        Toast.makeText(context, context.getString(R.string.EMPTY_ID), Toast.LENGTH_LONG).show()
                    }

                }) {
                    Text(text = stringResource(R.string.CONFIRM))
                }
            },

            //on dismiss cancella l'alert
            dismissButton = {
                Button(onClick = { showDialog.value = false }) {
                    Text(text = stringResource(R.string.CANCEL))

                }
            }
        )

    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {

        Row(
            Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = Color.Gray,
                    shape = RoundedCornerShape(15.dp)
                )
                .clip(RoundedCornerShape(15.dp))
                .background(Color.White)

        ) {

            Box() {


                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        stringResource(R.string.ID_SHOPLIST),
                        fontFamily = robotoFont,
                        fontWeight = FontWeight.Normal,
                        fontSize = 20.sp,

                        color = colorResource(id = R.color.texts_color),
                        modifier = Modifier
                            .padding(10.dp)
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End,

                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth()
                            .border(
                                width = 1.dp,
                                color = Color.Gray,
                                shape = RoundedCornerShape(15.dp)
                            )
                            .clip(RoundedCornerShape(15.dp))
                            .background(Color.White),

                        ) {

                        ClickableText(
                            AnnotatedString(spesa_ID),
                            onClick = {
                                showDialog.value = true

                            },
                            /*fontFamily = robotoFont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = colorResource(id = R.color.texts_color),
                            textAlign = TextAlign.Center,*/
                            modifier = Modifier
                                .padding(horizontal = 15.dp),

                            )

                        Button(
                            onClick = {

                                val clipboardManager =
                                    context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clipData = ClipData.newPlainText("ID_Spesa", spesa_ID)
                                clipboardManager.setPrimaryClip(clipData)
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.text_copied_to_clipboard),
                                    Toast.LENGTH_LONG
                                ).show()
                            },
                            Modifier.height(50.dp),
                            border = BorderStroke(1.dp, Color.Gray),
                            colors = ButtonDefaults.buttonColors(
                                contentColor = Color.Gray,
                                backgroundColor = Color.White
                            )
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_copy),
                                contentDescription = null
                            )
                        }
                    }
                }
            }
        }
    }


}

fun getRandomString(length: Int) : String {
    val allowedChars = ('A'..'Z') + ('0'..'9')
    return (1..length)
        .map { allowedChars.random() }
        .joinToString("")
}
