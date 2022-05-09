package kz.example.diplomapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import kz.example.diplomapp.R
import kz.example.diplomapp.common.NavRoutes
import kz.example.diplomapp.common.UiState
import kz.example.diplomapp.domain.model.Category
import kz.example.diplomapp.ui.screen.LoadingScreen
import kz.example.diplomapp.ui.screen.common.ErrorDialog
import kz.example.diplomapp.ui.screen.main.MainViewModel

@Composable
fun SubCategoryList(
    viewModel: MainViewModel,
    navController: NavHostController,
    categoryId: String,
    categoryTitle: String
) {

    LaunchedEffect(Unit) {
        viewModel.getSubCategory(categoryId)
    }
    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = Color.White,
                elevation = 12.dp,
                title = { Text(text = categoryTitle, color = Color.Black) },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }) {
                        Icon(Icons.Filled.ArrowBack, "backIcon")
                    }
                }
            )
        }
    ) { contentPadding ->
        when (val state = viewModel.uiState.collectAsState().value) {
            is UiState.Loading -> {
                LoadingScreen()
            }
            is UiState.Success<*> -> {
                val data = state.list as List<Category>
                if (data.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .padding(contentPadding)
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {

                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painterResource(id = R.drawable.empty_box),
                                "",
                                modifier = Modifier.size(96.dp)
                            )
                            Text(
                                text = stringResource(R.string.empty_subcategory),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
                    SubCategoryListLoaded(data, navController, contentPadding, categoryId)
                }
            }
            is UiState.Error -> {
                ErrorDialog(message = state.exception.toString())
            }
        }
    }
}

@Composable
fun SubCategoryListLoaded(
    categoryList: List<Category>,
    navController: NavHostController,
    contentPadding: PaddingValues,
    parentCategoryId: String
) {
        LazyColumn(Modifier.padding(contentPadding)) {
            categoryList.forEach { categoryItem ->
                item {
                    MaterialTheme {
                        Column(Modifier.clickable(
                            interactionSource = remember {
                                MutableInteractionSource()
                            },
                            indication = rememberRipple(bounded = true),
                            onClick = {
                                navController.navigate("${NavRoutes.PostList.route}/${categoryItem.documentId}/${categoryItem.title}/$parentCategoryId")
                            }
                        )) {
                            Row(
                                Modifier
                                    .padding(16.dp)
                            ) {
                                val painter = rememberAsyncImagePainter(
                                    model = categoryItem.icon
                                )

                                Image(
                                    painter = painter,
                                    contentDescription = "",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(36.dp)
                                )
                                Spacer(modifier = Modifier.padding(8.dp))
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.CenterVertically)
                                ) {
                                    Text(
                                        text = categoryItem.title ?: ""
                                    )
                                }
                                Spacer(
                                    modifier = Modifier
                                        .weight(1f)
                                )
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.CenterVertically)
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.ic_arrow_next_24),
                                        contentDescription = ""
                                    )
                                }
                            }
                            Divider(
                                color = colorResource(id = R.color.divider_color),
                                thickness = 0.5.dp,
                                modifier = Modifier.padding(16.dp, 0.dp, 16.dp, 0.dp)
                            )
                        }
                    }
                }
            }
        }
}