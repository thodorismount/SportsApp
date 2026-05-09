package com.kaizen.sportsapp.remote.dto

import android.annotation.SuppressLint
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class EventDto(
    @SerialName("i") val id: String,
    @SerialName("si") val sportId: String,
    @SerialName("d") val name: String,
    @SerialName("tt") val startTime: Long
)
