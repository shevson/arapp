package com.shevson.androidrecruitmentapp.data.remote

import com.shevson.androidrecruitmentapp.data.local.RepoDbModel

class ReposItem (
    val id: Int,
    val node_id: String,
    val name: String,
    val full_name: String,
    val gravatar_id: String,
    val private: Boolean,
    val owner: UsersItem,
    val html_url: String,
    val description: String?
)

fun ReposItem.toRepoDbModel() = RepoDbModel(
    id = id,
    name = name
)