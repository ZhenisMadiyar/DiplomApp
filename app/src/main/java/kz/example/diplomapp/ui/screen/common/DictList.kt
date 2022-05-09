package kz.example.diplomapp.ui.screen.category

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import kz.example.diplomapp.R
import kz.example.diplomapp.domain.model.Category

@Composable
fun DictList(
    categoryList: List<Category>,
    selectedCategory: (Category) -> Unit
) {
    Scaffold {
        LazyColumn(Modifier.padding(it)) {
            categoryList.forEach { categoryItem ->
                item {
                    MaterialTheme {
                        Column(Modifier.clickable(
                            interactionSource = remember {
                                MutableInteractionSource()
                            },
                            indication = rememberRipple(bounded = true),
                            onClick = {
                                selectedCategory.invoke(categoryItem)
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
}