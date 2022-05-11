package kz.example.diplomapp.domain.model

import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import kotlinx.parcelize.Parcelize

@Parcelize
data class Post(
    @DocumentId
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val thumb: String = "",
    val price: Int = 0,
    val createdDate: com.google.firebase.Timestamp? = null
): Parcelable {
    fun toMap(): HashMap<String, Any> {
        val hashMap = HashMap<String, Any>()
        hashMap["id"] = id
        hashMap["title"] = title
        hashMap["description"] = description
        hashMap["thumb"] = thumb
        hashMap["price"] = price
        hashMap["createdDate"] = createdDate ?: ""
        return hashMap
    }
}