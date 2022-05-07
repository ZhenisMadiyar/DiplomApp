package kz.example.diplomapp.domain.model

import com.google.firebase.firestore.DocumentId

/**
 * Created by madik on 26,April,2022
 */
data class Category(
    @DocumentId
    val documentId: String = "",
    val id: String = "",
    val title: String? = "",
    val icon: String? = ""
)