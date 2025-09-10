package com.example.newpokedex.data.remote.responses


import com.example.newpokedex.data.remote.responses.StatX
import com.google.gson.annotations.SerializedName

data class Stat(
    @SerializedName("base_stat")
    val baseStat: Int,
    val effort: Int,
    val stat: StatX
)