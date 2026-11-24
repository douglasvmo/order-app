package com.example.simpleorderapplication.di

import com.example.simpleorderapplication.data.AppDatabase
import com.example.simpleorderapplication.data.DatabaseProvider
import com.example.simpleorderapplication.data.RealmToRoomMigrator
import com.example.simpleorderapplication.data.models.RealmOrder
import com.example.simpleorderapplication.data.models.RealmProduct
import com.example.simpleorderapplication.data.repository.OrderRealmRepository
import com.example.simpleorderapplication.data.repository.OrderRoomRepository
import com.example.simpleorderapplication.domain.repository.OrdersRepository
import com.example.simpleorderapplication.ui.order.OrderViewModel
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {

    single<AppDatabase> {
        DatabaseProvider.getDatabase(androidContext())
    }

    single { get<AppDatabase>().ordersDao() }

    single {
        val config = RealmConfiguration.create(
            schema = setOf(
                RealmOrder::class,
                RealmProduct::class
            )
        )

        Realm.open(config)
    }

    single { RealmToRoomMigrator(androidContext(), get<Realm>(), get<AppDatabase>()) }
}

val repositoryModule = module {
    single { OrderRealmRepository(get()) }
    single<OrdersRepository> { OrderRoomRepository(get(), get()) }
}

val viewModelModule = module {
    viewModel { OrderViewModel(get()) }
}
