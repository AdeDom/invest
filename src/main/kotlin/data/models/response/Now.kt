package com.adedom.data.models.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Now(
    @SerialName("value") val value: Int?,
    @SerialName("valueText") val valueText: String?,
)
