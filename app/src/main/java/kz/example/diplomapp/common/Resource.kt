package kz.example.diplomapp.common

import java.lang.Exception

sealed class Resource<out T> {
    class Success<out T>(val data: T?) : Resource<T>()
    class Loading<out T> : Resource<T>()
    class Error(val exception: Exception?) : Resource<Nothing>()
}
