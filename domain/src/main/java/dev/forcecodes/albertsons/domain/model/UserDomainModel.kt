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
package dev.forcecodes.albertsons.domain.model

data class UserSimpleInfo(
    val id: String,
    val fullName: String,
    val address: String,
    val email: String,
    val thumbnailUrl: String,
)

data class UserDetails(
    val fullName: String? = null,
    val email: String? = null,
    val dob: String? = null,
    val age: Int? = null,
    val dateRegistered: String? = null,
    val phone: String? = null,
    val cell: String? = null,
    val address: String? = null,
    val timezone: String? = null,
    val loginCredentials: LoginCredentials? = null,
)

data class LoginCredentials(
    val uuid: String? = null,
    val username: String? = null,
    val password: String? = null,
    val salt: String? = null,
    val md5: String? = null,
    val sha1: String? = null,
    val sha256: String? = null,
)
