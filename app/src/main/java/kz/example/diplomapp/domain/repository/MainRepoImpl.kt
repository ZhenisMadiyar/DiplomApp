package kz.example.diplomapp.domain.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kz.example.diplomapp.common.Const.CATEGORY_COLLECTION
import kz.example.diplomapp.common.Const.SUBCATEGORY_COLLECTION
import kz.example.diplomapp.common.Resource
import kz.example.diplomapp.data.repository.MainRepo
import kz.example.diplomapp.domain.model.Category

/**
 * Created by madik on 28,April,2022
 */

class MainRepoImpl : MainRepo {

    private val db = FirebaseFirestore.getInstance()

    override suspend fun getCategories(): Flow<List<Category>> = callbackFlow {
        val eventDocument = db.collection(CATEGORY_COLLECTION)

        val subs = eventDocument.addSnapshotListener { snapshot, _ ->
            if (!snapshot!!.isEmpty) {
                trySend(snapshot.toObjects(Category::class.java))
            }
        }

        awaitClose{ subs.remove() }
    }

    override suspend fun getSubCategories(categoryId: String): Flow<List<Category>> = callbackFlow {
        val eventDocument = db.collection(CATEGORY_COLLECTION)
            .document(categoryId)
            .collection(SUBCATEGORY_COLLECTION)

        val subs = eventDocument.addSnapshotListener { snapshot, _ ->
            if (!snapshot!!.isEmpty) {
                trySend(snapshot.toObjects(Category::class.java))
            } else {
                trySend(emptyList<Category>())
            }
        }

        awaitClose{ subs.remove() }
    }
}