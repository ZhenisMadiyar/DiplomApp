package kz.example.diplomapp.data.di.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kz.example.diplomapp.domain.repository.MainRepoImpl
import kz.example.diplomapp.domain.repository.PostRepoImpl

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @Provides
    @ViewModelScoped
    fun provideMainRepository(): MainRepoImpl {
        return MainRepoImpl()
    }

    @Provides
    @ViewModelScoped
    fun providePostRepository(): PostRepoImpl {
        return PostRepoImpl()
    }
}