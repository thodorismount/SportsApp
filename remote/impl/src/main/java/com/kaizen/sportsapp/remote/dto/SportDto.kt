package com.kaizen.sportsapp.remote.dto

import android.annotation.SuppressLint
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class SportDto(
    @SerialName("i") val id: String,
    @SerialName("d") val name: String,
    @SerialName("e") val events: List<EventDto>
)
