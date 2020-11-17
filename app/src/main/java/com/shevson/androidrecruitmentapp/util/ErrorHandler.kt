package com.shevson.androidrecruitmentapp.util

import android.content.Context
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.google.android.material.snackbar.Snackbar
import com.shevson.androidrecruitmentapp.util.ErrorCodes.*
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import java.lang.ref.WeakReference
import java.util.concurrent.TimeUnit

class ErrorHandler(
    activity: FragmentActivity,
    snackBarContainerView: View
) {

    private val errorStream = PublishSubject.create<ErrorData>()
    private val activityRef = WeakReference<FragmentActivity>(activity)
    private val snackBarContainerViewRef = WeakReference<View>(snackBarContainerView)
    private var errorStreamDisposable: Disposable

    init {
        errorStreamDisposable = errorStream
            .window(1, TimeUnit.SECONDS)
            .switchMap { source -> source.lastElement().toObservable() }
            .subscribe { displayErrorSnackBar(it.errorCode, it.message) }
    }

    fun displayError(error: ErrorData) {
        errorStream.onNext(error)
    }

    private fun displayErrorSnackBar(errorCode: ErrorCodes?, message: String?) {
        val activity = activityRef.get() ?: return
        val snackBarContainer = snackBarContainerViewRef.get() ?: return
        var errorMessage = when (errorCode) {
            NO_NETWORK -> "No network :("
            TIMEOUT -> "Connection timed out"
            SERVER_UNRESPONSIVE -> "API service is not responding"
            else -> "Unknown Error"
        }
        activity.showSnackBarShort(snackBarContainer, message ?: errorMessage)
    }

}

fun Context?.showSnackBarShort(view: View, message: String) {
    this?.run { Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show() }
}

enum class ErrorCodes {
    NO_NETWORK,
    TIMEOUT,
    SERVER_UNRESPONSIVE
}

data class ErrorData(
    val errorCode: ErrorCodes?,
    val message: String?
)