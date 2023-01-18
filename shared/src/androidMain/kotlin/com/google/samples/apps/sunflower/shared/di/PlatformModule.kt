package com.google.samples.apps.sunflower.shared.di

import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.google.samples.apps.sunflower.Database
import com.google.samples.apps.sunflower.shared.common.DatabaseInitializer
import com.google.samples.apps.sunflower.shared.data.PlantDao
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import io.ktor.client.engine.okhttp.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.dsl.module
import sunflower_kmm.shared.BuildConfig


actual val platformModule = module {
    single { OkHttp.create { } }
    single<SqlDriver> {
        AndroidSqliteDriver(
            Database.Schema,
            get(),
            "test.db",
            callback = CustomCallback()
        )
    }
}

actual val API_ACCESS_KEY: String = BuildConfig.UNSPLASH_KEY

private class CustomCallback : SupportSQLiteOpenHelper.Callback(Database.Schema.version),
    KoinComponent {
    private val plantDao by inject<PlantDao>()

    private val delegate: AndroidSqliteDriver.Callback =
        AndroidSqliteDriver.Callback(Database.Schema)

    override fun onCreate(db: SupportSQLiteDatabase) {
        delegate.onCreate(db)

        MainScope().launch(Dispatchers.IO) {
            DatabaseInitializer(plantDao).init()
        }
    }

    override fun onUpgrade(db: SupportSQLiteDatabase, oldVersion: Int, newVersion: Int) {
        delegate.onUpgrade(db, oldVersion, newVersion)
    }
}