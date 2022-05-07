package kz.example.diplomapp.ui.screen.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kz.example.diplomapp.common.UiState
import kz.example.diplomapp.domain.repository.PostRepoImpl
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val repo: PostRepoImpl
) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Empty)
    val uiState: StateFlow<UiState> = _uiState

    fun getPostList(documentId: String) {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            repo.getPostList(documentId)
                .catch {
                    _uiState.value = UiState.Error(it)
                }
                .collect {
                    _uiState.value = UiState.Success(it)
                }
        }
    }

    fun createPost(categoryDocumentId: String, subCategoryDocumentId: String, postData: HashMap<String, Any>) {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            repo.createPost(categoryDocumentId, subCategoryDocumentId, postData)
                .catch {
                    _uiState.value = UiState.Error(it)
                }
                .collect { isSuccessAdded ->
                    _uiState.value = UiState.Success(isSuccessAdded)
                }
        }
    }

}