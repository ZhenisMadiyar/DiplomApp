package kz.example.diplomapp.domain.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kz.example.diplomapp.common.Const
import kz.example.diplomapp.data.repository.PostRepo
import kz.example.diplomapp.domain.model.Post

class PostRepoImpl : PostRepo {

    private val firestore = FirebaseFirestore.getInstance()

    override suspend fun getPostList(categoryId: String): Flow<List<Post>> = callbackFlow {

        val event = firestore.collection(Const.CATEGORY_COLLECTION)
            .document(categoryId)
            .collection(Const.POST_COLLECTION)

        val subs = event.addSnapshotListener { documentSnapshot, _ ->
            if (!documentSnapshot!!.isEmpty) {
                trySend(documentSnapshot.toObjects(Post::class.java))
            } else trySend(emptyList<Post>())
        }

        awaitClose { subs.remove() }
    }

    override suspend fun createPost(
        subDocumentId: String,
        documentId: String,
        postData: HashMap<String, Any>
    ): Flow<Boolean> = callbackFlow {
        firestore.collection(Const.CATEGORY_COLLECTION)
            .document(subDocumentId)
            .collection(Const.SUBCATEGORY_COLLECTION)
            .document(documentId)
            .collection(Const.POST_COLLECTION)
            .add(postData)
            .addOnSuccessListener {
                trySend(true)
            }
            .addOnFailureListener {
                trySend(false)
            }

        awaitClose { close() }
    }
}