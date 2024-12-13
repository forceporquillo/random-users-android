/**
 * Copyright 2024 strongforce1
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package dev.forcecodes.albertsons.data.di

import androidx.room.TypeConverter
import com.squareup.moshi.JsonAdapter
import dev.forcecodes.albertsons.data.local.LoginEntity

class Converters {
    companion object {
        private inline fun <reified T> jsonAdapter(): JsonAdapter<T> {
            return sharedMoshi.adapter(T::class.java)
        }

        @TypeConverter @JvmStatic
        fun fromLoginEntity(login: LoginEntity?): String? {
            return login.let { sharedMoshi.adapter(LoginEntity::class.java).toJson(it) }
        }

        @TypeConverter @JvmStatic
        fun toLoginEntity(loginJson: String): LoginEntity? {
            return loginJson.let {
                sharedMoshi.adapter(LoginEntity::class.java).fromJson(it)
            }
        }
    }
}
