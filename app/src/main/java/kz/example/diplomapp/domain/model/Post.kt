package kz.example.diplomapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Post(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val thumb: String = "",
    val price: Int = 0,
    val createdDate: com.google.firebase.Timestamp? = null
): Parcelable