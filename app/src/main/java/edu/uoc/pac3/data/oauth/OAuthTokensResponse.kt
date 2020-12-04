package edu.uoc.pac3.data.oauth

import kotlinx.serialization.*

/**
 * Created by alex on 07/09/2020.
 */

@Serializable
data class OAuthTokensResponse(
        @SerialName("access_token") val accessToken: String,
        @SerialName("refresh_token") val refreshToken: String? = null,
)
