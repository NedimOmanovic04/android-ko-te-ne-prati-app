package com.example.instagram_followers

data class StringData(
    val value: String?,
    val href: String
)

data class UserEntry(
    val title: String?,
    val string_list_data: List<StringData>
)

data class FollowingRoot(
    val relationships_following: List<UserEntry>
)