package com.shevson.androidrecruitmentapp.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.shevson.androidrecruitmentapp.data.local.LocalDb.Companion.COLUMN_ID
import com.shevson.androidrecruitmentapp.data.local.LocalDb.Companion.COLUMN_REPO_ID
import com.shevson.androidrecruitmentapp.data.local.LocalDb.Companion.COLUMN_USER_ID
import com.shevson.androidrecruitmentapp.data.local.LocalDb.Companion.TABLE_USERS_REPOS_JOIN

@Entity(
    tableName = TABLE_USERS_REPOS_JOIN,
    primaryKeys = [COLUMN_USER_ID, COLUMN_REPO_ID],
    foreignKeys = [
        ForeignKey(
            entity = RepoDbModel::class,
            parentColumns = [COLUMN_ID],
            childColumns = [COLUMN_REPO_ID])
    ],
    indices = [Index(value = [COLUMN_USER_ID]), Index(value = [COLUMN_REPO_ID])]
)
data class UsersReposJoin(
    val user_id: Int,
    val repo_id: Int
)