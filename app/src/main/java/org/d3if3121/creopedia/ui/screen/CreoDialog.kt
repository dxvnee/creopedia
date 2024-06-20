package org.d3if3121.creopedia.ui.screen

import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Paint.Align
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import org.d3if3121.creopedia.R
import org.d3if3121.creopedia.model.Creo
import org.d3if3121.creopedia.ui.theme.CreopediaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreoDialog(
    bitmap: Bitmap?,
    onDismissRequest: () -> Unit,
    onConfirmation: (Creo) -> Unit,


    tombolHapus: Boolean
){

    var name by remember { mutableStateOf("") }
    var element by remember { mutableStateOf("") }
    var size by remember { mutableStateOf("") }

    var nameError by remember { mutableStateOf(false) }
    var elementError by remember { mutableStateOf(false) }
    var sizeError by remember { mutableStateOf(false) }

    var hapus by remember { mutableStateOf(false) }


    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ){
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    bitmap = bitmap!!.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(text = "Name") },
                    maxLines = 1,
                    supportingText = { ErrorText(isError = nameError) },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp, 10.dp))
                        .padding(vertical = 10.dp),

                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        unfocusedBorderColor = warnaOrange,
                        focusedBorderColor = warnaOrange,
                        focusedLabelColor = warnaOrange,
                        unfocusedLabelColor = warnaHitam,
                    ),
                    shape = RoundedCornerShape(15.dp),
                    textStyle = TextStyle(
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        color = warnaHitam
                    ),
                    placeholder = {
                        Text(
                            text = "Creo name",
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            color = warnaAbu
                        )
                    },
                )
                OutlinedTextField(
                    value = element,
                    onValueChange = { element = it },
                    label = { Text(fontWeight = FontWeight.Normal, text = "Element") },
                    maxLines = 1,
                    supportingText = { ErrorText(isError = elementError) },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp, 10.dp))
                        .padding(vertical = 10.dp),

                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        unfocusedBorderColor = warnaOrange,
                        focusedBorderColor = warnaOrange,
                        focusedLabelColor = warnaOrange,
                        unfocusedLabelColor = warnaHitam,
                    ),
                    shape = RoundedCornerShape(15.dp),
                    textStyle = TextStyle(
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        color = warnaHitam
                    ),
                    placeholder = {
                        Text(
                            text = "Fire, earth, etc.",
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            color = warnaAbu
                        )
                    },
                )
                OutlinedTextField(
                    value = size,
                    onValueChange = { size = it },
                    label = { Text(  fontWeight = FontWeight.Normal, text = "Size") },
                    maxLines = 1,
                    supportingText = { ErrorText(isError = sizeError) },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        imeAction = ImeAction.Done
                    ),

                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp, 10.dp))
                        .padding(vertical = 10.dp),

                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        unfocusedBorderColor = warnaOrange,
                        focusedBorderColor = warnaOrange,
                        focusedLabelColor = warnaOrange,
                        unfocusedLabelColor = warnaHitam,
                    ),
                    shape = RoundedCornerShape(15.dp),
                    textStyle = TextStyle(
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        color = warnaHitam
                    ),
                    placeholder = {
                        Text(
                            text = "cm",
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            color = warnaAbu
                        )
                    },
                )


                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.Center
                ){
                    OutlinedButton(
                        onClick = { onDismissRequest() },
                        modifier = Modifier.padding(8.dp)
                    ){
                        Text(  fontWeight = FontWeight.Bold, text = "Batal")
                    }
                    OutlinedButton(
                        onClick = {
                            nameError = (name == "")
                            elementError = (element == "")
                            sizeError = (size == "")

                            if (nameError || elementError || sizeError){

                            } else {
                                onConfirmation(
                                    Creo(
                                        name = name,
                                        element = element,
                                        size =  size,
                                    ),
                                )
                            }
                        },
                        enabled = name.isNotEmpty() && element.isNotEmpty(),
                        modifier = Modifier.padding(8.dp)
                    ){
                        Text(  fontWeight = FontWeight.Bold, text = "Save")
                    }

                }
            }
        }
    }
}

@Composable
fun ErrorText(isError: Boolean){
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun AddDialogPreview(){
    CreopediaTheme {
        CreoDialog(bitmap = null,
            onDismissRequest = {},
            onConfirmation = { creo ->
                Creo(
                    name ="tes",
                    element = "fire",
                    size = "jee"
                )
            },
            false
        )
    }
}



