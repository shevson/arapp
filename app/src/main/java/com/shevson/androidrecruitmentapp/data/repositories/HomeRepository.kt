package com.shevson.androidrecruitmentapp.data.repositories

import com.shevson.androidrecruitmentapp.data.local.UserDbModel
import com.shevson.androidrecruitmentapp.data.local.UsersDao
import com.shevson.androidrecruitmentapp.data.local.UsersReposJoinDao
import com.shevson.androidrecruitmentapp.data.remote.ApiService
import com.shevson.androidrecruitmentapp.data.remote.toRepoDbModel
import com.shevson.androidrecruitmentapp.data.remote.toUserDbModel
import io.reactivex.Completable
import io.reactivex.Observable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import javax.inject.Inject


interface HomeRepository {
    fun getUsers(): Observable<List<UserDbModel>>
    fun getReposNamesForUser(userId: Int): Observable<List<String>>
    fun updateUsers(): Completable
    suspend fun updateReposForUserLogin()
}

class HomeRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val usersDao: UsersDao,
    private val usersReposJoinDao: UsersReposJoinDao
) : HomeRepository {

    override fun getUsers(): Observable<List<UserDbModel>> =
        usersDao.selectAll().toObservable()

    override fun getReposNamesForUser(userId: Int): Observable<List<String>> =
        usersReposJoinDao.getReposNamesForUser(userId).toObservable()

    override fun updateUsers(): Completable {
        return apiService.getUsers()
            .map { items ->
                usersDao.insert(items.take(30).map {
                    it.toUserDbModel()
                })
            }
            .ignoreElement()
    }

    override suspend fun updateReposForUserLogin() {
        withContext(Dispatchers.IO) {
            val usersList = usersDao.getAll()
            usersList.map { user ->
                async {
                    apiService.getUsersRepos(user.login)
                }
            }.awaitAll().forEach {
                if (it.isNotEmpty()) {
                    usersReposJoinDao.insertReposForUser(it[0].owner.id, it.take(3).map { item ->
                        item.toRepoDbModel()
                    })
                }
            }
        }
    }
}
