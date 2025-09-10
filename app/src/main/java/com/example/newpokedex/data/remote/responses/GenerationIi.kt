package com.example.newpokedex.data.remote.responses


import com.example.newpokedex.data.remote.responses.Crystal
import com.google.gson.annotations.SerializedName

data class GenerationIi(
    val crystal: Crystal,
    val gold: Gold,
    val silver: Silver
)