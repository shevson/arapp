package com.shevson.androidrecruitmentapp.data.local

import androidx.room.*

@Dao
abstract class ReposDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(repos: List<RepoDbModel>): List<Long>
}