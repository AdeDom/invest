package com.adedom.data.models.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Fgi(
    @SerialName("now") val now: Now?,
)
