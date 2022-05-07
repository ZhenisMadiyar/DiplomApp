package kz.example.diplomapp.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import kz.example.diplomapp.R
import kz.example.diplomapp.domain.model.Post
import kz.example.diplomapp.ui.theme.CallBtnColor
import kz.example.diplomapp.ui.theme.PriceColor
import java.text.NumberFormat

@Composable
fun PostDetail(
    navHostController: NavHostController,
    postItem: Post
) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "77777865756"))
            startActivity(context, intent, null)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = Color.White,
                elevation = 2.dp,
                title = { Text(text = postItem.title, color = Color.Black) },
                navigationIcon = {
                    IconButton(onClick = {
                        navHostController.popBackStack()
                    }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "backDetail")
                    }
                }
            )
        }
    ) { contentPadding ->
        Box(modifier = Modifier.padding(contentPadding)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
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
                        .height(240.dp),
                    contentScale = ContentScale.Crop
                )

                Column(Modifier.padding(16.dp)) {
                    Text(
                        text = postItem.title,
                        style = TextStyle(
                            fontSize = 22.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Spacer(modifier = Modifier.padding(top = 8.dp))
                    Text(
                        text = NumberFormat.getInstance()
                            .format(postItem.price) + "тг.",
                        style = TextStyle(
                            fontSize = 24.sp,
                            color = PriceColor,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Spacer(modifier = Modifier.padding(top = 8.dp))
                    Text(
                        text = postItem.description,
                        style = TextStyle(
                            fontSize = 14.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.Medium
                        )
                    )

                    Spacer(modifier = Modifier.padding(top = 8.dp))
                    Text(
                        text = postItem.createdDate?.toDate().toString(),
                        style = TextStyle(
                            fontSize = 12.sp,
                            color = Color.Gray,
                            fontWeight = FontWeight.Medium
                        )
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        onClick = {
                            // Check permission
                            when (PackageManager.PERMISSION_GRANTED) {
                                ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.CALL_PHONE
                                ) -> {
                                    val intent = Intent(
                                        Intent.ACTION_CALL,
                                        Uri.parse("tel:" + "77777865756")
                                    )
                                    startActivity(context, intent, null)
                                }
                                else -> {
                                    launcher.launch(Manifest.permission.CALL_PHONE)
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = CallBtnColor),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(text = stringResource(R.string.call), color = Color.White)
                    }
                    Spacer(modifier = Modifier.padding(bottom = 60.dp))
                }
            }
        }
    }
}
