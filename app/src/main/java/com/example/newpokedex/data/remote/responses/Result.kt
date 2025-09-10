package com.example.newpokedex.data.remote.responses


import com.google.gson.annotations.SerializedName

data class Result(
    val name: String,
    val url: String
)