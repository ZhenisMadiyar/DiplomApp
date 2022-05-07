package kz.example.diplomapp.common

sealed class UiState {
    class Success<out T>(val list: T) : UiState()
    class Error(val exception: Throwable) : UiState()
    object Loading : UiState()
    object Empty : UiState()
}
