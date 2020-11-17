package com.shevson.androidrecruitmentapp.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = LocalDb.TABLE_REPOS,
    indices = [Index(value = [LocalDb.COLUMN_ID], unique = false)]
)

data class RepoDbModel(
    @PrimaryKey val id: Int,
    val name: String = ""
)