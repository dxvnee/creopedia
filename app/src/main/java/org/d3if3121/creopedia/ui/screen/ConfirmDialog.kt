package org.d3if3121.creopedia.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import coil.request.ImageRequest
import org.d3if3121.creopedia.R
import org.d3if3121.creopedia.model.Creo
import org.d3if3121.creopedia.model.User
import org.d3if3121.creopedia.network.CreoApi
import org.d3if3121.creopedia.ui.theme.CreopediaTheme

@Composable
fun ConfirmDialog(
    selectedCreo: Creo?,
    onDismissRequest: () -> Unit,
    onConfirmationEdit: () -> Unit,
    onConfirmationDelete: () -> Unit
){
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Row {
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .border(1.dp, Color.Gray),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(
                                    CreoApi.getCreoUrl(selectedCreo!!.id)
                                )
                                .crossfade(true)
                                .build(),
                            contentDescription = selectedCreo!!.name,
                            contentScale = ContentScale.Crop,
                            placeholder = painterResource(id = R.drawable.loading_img),
                            error = painterResource(id = R.drawable.baseline_broken_image_24),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp)
                                .padding(4.dp)
                        )
                    }
                }
                Column (
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ){
                    Row (

                    ){
                        Column (
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            Text(
                                text = selectedCreo!!.id,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "Creo name: " + selectedCreo!!.name,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "Creo element: " + selectedCreo!!.element,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "Creo size: " + selectedCreo!!.size + " cm",
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(bottom = 20.dp)

                            )
                        }
                    }
                    Row {
                        Text(
                            text = "Edit or Delete? (" + selectedCreo!!.name + ") ",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {

                        OutlinedButton(
                            onClick = { onConfirmationEdit() },
                            modifier = Modifier.padding(8.dp)
                        ){
                            Text(text = "Edit")
                        }
                        OutlinedButton(
                            onClick = { onConfirmationDelete() },
                            modifier = Modifier.padding(8.dp),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.error)
                        ){
                            Text(
                                text = "Delete",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }

            }
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun ConfirmPreview(){
    CreopediaTheme {
        ConfirmDialog(
            Creo(
                name = "Egia",
                element = "Fier",
                size = "12",
                id = "19"
            ),
            onDismissRequest = {},
            onConfirmationEdit = {},
            onConfirmationDelete = {}
        )

    }
}