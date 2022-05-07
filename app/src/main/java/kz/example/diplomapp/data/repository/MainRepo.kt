package kz.example.diplomapp.data.repository

import kotlinx.coroutines.flow.Flow
import kz.example.diplomapp.domain.model.Category

interface MainRepo {
    suspend fun getCategories() : Flow<List<Category>>
    suspend fun getSubCategories(categoryId: String) : Flow<List<Category>>
}