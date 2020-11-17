package com.shevson.androidrecruitmentapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shevson.androidrecruitmentapp.data.local.LocalDb.Companion.COLUMN_ID
import io.reactivex.Flowable

@Dao
abstract class UsersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(users: List<UserDbModel>): List<Long>

    @Query("SELECT * FROM ${LocalDb.TABLE_USERS} ORDER BY $COLUMN_ID")
    abstract fun selectAll(): Flowable<List<UserDbModel>>

    @Query("SELECT * FROM ${LocalDb.TABLE_USERS} ORDER BY $COLUMN_ID")
    abstract fun getAll(): List<UserDbModel>
}