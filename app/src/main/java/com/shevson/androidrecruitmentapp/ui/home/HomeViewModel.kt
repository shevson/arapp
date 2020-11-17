package com.shevson.androidrecruitmentapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shevson.androidrecruitmentapp.data.local.UserDbModel
import com.shevson.androidrecruitmentapp.data.repositories.HomeRepository
import com.shevson.androidrecruitmentapp.util.*
import com.shevson.androidrecruitmentapp.util.LoadingState.*
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class HomeViewModel @Inject internal constructor(
    private val rxSchedulersProvider: RxSchedulersProvider,
    private val homeRepository: HomeRepository
) : ViewModel(), CoroutineScope {

    private val parentJob = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + parentJob

    private val compositeDisposable = CompositeDisposable()

    private val _loadingState: MutableLiveData<LoadingState> = MutableLiveData()
    val loadingState: LiveData<LoadingState> = _loadingState

    private val _data: MutableLiveData<ArrayList<Pair<UserDbModel, List<String>>>> =
        MutableLiveData()
    val data: LiveData<ArrayList<Pair<UserDbModel, List<String>>>> = _data

    private val _errorData: MutableLiveData<ErrorData> = MutableLiveData()
    val errorData: LiveData<ErrorData> = _errorData

    init {
        _data.value = ArrayList()
        _loadingState.value = NOT_INITIALIZED
        subscribeToUsersData()
        updateUsersData()
    }

    private fun subscribeToUsersData() {
        homeRepository.getUsers()
            .flatMap { usersList ->
                Observable.fromIterable(usersList)
            }
            .flatMap({
                homeRepository.getReposNamesForUser(it.id)
            }, { user: UserDbModel, repos: List<String> ->
                Pair(user, repos)
            })
            .subscribeOn(rxSchedulersProvider.IO)
            .observeOn(rxSchedulersProvider.main)
            .subscribe({
                updateLiveData(it)
            }, {
                _errorData.value = ErrorData(null, "Error while trying to load data from local db")
            }).addTo(compositeDisposable)
    }

    private fun updateUsersData() {
        homeRepository.updateUsers()
            .subscribeOn(rxSchedulersProvider.IO)
            .observeOn(rxSchedulersProvider.main)
            .doOnSubscribe { _loadingState.value = IN_PROGRESS }
            .subscribe({
                updateReposForUsers()
            }, {
                _errorData.value = ErrorData(null, "Error while trying to fetch data from API")
                if (_data.value!!.isEmpty()) {
                    _loadingState.value = NO_RESULTS
                } else {
                    _loadingState.value = FINISHED
                }
            }).addTo(compositeDisposable)
    }

    private fun updateReposForUsers() {
        parentJob.cancelChildren()
        launch {
            val result = runCatching { homeRepository.updateReposForUserLogin() }
            result.onSuccess {
                _loadingState.value = FINISHED
            }.onFailure {
                _errorData.value = ErrorData(null, "Error while trying to fetch data from API")
                if (_data.value!!.isEmpty()) {
                    _loadingState.value = NO_RESULTS
                } else {
                    _loadingState.value = FINISHED
                }
            }
        }
    }

    private fun updateLiveData(item: Pair<UserDbModel, List<String>>) {
        val currentData = _data.value
        val existingPosition = currentData!!.indexOfFirst { it.first.id == item.first.id }
        if (existingPosition != -1) {
            currentData[existingPosition] = item
        } else {
            currentData.add(item)
        }
        _data.value = currentData

    }

    override fun onCleared() {
        compositeDisposable.clear()
        parentJob.cancelChildren()
    }
}

@Suppress("UNCHECKED_CAST")
class HomeViewModelFactory @Inject constructor(
    private val rxSchedulersProvider: RxSchedulersProvider,
    private val homeRepository: HomeRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeViewModel(rxSchedulersProvider, homeRepository) as T
    }
}