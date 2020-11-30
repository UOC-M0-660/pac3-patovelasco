package edu.uoc.pac3.data.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Created by alex on 07/09/2020.
 */

@Serializable
data class User(
        @SerialName("display_name") val userName: String? = null,
        @SerialName("description") val description: String? = null,
        @SerialName("profile_image_url") val profileImageUrl: String? = null,
        @SerialName("view_count") val viewCount: String? = null,
)

@Serializable
data class UserResponse(
        @SerialName("data") val data: List<User>? = null,
)