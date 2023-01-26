package com.google.samples.apps.sunflower.shared.di

import co.touchlab.sqliter.DatabaseConfiguration
import com.google.samples.apps.sunflower.Database
import com.google.samples.apps.sunflower.shared.common.DatabaseInitializer
import com.google.samples.apps.sunflower.shared.common.ioDispatcher
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import com.squareup.sqldelight.drivers.native.wrapConnection
import io.ktor.client.engine.darwin.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.koin.dsl.module
import sunflower_kmm.shared.BuildConfig

actual val platformModule = module {
    single { Darwin.create { } }
    single<SqlDriver> {
        val schema = Database.Schema
        val configuration = DatabaseConfiguration(
            name = "test.db",
            version = schema.version,
            create = { connection ->
                wrapConnection(connection) {
                    schema.create(it)
                }
                MainScope().launch(ioDispatcher) {
                    DatabaseInitializer(get()).init()
                }
            },
            upgrade = { connection, oldVersion, newVersion ->
                wrapConnection(connection) { schema.migrate(it, oldVersion, newVersion) }
            }
        )

        NativeSqliteDriver(configuration)
    }
}

actual val API_ACCESS_KEY = BuildConfig.UNSPLASH_KEY