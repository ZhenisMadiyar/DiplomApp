package kz.example.diplomapp.data.repository

import kotlinx.coroutines.flow.Flow
import kz.example.diplomapp.domain.model.Post

interface PostRepo {
    suspend fun getPostList(categoryId: String, parentDocumentId: String): Flow<List<Post>>
    suspend fun createPost(documentId: String, subDocumentId: String, postData: HashMap<String, Any>): Flow<Boolean>
}