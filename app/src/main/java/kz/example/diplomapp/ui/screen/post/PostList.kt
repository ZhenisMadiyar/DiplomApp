package kz.example.diplomapp.ui

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.google.gson.Gson
import kz.example.diplomapp.R
import kz.example.diplomapp.common.NavRoutes
import kz.example.diplomapp.common.UiState
import kz.example.diplomapp.domain.model.Post
import kz.example.diplomapp.ui.screen.common.ErrorDialog
import kz.example.diplomapp.ui.screen.post.PostViewModel
import kz.example.diplomapp.ui.theme.PriceColor
import java.text.NumberFormat

@Composable
fun PostList(
    viewModel: PostViewModel,
    navController: NavHostController,
    documentId: String,
    categoryName: String,
    parentDocumentId: String
) {

    LaunchedEffect(Unit) {
        viewModel.getPostList(documentId, parentDocumentId)
        Log.i("Opened post list", "POST_LIST")
    }
    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = Color.White,
                elevation = 12.dp,
                title = { Text(text = categoryName, color = Color.Black) },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }) {
                        Icon(Icons.Filled.ArrowBack, "backIcon")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    //open create post screen
                    navController.navigate("${NavRoutes.PostCreate.route}/$documentId")
                },
                backgroundColor = Color.White
            ) {
                Icon(Icons.Default.Add, "")
            }
        }
    ) { contentPadding ->
        when (val state = viewModel.uiState.collectAsState().value) {
            is UiState.Loading -> {

            }
            is UiState.Success<*> -> {
                val data = state.list as List<Post>
                Log.i("PostData", data.size.toString())
                if (data.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .padding(contentPadding)
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {

                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = CenterHorizontally
                        ) {
                            Image(
                                painterResource(id = R.drawable.empty_box),
                                "",
                                modifier = Modifier.size(96.dp)
                            )
                            Text(
                                text = stringResource(R.string.empty_category),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
                    PostListLoaded(data, navController, categoryName, contentPadding)
                }
            }
            is UiState.Error -> {
                ErrorDialog(message = state.exception.toString())
            }
        }
    }
}

@Composable
fun PostListLoaded(
    data: List<Post>,
    navController: NavHostController,
    categoryName: String,
    contentPadding: PaddingValues
) {
    LazyColumn(modifier = Modifier.padding(contentPadding)) {
        data.forEach { postItem ->
            item {
                MaterialTheme {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .clickable(
                                interactionSource = remember {
                                    MutableInteractionSource()
                                },
                                indication = rememberRipple(bounded = true),
                                onClick = {
                                    val jsonPost = Uri.encode(Gson().toJson(postItem))
                                    navController.navigate("${NavRoutes.PostDetail.route}/$jsonPost")
                                }
                            )) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            elevation = 2.dp
                        ) {

                            val painter = rememberAsyncImagePainter(
                                model = postItem.thumb,
                                error = painterResource(id = R.drawable.kinoroom)
                            )
                            Image(
                                painter = painter,
                                contentDescription = "",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Spacer(modifier = Modifier.padding(top = 8.dp))
                                Text(
                                    text = postItem.title,
                                    style = TextStyle(
                                        fontSize = 18.sp,
                                        color = Color.Black,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Spacer(modifier = Modifier.padding(top = 4.dp))
                                Text(
                                    text = postItem.description,
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        color = Color.Black,
                                        fontWeight = FontWeight.Medium
                                    ),
                                    maxLines = 3,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Box(modifier = Modifier.align(Alignment.CenterVertically)) {
                                Text(
                                    text = NumberFormat.getInstance()
                                        .format(postItem.price) + "тг.",
                                    style = TextStyle(
                                        fontSize = 24.sp,
                                        color = PriceColor,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }

                    }
                }
            }
        }
    }
}