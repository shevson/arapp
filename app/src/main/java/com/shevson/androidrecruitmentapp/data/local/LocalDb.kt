package com.shevson.androidrecruitmentapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        UserDbModel::class,
        RepoDbModel::class,
        UsersReposJoin::class
    ], version = 1
)
abstract class LocalDb : RoomDatabase() {

    companion object {
        private const val DATABASE_NAME = "local-database"

        const val TABLE_USERS = "users"
        const val TABLE_REPOS = "repos"
        const val TABLE_USERS_REPOS_JOIN = "users_repos_join"

        internal const val COLUMN_ID = "id"
        internal const val COLUMN_USER_ID = "user_id"
        internal const val COLUMN_REPO_ID = "repo_id"
        internal const val COLUMN_NAME = "name"

        @JvmStatic
        fun init(context: Context): LocalDb {
            return buildDatabase(context)
        }

        private fun buildDatabase(context: Context): LocalDb {
            return Room.databaseBuilder(context, LocalDb::class.java, DATABASE_NAME)
                .build()
        }
    }

    abstract fun usersDao(): UsersDao
    abstract fun reposDao(): ReposDao
    abstract fun usersReposDao(): UsersReposJoinDao
}