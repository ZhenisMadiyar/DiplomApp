package kz.example.diplomapp.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavRoutes(val route: String, val icon: ImageVector? = null, val title: String = "") {
    object SplashScreen: NavRoutes("splash_screen")
    object CategoryList : NavRoutes("category_list", Icons.Default.List, "Категория")
    object SubCategoryList : NavRoutes("subcategory_list") {
        const val routeWithArgument: String = "subcategory_list/{category_id}/{category_name}"
        const val EXTRA_CATEGORY_ID = "category_id"
        const val EXTRA_CATEGORY_NAME = "category_name"
    }
    object PostList : NavRoutes("post_list") {
        const val routeWithArgument: String = "post_list/{category_id}/{category_name}/{parent_category_id}"
        const val EXTRA_CATEGORY_ID = "category_id"
        const val EXTRA_CATEGORY_NAME = "category_name"
        const val EXTRA_PARENT_CATEGORY_ID = "parent_category_id"
    }
    object PostDetail: NavRoutes("post_detail") {
        const val routeWithArgument: String = "post_detail/{post_item}"
        const val EXTRA_POST_ITEM = "post_item"
    }
    object PostCreate: NavRoutes("post_create", Icons.Default.AddCircle, "Создать")
    object Favourites: NavRoutes("favourite_posts", Icons.Default.Favorite, "Избранные")
}