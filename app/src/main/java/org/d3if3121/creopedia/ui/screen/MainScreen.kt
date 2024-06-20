package org.d3if3121.creopedia.ui.screen


import android.content.ContentResolver
import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.drawable.BitmapDrawable
import android.media.Image
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.ClearCredentialException
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.d3if3121.creopedia.BuildConfig
import org.d3if3121.creopedia.R
import org.d3if3121.creopedia.model.Creo
import org.d3if3121.creopedia.model.User
import org.d3if3121.creopedia.network.ApiStatus
import org.d3if3121.creopedia.network.CreoApi
import org.d3if3121.creopedia.network.UserDataStore
import org.d3if3121.creopedia.ui.theme.CreopediaTheme
import org.d3if3121.creopedia.ui.viewmodel.MainViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CreopediaTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = warnaPutih) {
                    MainScreen()
                }
            }
        }
    }
}

val warnaOrange = Color(0xFFDB3E00)
val warnaOrangeTua = Color(0xFFA83500)
val warnaPutih = Color(0xFFFFFFFF)
val warnaHitam = Color(0xFF000000)
val warnaAbu = Color(0xFF5C5C5C)
val warnaMerah = Color(0xFFDF0000)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {

    val context  = LocalContext.current
    val dataStore = UserDataStore(context)
    val user by dataStore.userFlow.collectAsState(User())

    val viewModel: MainViewModel = viewModel()
    val errorMessage by viewModel.errorMessage

    var showDialog by remember { mutableStateOf(false) }
    var showCreoDialog by remember { mutableStateOf(false)}
    var showCreoConfirmDialog by remember { mutableStateOf(false)}
    var showCreoEditDialog by remember { mutableStateOf(false)}
    var showCreoDeleteDialog by remember { mutableStateOf(false)}

    var selectedCreo by remember { mutableStateOf<Creo?>(null)}

    var bitmap: Bitmap? by remember { mutableStateOf(null) }

    val launcher = rememberLauncherForActivityResult(CropImageContract()) {
        bitmap = getCroppedImage(context.contentResolver, it)
        if (bitmap != null) showCreoDialog = true
    }
    val launcherEdit = rememberLauncherForActivityResult(CropImageContract()) {
        bitmap = getCroppedImage(context.contentResolver, it)
        if (bitmap != null){
            showCreoEditDialog = true
            Log.d("JADIDI", "JADIID")
        }
    }

    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text(  fontWeight = FontWeight.ExtraBold, text = stringResource(id = R.string.app_name))
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = warnaOrange,
                    titleContentColor = warnaPutih,
                ),
                actions = {
                    IconButton(onClick = {
                        if(user.email.isEmpty()){
                            CoroutineScope(Dispatchers.IO).launch {
                                signIn(context, dataStore)
                            }
                        }
                        else {
                            showDialog = true
                        }
                    }) {

                        Icon(
                            painter = painterResource(R.drawable.baseline_account_circle_24),
                            contentDescription = "profile",
                            tint = warnaPutih
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                containerColor = warnaOrange,
                onClick = {
                val options = CropImageContractOptions(
                    null, CropImageOptions(
                        imageSourceIncludeGallery = true,
                        imageSourceIncludeCamera = true,
                        fixAspectRatio = true
                    )
                )
                launcher.launch(options)
            },) {
                Icon(
                    tint = warnaPutih,
                    imageVector = Icons.Default.Add,
                    contentDescription = "Tambah"
                )
            }
        }
    ) { padding ->
        ScreenContent(user, viewModel, user.email, Modifier.padding(padding)){ creoData ->
            showCreoConfirmDialog = true
            selectedCreo = creoData
            Log.d("D", "hehhe")
        }
        if (showCreoConfirmDialog){
            ConfirmDialog(
                selectedCreo = selectedCreo,
                onDismissRequest = { showCreoConfirmDialog = false },
                onConfirmationEdit = {
                    val options = CropImageContractOptions(
                        null, CropImageOptions(
                            imageSourceIncludeGallery = true,
                            imageSourceIncludeCamera = true,
                            fixAspectRatio = true
                        )
                    )
                    launcherEdit.launch(options)
                    showCreoConfirmDialog = false
                },
                onConfirmationDelete = {
                    showCreoDeleteDialog = true

                    showCreoConfirmDialog = false
                }
            )

        }

        if(showCreoDeleteDialog){
            DeleteDialog(
                selectedCreo = selectedCreo,
                onDismissRequest = {
                    showCreoDeleteDialog = false
                }
            ){
                viewModel.deleteCreo(user.email, selectedCreo!!.id.toInt())
                showCreoDeleteDialog = false
            }
        }

        if(showCreoEditDialog){
            CreoDialog(
                bitmap = bitmap,
                onDismissRequest = {
                    showCreoEditDialog = false
                },
                onConfirmation = { creo ->
                    viewModel.updateCreo(user.email, selectedCreo!!.id.toInt() ,creo, bitmap!!)
                    showCreoEditDialog = false
                },
                tombolHapus = true
            )
        }

        if (showDialog){
            ProfilDialog(
                user = user,
                onDismissRequest = { showDialog = false }) {
                CoroutineScope(Dispatchers.IO).launch { signOut(context, dataStore) }
                showDialog = false
            }
        }

        if (showCreoDialog){
            CreoDialog(
                bitmap = bitmap,
                onDismissRequest = {
                    showCreoDialog = false
                },
                onConfirmation = { creo ->
                    viewModel.createCreoWithImage(user.email, creo, bitmap!!)
                    showCreoDialog = false
                }, false

            )
        }

        if (errorMessage != null) {
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            viewModel.clearMessage()
        }

    }


}

@Composable
fun ScreenContent(
    user: User,
    viewModel: MainViewModel,
    userId: String,
    modifier: Modifier,
    onItemClicked: (Creo) -> Unit
){

    val creoData by viewModel.creoData.collectAsState()
    val status by viewModel.status.collectAsState()

    var showCreoDialog by remember { mutableStateOf(false) }

    LaunchedEffect(userId, viewModel.status){
        viewModel.getAllCreo(userId)
    }

    when (status){
        ApiStatus.LOADING -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
//                CircularProgressIndicator()
                Text(  fontWeight = FontWeight.Bold, text = "Loading..")

            }
        }

        ApiStatus.SUCCESS -> {

            LazyVerticalGrid (
                modifier = modifier.fillMaxSize(),
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(bottom = 80.dp)
            ){
                items(creoData){
                    ListItem(
                        creo = it
                    ){
                        onItemClicked(it)
                    }
                }
            }
        }

        ApiStatus.FAILED -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement =  Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(  fontWeight = FontWeight.Normal, text = "Error")

                Button (
                    onClick = { viewModel.getAllCreo(userId) },
                    modifier = Modifier.padding(top = 16.dp),
                    contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = warnaOrange,
                        contentColor = warnaPutih,
                        disabledContentColor = warnaPutih,
                        disabledContainerColor = warnaOrangeTua
                    )
                ) {
                    Text(  fontWeight = FontWeight.Bold, text = "Try Again")
                }
            }
        }

    }

}

@Composable
fun ListItem(
    creo: Creo,
    onClicks: () -> Unit
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(11.dp))
            .clickable(
                onClick = onClicks
            )
            .padding(7.dp),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = warnaPutih,
            contentColor = warnaPutih,
            disabledContainerColor = warnaOrange,
            disabledContentColor = warnaPutih
        )
    ){
        Column {
            Row(
                modifier = Modifier.padding(10.dp)
            ){
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(
                            CreoApi.getCreoUrl(creo.id)
                        )
                        .crossfade(true)
                        .build(),
                    contentDescription = creo.name,
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.loading_img),
                    error = painterResource(id = R.drawable.baseline_broken_image_24),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(shape = RoundedCornerShape(12.dp), color = warnaAbu)
                )
            }
            Row {
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                ){
                    Row(
                        modifier = Modifier.padding(14.dp)
                    ){

                        Column (
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(end = 10.dp)
                        ){
                            Text(
                                text = creo.id,
                                fontWeight = FontWeight.ExtraBold,
                                color = warnaOrange,
                                fontSize = 30.sp,
                            )
                        }
                        Column(
                            modifier = Modifier.background(shape = RoundedCornerShape(12.dp), color = warnaOrange)
                                .fillMaxWidth()
                                .padding(10.dp)

                        )
                        {
                            Text(
                                text = creo.name,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = creo.name,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = creo.element,
                                fontWeight = FontWeight.Normal,
                                fontStyle = FontStyle.Italic,
                                fontSize = 14.sp,
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )

                        }
                    }

                }
            }
        }

    }
}


private fun getCroppedImage(
   resolver: ContentResolver,
   result:  CropImageView.CropResult
): Bitmap? {
    if(!result.isSuccessful){
        Log.e("IMAGE", "Error: ${result.error}")
        return null
    }

    val uri = result.uriContent?: return null

    return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
        MediaStore.Images.Media.getBitmap(resolver, uri)
    } else {
        val source = ImageDecoder.createSource(resolver, uri)
        ImageDecoder.decodeBitmap(source)
    }
}

private suspend fun signIn(context: Context, dataStore: UserDataStore) {
    val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(false)
        .setServerClientId(BuildConfig.API_KEY)
        .build()

    val request: GetCredentialRequest = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()

    try {
        val credentialManager = CredentialManager.create(context)
        val result = credentialManager.getCredential(context, request)
        handleSignIn(result, dataStore)
    } catch (e: GetCredentialException) {
        Log.e("SIGN-IN", "Error: ${e.errorMessage}")
    }
}

private suspend fun signOut(context: Context, dataStore: UserDataStore){
    try {
        val credentialManager = CredentialManager.create(context)
        credentialManager.clearCredentialState(
            ClearCredentialStateRequest()
        )
        dataStore.saveData(User())
    } catch (e: ClearCredentialException){
        Log.e("SIGN-IN", "Error: ${e.errorMessage}")
    }
}

private suspend fun handleSignIn(result: GetCredentialResponse, dataStore: UserDataStore) {
    val credential = result.credential
    if (credential is CustomCredential &&
        credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
        try {
            val googleId = GoogleIdTokenCredential.createFrom(credential.data)
            val nama = googleId.displayName ?: ""
            val email = googleId.id
            val photoUrl = googleId.profilePictureUri.toString()
            dataStore.saveData(User(nama, email, photoUrl))

        } catch (e: GoogleIdTokenParsingException) {
            Log.e("SIGN-IN", "Error: ${e.message}")
        }
    }
    else {
        Log.e("SIGN-IN", "Error: unrecognized custom credential type.")
    }
}


@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun ScreenPreview() {
    CreopediaTheme {
        MainScreen()
    }
}