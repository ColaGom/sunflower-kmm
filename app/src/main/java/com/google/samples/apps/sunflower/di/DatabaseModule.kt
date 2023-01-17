/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.samples.apps.sunflower.di

import android.content.Context
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.google.samples.apps.sunflower.Database
import com.google.samples.apps.sunflower.data.GardenPlantingDao
import com.google.samples.apps.sunflower.data.PlantDao
import com.google.samples.apps.sunflower.utilities.PLANT_DATA_FILENAME
import com.google.samples.apps.sunflower.workers.SeedDatabaseWorker
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val databaseModule = module {
    single<SqlDriver> {
        AndroidSqliteDriver(
            Database.Schema,
            get(),
            "test.db",
            callback = CustomCallback(get())
        )
    }
    single { Database(get()) }
    factory { get<Database>().gardenPlantingsQueries }
    factory { get<Database>().plantsQueries }
    factoryOf(::PlantDao)
    factoryOf(::GardenPlantingDao)
}

private class CustomCallback(
    private val context: Context
) : SupportSQLiteOpenHelper.Callback(Database.Schema.version) {
    private val delegate: AndroidSqliteDriver.Callback =
        AndroidSqliteDriver.Callback(Database.Schema)

    override fun onCreate(db: SupportSQLiteDatabase) {
        delegate.onCreate(db)
        val request = OneTimeWorkRequestBuilder<SeedDatabaseWorker>()
            .setInputData(workDataOf(SeedDatabaseWorker.KEY_FILENAME to PLANT_DATA_FILENAME))
            .build()
        WorkManager.getInstance(context).enqueue(request)
    }

    override fun onUpgrade(db: SupportSQLiteDatabase, oldVersion: Int, newVersion: Int) {
        delegate.onUpgrade(db, oldVersion, newVersion)
    }
}