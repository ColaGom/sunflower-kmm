/*
 * Copyright 2023 Google LLC
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

package com.google.samples.apps.sunflower.shared.common

import kotlinx.datetime.Instant
import java.text.SimpleDateFormat
import java.util.*

actual object DateFormatter {
    actual fun format(instant: Instant): String = dateFormat.format(instant.toJavaDate())
    private val dateFormat = SimpleDateFormat("MMM d, yyyy", Locale.US)
}

fun Instant.toJavaDate() = Date(toEpochMilliseconds())