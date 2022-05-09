package kz.example.diplomapp.ui.screen.main

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import kz.example.diplomapp.common.AssetParamType
import kz.example.diplomapp.common.NavRoutes
import kz.example.diplomapp.domain.model.Post
import kz.example.diplomapp.ui.CategoryList
import kz.example.diplomapp.ui.PostDetail
import kz.example.diplomapp.ui.PostList
import kz.example.diplomapp.ui.SubCategoryList
import kz.example.diplomapp.ui.screen.SplashScreen
import kz.example.diplomapp.ui.screen.favourite.FavouriteList
import kz.example.diplomapp.ui.screen.post.PostCreate

@ExperimentalMaterialApi
@Composable
fun AppNavGraph(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = NavRoutes.CategoryList.route
    ) {

        composable(NavRoutes.CategoryList.route) {
            CategoryList(hiltViewModel(), navController)
        }

        composable(NavRoutes.PostCreate.route) {
            PostCreate(hiltViewModel(), hiltViewModel(), navController)
        }

        composable(NavRoutes.Favourites.route) {
            FavouriteList(navController)
        }
        composable(NavRoutes.SplashScreen.route) {
            SplashScreen(navController)
        }

        composable(
            NavRoutes.SubCategoryList.routeWithArgument,
            arguments = listOf(
                navArgument(NavRoutes.SubCategoryList.EXTRA_CATEGORY_ID) {
                    type = NavType.StringType
                },
                navArgument(NavRoutes.SubCategoryList.EXTRA_CATEGORY_NAME) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val categoryId =
                backStackEntry.arguments?.getString(NavRoutes.SubCategoryList.EXTRA_CATEGORY_ID)
            val categoryTitle =
                backStackEntry.arguments?.getString(NavRoutes.SubCategoryList.EXTRA_CATEGORY_NAME)
            SubCategoryList(hiltViewModel(), navController, categoryId ?: "", categoryTitle ?: "")
        }

        composable(
            NavRoutes.PostList.routeWithArgument,
            arguments = listOf(
                navArgument(NavRoutes.PostList.EXTRA_CATEGORY_ID) {
                    type = NavType.StringType
                },
                navArgument(NavRoutes.PostList.EXTRA_CATEGORY_NAME) {
                    type = NavType.StringType
                },
                navArgument(NavRoutes.PostList.EXTRA_PARENT_CATEGORY_ID) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val documentId =
                backStackEntry.arguments?.getString(NavRoutes.PostList.EXTRA_CATEGORY_ID)
                    ?: return@composable
            val categoryName =
                backStackEntry.arguments?.getString(NavRoutes.PostList.EXTRA_CATEGORY_NAME)
            val parentDocumentId =
                backStackEntry.arguments?.getString(NavRoutes.PostList.EXTRA_PARENT_CATEGORY_ID)
                    ?: return@composable
            PostList(hiltViewModel(), navController, documentId, categoryName ?: "", parentDocumentId)
        }

        composable(
            NavRoutes.PostDetail.routeWithArgument,
            arguments = listOf(
                navArgument(NavRoutes.PostDetail.EXTRA_POST_ITEM) {
                    type = AssetParamType()
                })
        ) { backStackEntry ->
            val postItem =
                backStackEntry.arguments?.getParcelable<Post>(NavRoutes.PostDetail.EXTRA_POST_ITEM) as Post
            PostDetail(navHostController = navController, postItem = postItem)
        }
    }

}