package com.shevson.androidrecruitmentapp.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = LocalDb.TABLE_USERS,
    indices = [Index(value = [LocalDb.COLUMN_ID], unique = false)]
)

data class UserDbModel(
    @PrimaryKey val id: Int,
    val login: String = "",
    val avatar_url: String = ""
)