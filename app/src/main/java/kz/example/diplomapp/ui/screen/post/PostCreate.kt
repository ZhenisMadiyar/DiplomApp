package kz.example.diplomapp.ui.screen.post

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.firestore.FieldValue
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch
import kz.example.diplomapp.R
import kz.example.diplomapp.common.UiState
import kz.example.diplomapp.domain.model.Category
import kz.example.diplomapp.ui.screen.LoadingScreen
import kz.example.diplomapp.ui.screen.category.DictList
import kz.example.diplomapp.ui.screen.common.ErrorDialog
import kz.example.diplomapp.ui.screen.main.MainViewModel
import java.time.LocalDate
import java.util.*

@ExperimentalMaterialApi
@Composable
fun PostCreate(
    postViewModel: PostViewModel,
    categoryViewModel: MainViewModel,
    navController: NavHostController
) {
    /*
    * title
    * desc
    * photo
    * price
    * createdDate
    * */

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )
    val coroutineScope = rememberCoroutineScope()

    var storage = FirebaseStorage.getInstance()
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val context = LocalContext.current
    val bitmap = remember {
        mutableStateOf<Bitmap?>(null)
    }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {
        imageUri = it
    }

    var title by remember {
        mutableStateOf("")
    }
    var desc by remember {
        mutableStateOf("")
    }
    var price by remember {
        mutableStateOf("")
    }
    var thumb by remember {
        mutableStateOf("")
    }
    var selectedCategory: Category? by remember {
        mutableStateOf(null)
    }
    var selectedSubCategory: Category? by remember {
        mutableStateOf(null)
    }
    var isSubCategory: Boolean = false
    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            LaunchedEffect(bottomSheetScaffoldState.bottomSheetState.isExpanded) {
                if (isSubCategory) {
                    categoryViewModel.getSubCategory(selectedCategory?.documentId ?: "")
                } else {
                    categoryViewModel.getCategory()
                }
            }
            when (val state = categoryViewModel.uiState.collectAsState().value) {
                is UiState.Loading -> {
                    LoadingScreen()
                }
                is UiState.Success<*> -> {
                    DictList(state.list as List<Category>) {
                        if (isSubCategory) selectedSubCategory = it
                        else selectedCategory = it
                        coroutineScope.launch {
                            bottomSheetScaffoldState.bottomSheetState.collapse()
                        }
                    }
                }
                is UiState.Error -> {
                    ErrorDialog(message = state.exception.toString())
                }
            }
        },
        sheetPeekHeight = 60.dp
    ) {
        Column(
            Modifier
                .padding(it)
                .padding(16.dp)
        ) {

            OutlinedTextField(
                value = title,
                onValueChange = { titleValue -> title = titleValue },
                label = { Text(text = stringResource(R.string.title)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.padding(top = 8.dp, bottom = 8.dp))
            OutlinedTextField(
                value = desc,
                onValueChange = { descValue -> desc = descValue },
                label = { Text(text = stringResource(R.string.desc)) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.padding(top = 8.dp, bottom = 8.dp))
            OutlinedTextField(
                value = price,
                onValueChange = { priceValue -> price = priceValue },
                label = { Text(text = stringResource(R.string.price)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.padding(top = 8.dp, bottom = 8.dp))
            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                onClick = {
                    isSubCategory = false
                    selectedSubCategory = null
                    coroutineScope.launch {
                        if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                            bottomSheetScaffoldState.bottomSheetState.expand()
                        } else {
                            bottomSheetScaffoldState.bottomSheetState.collapse()
                        }
                    }
                }) {
                Text(text = selectedCategory?.title ?: stringResource(R.string.select_category))
            }

            if (selectedCategory != null) {
                Spacer(modifier = Modifier.padding(top = 8.dp, bottom = 8.dp))
                OutlinedButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    onClick = {
                        isSubCategory = true
                        coroutineScope.launch {
                            if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                                bottomSheetScaffoldState.bottomSheetState.expand()
                            } else {
                                bottomSheetScaffoldState.bottomSheetState.collapse()
                            }
                        }
                    }) {
                    Text(text = selectedSubCategory?.title ?: stringResource(R.string.select_subcategory))
                }
            }
            Spacer(modifier = Modifier.padding(top = 8.dp, bottom = 8.dp))
            Button(modifier = Modifier.fillMaxWidth(), onClick = {
                //upload photo
                launcher.launch("image/*")
            }) {
                Text(text = "Загрузить фото")
            }

            bitmap.value?.let { btm ->
                Image(
                    bitmap = btm.asImageBitmap(), contentDescription = "",
                    modifier = Modifier.size(160.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    if (title == "") {
                        Toast.makeText(
                            context,
                            context.getString(R.string.empty_title),
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Button
                    } else if (desc == "") {
                        Toast.makeText(
                            context,
                            context.getString(R.string.empty_desc),
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Button
                    } else if (price == "") {
                        Toast.makeText(
                            context,
                            context.getString(R.string.empty_price),
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Button
                    } else if (selectedCategory == null || selectedSubCategory == null) {
                        Toast.makeText(
                            context,
                            context.getString(R.string.empty_category2),
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Button
                    }
                    //send post to Firebase
                    val data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        LocalDate.now()
                    } else {
                        Calendar.getInstance().time
                    }
                    val postData = hashMapOf<String, Any>(
                        "title" to title,
                        "description" to desc,
                        "price" to price.toInt(),
                        "thumb" to thumb,
                        "createdDate" to FieldValue.serverTimestamp()
                    )
                    postViewModel.createPost(
                        selectedCategory?.documentId!!,
                        selectedSubCategory?.documentId!!,
                        postData
                    )

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(text = stringResource(R.string.create))
            }
            Spacer(modifier = Modifier.padding(bottom = 60.dp))
            when (val state = postViewModel.uiState.collectAsState().value) {
                is UiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Column(
                            Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = stringResource(R.string.loading))
                            CircularProgressIndicator()
                        }
                    }
                }
                is UiState.Success<*> -> {
                    Toast.makeText(
                        context,
                        stringResource(R.string.successfully_added),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is UiState.Error -> {
                    Log.w("Failure creating", state.exception.toString())
                    ErrorDialog(message = state.exception.toString())
                }
            }

            imageUri?.let { uri ->
                val uuid = (0..1000).random()
                val storageRef = storage.reference
                val imageRef = storageRef.child("${uuid}image.jpg")
                imageRef.putFile(uri)
                    .addOnSuccessListener {
                        imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                            thumb = downloadUri.toString()
                            Log.i("Download url", downloadUri.toString())
                        }
                        if (Build.VERSION.SDK_INT < 28) {
                            bitmap.value = MediaStore.Images
                                .Media.getBitmap(context.contentResolver, uri)

                        } else {
                            val source = ImageDecoder
                                .createSource(context.contentResolver, uri)
                            bitmap.value = ImageDecoder.decodeBitmap(source)
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            context,
                            context.getString(R.string.problem_occurred),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
        }
    }
}