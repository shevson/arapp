package com.shevson.androidrecruitmentapp.util

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class RxSchedulersProvider @Inject constructor() {
    val main: Scheduler by lazy { AndroidSchedulers.mainThread() }
    val IO: Scheduler by lazy { Schedulers.io() }
    val compute: Scheduler by lazy { Schedulers.computation() }
}