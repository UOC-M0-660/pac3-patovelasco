package edu.uoc.pac3.data.network

/**
 * Created by alex on 07/09/2020.
 */
object Endpoints {

    // OAuth2 API Endpoints
    private const val oauthBaseUrl = "https://id.twitch.tv/oauth2"

    // TODO: Add all remaining endpoints
    const val authorizationUrl = "$oauthBaseUrl/authorize"
    const val tokenUrl = "$oauthBaseUrl/token"

    // Twitch API Endpoints
    private const val twitchBaseUrl = "https://api.twitch.tv/helix"

    // TODO: Add all remaining endpoints
    const val streamsUrl = "$twitchBaseUrl/streams"
    const val userUrl = "$twitchBaseUrl/users"

    const val redirectUri = "http://localhost"

}