package com.albab.mycollection.view.common

data class SearchModelState(
    val searchText: String = "",
    val list: List<Any> = arrayListOf(),
    val resultsFound: Boolean = true
)