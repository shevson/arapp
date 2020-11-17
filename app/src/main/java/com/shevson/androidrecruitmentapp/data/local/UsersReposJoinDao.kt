package com.shevson.androidrecruitmentapp.data.local

import androidx.room.*
import com.shevson.androidrecruitmentapp.data.local.LocalDb.Companion.COLUMN_ID
import com.shevson.androidrecruitmentapp.data.local.LocalDb.Companion.COLUMN_NAME
import com.shevson.androidrecruitmentapp.data.local.LocalDb.Companion.COLUMN_REPO_ID
import com.shevson.androidrecruitmentapp.data.local.LocalDb.Companion.COLUMN_USER_ID
import com.shevson.androidrecruitmentapp.data.local.LocalDb.Companion.TABLE_REPOS
import com.shevson.androidrecruitmentapp.data.local.LocalDb.Companion.TABLE_USERS_REPOS_JOIN
import io.reactivex.Flowable

@Dao
abstract class UsersReposJoinDao(private val db: LocalDb) {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(link: UsersReposJoin): Long

    @Query("""
        SELECT $TABLE_REPOS.$COLUMN_NAME FROM $TABLE_REPOS
        INNER JOIN $TABLE_USERS_REPOS_JOIN
        ON $TABLE_REPOS.$COLUMN_ID=$TABLE_USERS_REPOS_JOIN.$COLUMN_REPO_ID
        WHERE $TABLE_USERS_REPOS_JOIN.$COLUMN_USER_ID=:userId
    """)
    abstract fun getReposNamesForUser(userId: Int): Flowable<List<String>>

    @Transaction
    open fun insertReposForUser(userId: Int, repos: List<RepoDbModel>) {
        db.reposDao().insert(repos)
        repos.forEach {
            insert(UsersReposJoin(userId, it.id))
        }
    }
}