package com.shevson.androidrecruitmentapp.util

import android.widget.ImageView
import com.shevson.androidrecruitmentapp.R
import com.squareup.picasso.Picasso

fun ImageView.loadAvatar(avatarUrl: String?) {
    if (avatarUrl?.isNotEmpty() == true) {
        Picasso.get()
            .load(avatarUrl)
            .error(R.drawable.ic_android_logo)
            .placeholder(R.drawable.ic_android_logo)
            .fit()
            .centerInside()
            .into(this)
    }
}