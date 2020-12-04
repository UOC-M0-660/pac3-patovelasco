package edu.uoc.pac3.data

import android.content.Context
import android.content.SharedPreferences
import edu.uoc.pac3.data.oauth.OAuthConstants.sharedAccessToken
import edu.uoc.pac3.data.oauth.OAuthConstants.sharedFileName
import edu.uoc.pac3.data.oauth.OAuthConstants.sharedNoDataFound
import edu.uoc.pac3.data.oauth.OAuthConstants.sharedRefreshToken


/**
 * Created by alex on 06/09/2020.
 */

class SessionManager(context: Context) {
    private val sharedPref: SharedPreferences = context.getSharedPreferences(sharedFileName, 0)

    fun isUserAvailable(): Boolean {
        return !getAccessToken().isNullOrEmpty()
    }

    fun getAccessToken(): String? {
        return sharedPref.getString(sharedAccessToken, sharedNoDataFound)
    }

    fun saveAccessToken(accessToken: String) {
        sharedPref.edit().putString(sharedAccessToken, accessToken).apply()
    }

    fun clearAccessToken() {
        sharedPref.edit().remove(sharedAccessToken).apply()
    }

    fun getRefreshToken(): String? {
        return sharedPref.getString(sharedRefreshToken, sharedNoDataFound)
    }

    fun saveRefreshToken(refreshToken: String) {
        sharedPref.edit().putString(sharedRefreshToken, refreshToken).apply()
    }

    fun clearRefreshToken() {
        sharedPref.edit().remove(sharedRefreshToken).apply()
    }

}