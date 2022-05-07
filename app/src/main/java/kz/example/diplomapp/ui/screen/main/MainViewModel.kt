package kz.example.diplomapp.ui.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kz.example.diplomapp.common.UiState
import kz.example.diplomapp.domain.repository.MainRepoImpl
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repo: MainRepoImpl
) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    fun getCategory() {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            repo.getCategories()
                .catch {
                    _uiState.value = UiState.Error(it)
                }
                .collect {
                    _uiState.value = UiState.Success(it)
                }
        }
    }

    fun getSubCategory(categoryId: String) {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            repo.getSubCategories(categoryId)
                .catch {
                    _uiState.value = UiState.Error(it)
                }
                .collect {
                    _uiState.value = UiState.Success(it)
                }
        }
    }
}