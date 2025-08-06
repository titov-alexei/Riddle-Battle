package com.example.riddlebattleoftheteam.data.api

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.GET

interface RiddleApiService {
    @GET("v1/riddles")
    suspend fun getRiddles(): Response<List<RiddleApiResponse>>
}

data class RiddleApiResponse(
    @SerializedName("title") val title: String,
    @SerializedName("question") val question: String,
    @SerializedName("answer") val answer: String
)