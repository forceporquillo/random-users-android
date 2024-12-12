package dev.forcecodes.albertsons.core.di

import androidx.room.TypeConverter
import com.squareup.moshi.JsonAdapter
import dev.forcecodes.albertsons.core.local.DobEntity
import dev.forcecodes.albertsons.core.local.LocationEntity
import dev.forcecodes.albertsons.core.local.LoginEntity
import dev.forcecodes.albertsons.core.local.NameEntity
import dev.forcecodes.albertsons.core.local.RegisteredEntity

class EntityConverters {

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

//
//    @TypeConverter
//    fun fromNameEntity(name: NameEntity?): String? {
//        return name?.let { jsonAdapter<NameEntity>().toJson(it) }
//    }
//
//    @TypeConverter
//    fun toNameEntity(nameJson: String?): NameEntity? {
//        return nameJson?.let {
//            jsonAdapter<NameEntity>().fromJson(it)
//        }
//    }
//
//    @TypeConverter
//    fun fromDobEntity(dob: DobEntity?): String? {
//        return dob?.let { jsonAdapter<DobEntity>().toJson(it) }
//    }
//
//    @TypeConverter
//    fun toDobEntity(dobJson: String?): DobEntity? {
//        return dobJson?.let {
//            jsonAdapter<DobEntity>().fromJson(it)
//        }
//    }
//
//    @TypeConverter
//    fun fromRegisteredEntity(registered: RegisteredEntity?): String? {
//        return registered?.let { jsonAdapter<RegisteredEntity>().toJson(it) }
//    }
//
//    @TypeConverter
//    fun toRegisteredEntity(registeredJson: String?): RegisteredEntity? {
//        return registeredJson?.let {
//            jsonAdapter<RegisteredEntity>().fromJson(it)
//        }
//    }
//
//    @TypeConverter
//    fun fromLocationEntity(location: LocationEntity): String? {
//        return location.let { jsonAdapter<LocationEntity>().toJson(it) }
//    }
//
//    @TypeConverter
//    fun toLocationEntity(locationJson: String): LocationEntity? {
//        return locationJson.let {
//            jsonAdapter<LocationEntity>().fromJson(it)
//        }
//    }
}
