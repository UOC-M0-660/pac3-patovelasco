package edu.uoc.pac3.data

import android.util.Log
import edu.uoc.pac3.data.network.Endpoints.redirectUri
import edu.uoc.pac3.data.network.Endpoints.streamsUrl
import edu.uoc.pac3.data.network.Endpoints.tokenUrl
import edu.uoc.pac3.data.network.Endpoints.userUrl
import edu.uoc.pac3.data.oauth.OAuthConstants.clientID
import edu.uoc.pac3.data.oauth.OAuthConstants.clientSecret
import edu.uoc.pac3.data.oauth.OAuthTokensResponse
import edu.uoc.pac3.data.oauth.UnauthorizedException
import edu.uoc.pac3.data.streams.StreamsResponse
import edu.uoc.pac3.data.user.User
import edu.uoc.pac3.data.user.UserResponse
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*

/**
 * Created by alex on 24/10/2020.
 */

class TwitchApiService(private val httpClient: HttpClient) {
    private val TAG = "TwitchApiService"

    /// Gets Access and Refresh Tokens on Twitch
    suspend fun getTokens(authorizationCode: String): OAuthTokensResponse? {
        // TODO("Get Tokens from Twitch")
        return try{
            val tokens = httpClient.post<OAuthTokensResponse>(tokenUrl) {
                parameter("client_id", clientID)
                parameter("client_secret", clientSecret)
                parameter("code", authorizationCode)
                parameter("grant_type", "authorization_code")
                parameter("redirect_uri", redirectUri)
            }
            Log.d(TAG, "Access Token: ${tokens.accessToken}. Refresh Token: ${tokens.refreshToken}")
            tokens
        }catch (e: ClientRequestException){
            validateStatusCode(e)
            null
        }
    }

    // Gets New Access and Refresh Tokens on Twitch
    suspend fun getNewTokens(refreshToken: String): OAuthTokensResponse? {
        return try {
            val newToken = httpClient.post<OAuthTokensResponse>(tokenUrl) {
                parameter("client_id", clientID)
                parameter("client_secret", clientSecret)
                parameter("refresh_token", refreshToken)
                parameter("grant_type", "refresh_token")
            }
            Log.d(TAG, "New Access Token: ${newToken.accessToken}. New Refresh Token: ${newToken.refreshToken}")
            newToken
        }catch (e: ClientRequestException){
            validateStatusCode(e)
            null
        }
    }

    /// Gets Streams on Twitch
    @Throws(UnauthorizedException::class)
    suspend fun getStreams(cursor: String? = null): StreamsResponse? {
        // TODO("Get Streams from Twitch")
        try {
            if(cursor.isNullOrEmpty()){
                val streams = httpClient.get<StreamsResponse>(streamsUrl) {
                    headers {
                        append("Client-Id", clientID)
                    }
                }
                Log.d(TAG, "Streams: ${streams.data?.size}")
                return streams
            // TODO("Support Pagination")
            } else {
                val streamsPagination = httpClient.get<StreamsResponse>(streamsUrl) {
                    parameter("first", 20)
                    parameter("after", cursor)
                    headers {
                        append("Client-Id", clientID)
                    }
                }
                Log.d(TAG, "Cursor Streams: ${streamsPagination.data?.size}")
                return streamsPagination
            }
        }catch (e: ClientRequestException){
            validateStatusCode(e)
            return null
        }
    }

    /// Gets Current Authorized User on Twitch
    @Throws(UnauthorizedException::class)
    suspend fun getUser(): User? {
        // TODO("Get User from Twitch")
        val user = httpClient.get<UserResponse>(userUrl) {
            headers {
                append("Client-Id", clientID)
            }
        }
        Log.d(TAG, "GET_USER: ${user.data?.get(0)?.userName}")
        return user.data?.get(0)
    }

    /// Gets Current Authorized User on Twitch
    @Throws(UnauthorizedException::class)
    suspend fun updateUserDescription(description: String): User? {
        // TODO("Update User Description on Twitch")
        val user = httpClient.put<UserResponse>(userUrl) {
            parameter("description", description)
            headers {
                append("Client-Id", clientID)
            }
        }
        Log.d(TAG, "UPDATE_USER: ${user.data?.get(0)?.userName}")
        return user.data?.get(0)
    }

    private fun validateStatusCode(e: ClientRequestException) {
        val statusCode = e.response?.status?.value
        if (statusCode == 400 || statusCode == 401) {
            throw UnauthorizedException
        }
    }
}