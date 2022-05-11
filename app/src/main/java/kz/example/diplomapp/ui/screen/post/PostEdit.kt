package kz.example.diplomapp.ui.screen.post

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kz.example.diplomapp.R
import kz.example.diplomapp.common.UiState
import kz.example.diplomapp.domain.model.Post
import kz.example.diplomapp.ui.screen.common.ErrorDialog
import java.time.LocalDate
import java.util.*

@ExperimentalMaterialApi
@Composable
fun PostEdit(
    postViewModel: PostViewModel,
    navController: NavHostController,
    parentCategoryId: String,
    subCategoryId: String,
    postItem: Post
) {
    /*
    * title
    * desc
    * price
    * */

    val context = LocalContext.current

    var title by remember {
        mutableStateOf(postItem.title)
    }
    var desc by remember {
        mutableStateOf(postItem.description)
    }
    var price by remember {
        mutableStateOf(postItem.price.toString())
    }
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
            LaunchedEffect(Unit) {
                Toast.makeText(
                    context,
                    context.getString(R.string.successfully_edited),
                    Toast.LENGTH_SHORT
                ).show()
                navController.popBackStack()
            }
        }
        is UiState.Error -> {
            Log.w("Failure editing", state.exception.toString())
            ErrorDialog(message = state.exception.toString())
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = Color.White,
                elevation = 2.dp,
                title = { Text(text = stringResource(id = R.string.edit), color = Color.Black) },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "backEdit")
                    }
                }
            )
        }
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
                        "price" to price.toInt()
                    )

                    postViewModel.editPost(
                        parentCategoryId,
                        subCategoryId,
                        postItem.id,
                        postData
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(text = stringResource(R.string.save))
            }
            Spacer(modifier = Modifier.padding(bottom = 60.dp))
        }
    }
}