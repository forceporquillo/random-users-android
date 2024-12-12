package dev.forcecodes.albertsons.core.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.forcecodes.albertsons.core.local.UserInfoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAllUserInfo(userInfo: List<UserInfoEntity>)

    @Query("SELECT * FROM userinfoentity")
    fun getUserInfo(): Flow<List<UserInfoEntity>>

    @Query("SELECT * FROM userinfoentity LIMIT :limit")
    fun getPagedUsers(limit: Int): PagingSource<Int, UserInfoEntity>

    @Query("SELECT * FROM userinfoentity WHERE id = :id")
    fun getUserInfoById(id: String): Flow<UserInfoEntity>
}
