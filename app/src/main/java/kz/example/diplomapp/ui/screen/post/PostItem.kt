package kz.example.diplomapp.ui.screen.post

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kz.example.diplomapp.R
import kz.example.diplomapp.ui.theme.PriceColor

@Composable
fun PostItem() {

    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = Color.White,
                elevation = 12.dp,
                title = { Text(text = "Home", color = Color.Black) },
                navigationIcon = {
                    IconButton(
                        onClick = {

                        }) {
                        Icon(Icons.Filled.ArrowBack, "backIcon")
                    }
                }
            )
        }
    ) { contentPadding ->
        Box(modifier = Modifier.padding(contentPadding)) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    elevation = 2.dp
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.kinoroom),
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
                            text = "Клубничный сироп",
                            style = TextStyle(
                                fontSize = 18.sp,
                                color = Color.Black,
                                fontWeight = FontWeight.Bold
                            ),
                            maxLines = 3
                        )

                        Spacer(modifier = Modifier.padding(top = 4.dp))
                        Text(
                            text = "Post description",
                            style = TextStyle(
                                fontSize = 14.sp,
                                color = Color.Black,
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                    Box(modifier = Modifier.align(Alignment.CenterVertically)) {
                        Text(
                            text = "5,000тг",
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