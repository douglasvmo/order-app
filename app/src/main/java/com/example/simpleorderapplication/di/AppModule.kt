package com.example.simpleorderapplication.di

import com.example.simpleorderapplication.data.models.Order
import com.example.simpleorderapplication.data.models.Product
import com.example.simpleorderapplication.data.repository.OrderRepository
import com.example.simpleorderapplication.ui.orderlist.OrderListViewModel
import com.example.simpleorderapplication.ui.orderproducts.OrderProductsViewModel
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val databaseModule = module {

    single {
        val config = RealmConfiguration.create(
            schema = setOf(
                Order::class,
                Product::class
            )
        )

        Realm.open(config)
    }
}

val repositoryModule = module {
    single { OrderRepository(get()) }
}

val viewModelModule = module {
    viewModel { OrderListViewModel(get()) }

    viewModel { OrderProductsViewModel( get()) }
}
