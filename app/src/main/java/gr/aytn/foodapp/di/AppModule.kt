package gr.aytn.foodapp.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import gr.aytn.foodapp.data.datasource.FoodDataSource
import gr.aytn.foodapp.repo.FoodRepository
import gr.aytn.foodapp.retrofit.ApiUtils
import gr.aytn.foodapp.retrofit.FoodDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun providesFoodDao(): FoodDao {
        return ApiUtils.getFoodDao()
    }

    @Provides
    @Singleton
    fun providesFoodDataSource(dao: FoodDao): FoodDataSource{
        return FoodDataSource(dao)
    }

    @Provides
    @Singleton
    fun providesFoodRepository(fds: FoodDataSource): FoodRepository{
        return FoodRepository(fds)
    }
}