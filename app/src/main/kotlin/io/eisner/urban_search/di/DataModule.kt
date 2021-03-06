package io.eisner.urban_search.di

import androidx.room.Room
import io.eisner.urban_search.data.Repository
import io.eisner.urban_search.data.db.UrbanDatabase
import io.eisner.urban_search.ui.search.SearchViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val dataModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            UrbanDatabase::class.java,
            "urban_database.db"
        ).fallbackToDestructiveMigration().build()
    }

    factory { Repository(get(), get()) }

    viewModel {
        SearchViewModel(get())
    }
}